package com.cbb.MyRPC.server.tcp;

import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.protocol.ProtocolMessage;
import com.cbb.MyRPC.protocol.ProtocolMessageDecoder;
import com.cbb.MyRPC.protocol.ProtocolMessageEncoder;
import com.cbb.MyRPC.protocol.ProtocolMessageTypeEnum;
import com.cbb.MyRPC.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.lang.reflect.Method;

public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                ProtocolMessage<RpcRequest> protocolMessage;
                try {
                    protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                RpcRequest rpcRequest = protocolMessage.getBody();

                RpcResponse response = new RpcResponse();

                try {
                    // 反射调用
                    Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                    Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                    Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                    // 封装返回结果
                    response.setData(result);
                    response.setDataType(method.getReturnType());
                    response.setMessage("success");
                } catch (Exception e) {
                    response.setMessage(e.getMessage());
                    response.setException(e.getMessage().toString());
                    e.printStackTrace();
                }
                ProtocolMessage.Header header = protocolMessage.getHeader();
                header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
                ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, response);

                try {
                    Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                    netSocket.write(encode);
                } catch (Exception e) {
                    throw new RuntimeException("协议消息编码错误");
                }
            });
        netSocket.handler(bufferHandlerWrapper);

    }
}
