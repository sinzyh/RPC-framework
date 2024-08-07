package com.z.rpc.loadbalancer;

import com.z.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer{

    /**
     * 当前轮询到的服务索引
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        //只有1个服务，无需轮训
        int size = serviceMetaInfoList.size();
        if ( size == 1) {
            return serviceMetaInfoList.get(0);
        }
        //取模轮训
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
