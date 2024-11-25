package com.cbb.MyRPC.service;

import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.registry.LocalRegistry;
import com.cbb.MyRPC.serializer.JdkSerializer;
import com.cbb.MyRPC.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        // 序列化器
        final Serializer serializer = new JdkSerializer();

        // 记录日志
        System.out.println("request method:" + httpServerRequest.method() + "url:" + httpServerRequest.uri());

        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                // 反序列化
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 业务处理
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("请求参数有误");
                doResponse(httpServerRequest, rpcResponse, serializer);
                return;
            }

            // 调用方法
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object invoke = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(invoke);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("调用成功");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            doResponse(httpServerRequest, rpcResponse, serializer);
        });
    }

    void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse,Serializer serializer){
        HttpServerResponse httpServerResponse = httpServerRequest.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
