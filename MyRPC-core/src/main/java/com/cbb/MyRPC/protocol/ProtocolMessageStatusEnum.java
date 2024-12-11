package com.cbb.MyRPC.protocol;

import lombok.Getter;

/**
 * 协议状态枚举类
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("OK",20),
    BAD_REQUEST("badRequest",40),
    BAD_REQUEST_UNAUTHORIZED("badResponse",50);

    private final String text;

    private final int code;

    ProtocolMessageStatusEnum(String text, int code) {
        this.text = text;
        this.code = code;
    }

    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum status : ProtocolMessageStatusEnum.values()) {
            if (status.getCode() == value) {
                return status;
            }
        }
        return null;
    }
}
