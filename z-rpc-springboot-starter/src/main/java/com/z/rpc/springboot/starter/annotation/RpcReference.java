package com.z.rpc.springboot.starter.annotation;

import com.z.rpc.config.RpcConfig;
import com.z.rpc.constant.RpcConstant;
import com.z.rpc.loadbalancer.LoadBalancerKeys;


public @interface RpcReference {
    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 模拟调用
     */
    boolean mock() default false;
}
