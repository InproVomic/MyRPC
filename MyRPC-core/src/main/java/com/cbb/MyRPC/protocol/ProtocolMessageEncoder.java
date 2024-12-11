package com.cbb.MyRPC.protocol;

import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

public class ProtocolMessageEncoder {

    public static Buffer encode(ProtocolMessage<?> message) {
        if(message == null || message.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = message.getHeader();
        // 向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null) {
            throw new RuntimeException("协议序列化器不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        try {
            byte[] bytes = serializer.serialize(message.getBody());
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } catch (Exception e) {
            throw new RuntimeException("编码失败",e);
        }
        return buffer;
    }
}
