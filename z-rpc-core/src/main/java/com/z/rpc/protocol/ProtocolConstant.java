package com.z.rpc.protocol;

/**
 * 协议常量：记录和自定义协议有关的关键信息
 */
public class ProtocolConstant {
    //消息头长度
    public static int MESSAGE_HEADER_LENGTH = 17;
    //协议版本号
    public static byte PROTOCOL_VERSION = 0x1;
    //魔数
    public static byte PROTOCOL_MAGIC = 0x1;
}
