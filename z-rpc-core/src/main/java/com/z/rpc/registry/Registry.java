package com.z.rpc.registry;

import com.z.rpc.config.RegistryConfig;
import com.z.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 注册中心，支持SPI技术，允许多种类型的注册中心
 */
public interface Registry {
    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    /**
     * 取消注册
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    /**
     * 发现服务（获取某服务的所有节点，消费端）
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws ExecutionException, InterruptedException;

    /**
     * 销毁服务
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartbeat();

    /**
     * 消费者鉴定服务，便于及时更新缓存
     */
    void consumerWatch(String serviceNodeKey);


}
