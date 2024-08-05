package com.z.rpc.registry;

import cn.hutool.json.JSONUtil;
import com.z.rpc.config.RegistryConfig;
import com.z.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry  {

    private Client client;

    private KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        System.out.println("init 初始化报错");
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();

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

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        kvClient.delete(key).get();
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws ExecutionException, InterruptedException {
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        //前缀查询
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get().getKvs();

            //解析服务信息
            return kvs.stream()
                    .map(kv -> {
                        String value = kv.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException("服务发现失败", e);
        }

    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        if (client != null) {
            client.close();
        }
        if (kvClient != null) {
            kvClient.close();
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
