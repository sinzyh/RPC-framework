package com.z.rpc.protocol;

import com.z.rpc.model.RpcRequest;
import com.z.rpc.model.RpcResponse;
import com.z.rpc.serializer.Serializer;
import com.z.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

public class ProtocolMessageDecoder {

    /**
     * 消息解码
     *
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        //从指定位置读取Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        //校验魔数
        if(magic!=ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("魔数不匹配，消息非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setMessageType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setLength(buffer.getInt(13));

        //解决粘包问题，只读取指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getLength());
        //解析消息体    获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByCode(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化协议消息不存在");
        }

        Serializer serializer = SerializerFactory.getSerializer(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByCode(header.getMessageType());

        switch (messageTypeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            case HEART_BEAT:
            case OTHER:
            default:
                throw new RuntimeException("暂不支持该消息");
        }
    }
}
