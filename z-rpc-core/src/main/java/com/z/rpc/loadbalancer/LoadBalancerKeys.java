package com.z.rpc.loadbalancer;

/**
 * 负载均衡器的常量名
 */
public interface LoadBalancerKeys {

     String ROUND_ROBIN = "roundRobin";
     String RANDOM = "random";
     String CONSISTENT_HASH = "consistentHash";

}
