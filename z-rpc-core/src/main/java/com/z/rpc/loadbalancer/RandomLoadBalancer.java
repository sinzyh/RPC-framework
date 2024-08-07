package com.z.rpc.loadbalancer;

import com.z.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomLoadBalancer implements LoadBalancer{

    /**
     * 当前轮询到的服务索引
     */
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size==0) {
            return null;
        }
        //只有1个服务，无需随机选择
        if ( size == 1) {
            return serviceMetaInfoList.get(0);
        }
        //随机轮训选择一个服务
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
