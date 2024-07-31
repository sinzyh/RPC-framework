package com.z.rpc.serializer;

import com.z.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂，工厂模式+单例模式
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

//    private static final Map<String ,Serializer> KEY_SERIALIZER_MAP = new HashMap<String ,Serializer>(){
//        {
//            put(SerializerKeys.JDK, new JdkSerializer());
//            put(SerializerKeys.HESSIAN, new HessianSerializer());
//
//        }
//    };

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
