package com.z.rpc.protocol;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {
    //消息头
    private Header header;
    //消息体 请求或相应内容
    private T body;

    /**
     * 协议消息头
     */
    @Data
    //一共17字节
    public static class Header{
        //魔数，保障数据安全
        private byte magic;
        //版本号
        private byte version;
        //序列化器
        private byte serializer;
        //消息类型
        private byte messageType;
        //状态
        private byte status;
        //请求id
        private long requestId;
        //消息长度
        private int length;
    }
}
