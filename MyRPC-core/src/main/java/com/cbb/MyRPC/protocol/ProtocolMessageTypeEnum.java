package com.cbb.MyRPC.protocol;

import lombok.Getter;

@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);
    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum protocolMessageType : values()) {
            if (protocolMessageType.key == key) {
                return protocolMessageType;
            }
        }
        return null;
    }
}
