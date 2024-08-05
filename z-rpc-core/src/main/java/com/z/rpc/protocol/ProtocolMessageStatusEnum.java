package com.z.rpc.protocol;

import lombok.Getter;

/**
 * 协议消息的状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20),
    BAD_REQUEST("bad request",40),
    BAD_RESPONSE("bad response",50);

    private final String text;

    private final int code;
    ProtocolMessageStatusEnum(String text, int code) {
        this.text=text;
        this.code=code;
    }

    /**
     * 根据code获取枚举
     */
    public static ProtocolMessageStatusEnum getEnumByCode(int code) {
        for (ProtocolMessageStatusEnum protocolMessageStatusEnum : ProtocolMessageStatusEnum.values()) {
            if (protocolMessageStatusEnum.getCode() == code) {
                return protocolMessageStatusEnum;
            }
        }
        return null;
    }
}
