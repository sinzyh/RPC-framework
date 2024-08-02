package com.z.rpc.registry;

import com.z.rpc.serializer.JdkSerializer;
import com.z.rpc.serializer.Serializer;
import com.z.rpc.spi.SpiLoader;

/**
 * 注册中心工厂，工厂模式+单例模式
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }


    private static final EtcdRegistry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
