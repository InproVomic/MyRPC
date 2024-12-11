package com.cbb.MyRPC.protocol;

public interface ProtocolConstants {
    // 消息头长度
    int MESSAGE_HEADER_LENGTH = 17;

    // 协议魔数
    byte PROTOCOL_MAGIC = 0X1;

    // 协议版本
    byte PROTOCOL_VERSION = 0X1;
}
