package com.z.rpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "KRYO"),
    HESSIAN(3, "hessian");



    private final int code;
    private final String value;

    ProtocolMessageSerializerEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(e -> e.value).collect(Collectors.toList());
    }

    /**
     * 根据code获得枚举
     */
    public static ProtocolMessageSerializerEnum getByCode(int code) {
        for (ProtocolMessageSerializerEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }


}
