package com.cbb.MyRPC.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import com.cbb.MyRPC.protocol.*;
import com.cbb.MyRPC.registry.Registry;
import com.cbb.MyRPC.registry.RegistryFactory;
import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;
import com.cbb.MyRPC.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 得到序列化器
        final Serializer serializer = SerializerFactory.getDefaultSerializer();

        //构造请求
        RpcRequest request = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args).build();

        try {
            // 得到注册中心，并从中获取服务信息
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getDefaultRegistry();
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
            serviceMetaInfo.setVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("未找到服务");
            }

            // 从服务信息中获取地址
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);
            RpcResponse response = VertxTcpClient.doRequest(request, selectedServiceMetaInfo);
            return response.getData();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
