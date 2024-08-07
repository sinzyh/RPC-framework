package com.z.rpc.loadbalancer;

import com.z.rpc.registry.EtcdRegistry;
import com.z.rpc.registry.Registry;
import com.z.rpc.spi.SpiLoader;

/**
 * 注册中心工厂，工厂模式+单例模式
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }


    private static final RoundRobinLoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
