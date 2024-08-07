package com.z.rpc.loadbalancer;

import com.z.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性hash负载器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{


    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes=new TreeMap<>();

    //虚拟节点数
    private final static int VIRTUAL_NODE_SIZE=100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        //构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                int hash=getHash(serviceMetaInfo.getServiceAddress()+"#"+i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        //获取调用请求的hash值
        int hash=getHash(requestParams);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null){
            //如果没有大于等于调用请求hash值的节点，则取第一个节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * hash算法
     * @param s
     * @return
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}
