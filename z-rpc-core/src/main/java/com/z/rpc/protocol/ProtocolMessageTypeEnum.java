package com.z.rpc.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);

    private final int code;

    ProtocolMessageTypeEnum(int code) {
        this.code = code;
    }

    /**
     * 根据code获取枚举
     */
    public static ProtocolMessageTypeEnum getEnumByCode(int code) {
        for (ProtocolMessageTypeEnum protocolMessageTypeEnum : ProtocolMessageTypeEnum.values()) {
            if (protocolMessageTypeEnum.getCode() == code) {
                return protocolMessageTypeEnum;
            }
        }
        return null;
    }
}
