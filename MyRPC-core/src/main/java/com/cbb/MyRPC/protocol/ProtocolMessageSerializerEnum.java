package com.cbb.MyRPC.protocol;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public enum ProtocolMessageSerializerEnum {

    JDK_SERIALIZER(0, "jdk"),
    JSON_SERIALIZER(1, "json"),
    KRYO_SERIALIZER(2, "kryo"),
    HESSIAN_SERIALIZER(3, "hessian");

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final int key;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item-> item.value).collect(Collectors.toList());
    }

    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum protocol : ProtocolMessageSerializerEnum.values()) {
            if (protocol.key == key) {
                return protocol;
            }
        }
        return null;
    }

    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        for (ProtocolMessageSerializerEnum protocol : ProtocolMessageSerializerEnum.values()) {
            if (protocol.value.equals(value)) {
                return protocol;
            }
        }
        return null;
    }
}
