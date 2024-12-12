package com.cbb.MyRPC.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import com.cbb.MyRPC.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class VertxTcpClient {
    public static RpcResponse doRequest(RpcRequest request, ServiceMetaInfo selectedServiceMetaInfo) throws ExecutionException, InterruptedException {
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        client.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                result -> {
                    if(result.succeeded()){
                        // 得到socket
                        NetSocket socket = result.result();
                        //构造协议消息
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstants.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstants.PROTOCOL_VERSION);
                        header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(request);

                        // 编码请求
                        try {
                            Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encodeBuffer);
                        }catch (Exception e){
                            throw new RuntimeException("协议编码错误");
                        }
                        // 处理响应
                        System.out.println(socket.remoteAddress().host());
                        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                            try {
                                // 得到响应
                                ProtocolMessage<RpcResponse> protocolResponse = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseFuture.complete(protocolResponse.getBody());
                            }catch (Exception e){
                                throw new RuntimeException("协议解码错误");
                            }
                        });
                        socket.handler(tcpBufferHandlerWrapper);

                    }else{
                        System.out.println("TCP连接失败");
                        responseFuture.completeExceptionally(new RuntimeException("TCP连接失败"));
                    }
                });

        RpcResponse response = responseFuture.get();
        // 关闭连接
        client.close();
        return response;
    }
}
