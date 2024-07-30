package com.z.rpc.serializer;

import java.io.IOException;

/**
 * 序列化接口
 */
public interface Serializer {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj) throws IOException;


    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> type) throws IOException;
}
