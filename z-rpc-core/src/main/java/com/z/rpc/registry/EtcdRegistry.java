package com.z.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.z.rpc.config.RegistryConfig;
import com.z.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry  {

    private Client client;

    private KV kvClient;

    private final Set<String> localRegistryNodeKey = new HashSet<>();

    //注册中心服务缓存
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    //监听服务集合    ConcurrentHashSet防止并发冲突
    private final Set<String> watchKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
//        System.out.println("init 初始化报错");
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        //监听心跳
        heartbeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        Lease leaseClient = client.getLeaseClient();
        //创建30s的租约
        long leaseID = leaseClient.grant(30).get().getID();

        //设置存储的键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将租约和key-value进行绑定,并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseID).build();
        kvClient.put(key, value, putOption).get();

        //本地载入服务信息
        localRegistryNodeKey.add(registryKey);

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        kvClient.delete(key).get();

        //本地移除服务信息
        localRegistryNodeKey.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws ExecutionException, InterruptedException {
        //优先从本地缓存获取服务
        List<ServiceMetaInfo> cacheServiceMetaInfoList = registryServiceCache.readCache();
        if (cacheServiceMetaInfoList!=null){
            return cacheServiceMetaInfoList;
        }

        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        //前缀查询
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get().getKvs();

            //解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = kvs.stream()
                    .map(kv -> {
                        //监听key的变化
                        String key = kv.getKey().toString(StandardCharsets.UTF_8);
                        consumerWatch(key);
                        String value = kv.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
            //写入缓存服务信息
            registryServiceCache.writeCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        }catch (Exception e){
            throw new RuntimeException("服务发现失败", e);
        }

    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        //遍历本地注册的服务
        for (String key : localRegistryNodeKey) {
            try {
                kvClient.delete(ByteSequence.from(key,StandardCharsets.UTF_8)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }
        if (client != null) {
            client.close();
        }
        if (kvClient != null) {
            kvClient.close();
        }

    }

    /**
     * 心跳检测，续签时间10s
     */
    @Override
    public void heartbeat() {
        //10s续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                //遍历本地注册的服务
                for (String key : localRegistryNodeKey) {
                    try {
                        List<KeyValue> kvs = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
                        if (CollUtil.isEmpty(kvs)) {
                            continue;
                        }
                        //节点未过期，进行续签
                        KeyValue keyValue = kvs.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(key + "续签失败",e);
                    }
                }
            }
        });

        //支持秒级定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 消费者监听服务节点变化
     */
    @Override
    public void consumerWatch(String serviceNodeKey)  {
        Watch watchClient = client.getWatchClient();
        //之前未被监听，开启监听
        boolean newWatch = watchKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE: //节点删除
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default: break; //其他情况不处理
                    }
                }
            });
        }
    }


    //    /**
//     * etcd官网demo
//     * @param args
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Client client = Client.builder().endpoints("http://localhost:2379").build();
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from("hello".getBytes());
//        ByteSequence value = ByteSequence.from("world".getBytes());
//
//        kvClient.put(key, value).get();
//
//        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
//        GetResponse response = getFuture.get();
//
//        kvClient.delete(key).get();
//    }
}
