package com.cbb.MyRPC.protocol;

import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws Exception {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(buffer.getByte(0));
        if(header.getMagic() != ProtocolConstants.PROTOCOL_MAGIC){
            throw new RuntimeException("magic number is not correct");
        }
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        // 解决粘包问题
        byte[] body = buffer.getBytes(17, 17 + header.getBodyLength());

        // 根据序列化协议，反序列化消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("序列化消息的协议不存在");
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());

        if(messageTypeEnum == null){
            throw new RuntimeException("消息体的类型不存在");
        }

        switch (messageTypeEnum){
            case REQUEST:
                RpcRequest request = serializer.deserialize(body, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(body, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHER:
            default:
                throw new RuntimeException("消息体的类型不存在");
        }
    }
}
