package com.cbb.MyRPC.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.serializer.JdkSerializer;
import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 得到序列化器
        Serializer serializer = SerializerFactory.getDefaultSerializer();

        //构造请求
        RpcRequest request = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args).build();

        try{
            //序列化请求
            byte[] bytes = serializer.serialize(request);
            //发送请求
            try(HttpResponse response = HttpRequest.post("http://localhost:8091").body(bytes).execute()){
                //得到响应
                byte[] body = response.bodyBytes();
                //反序列化响应
                RpcResponse rpcResponse = serializer.deserialize(body, RpcResponse.class);
                //返回响应结果
                return rpcResponse.getData();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
