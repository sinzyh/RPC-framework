package com.z.rpc.registry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 */
public class LocalRegistry {

    /**
     * 注册中心存储
     */
     private static final ConcurrentHashMap<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     */
     public static void register(String serviceName, Class<?> serviceClass){
         map.put(serviceName, serviceClass);
     }

    /**
     * 获取服务
     */
     public static Class<?> get(String serviceName){
         return map.get(serviceName);
     }

    /**
     * 删除服务
     */
     public static void remove(String serviceName){
         map.remove(serviceName);
     }


}
