package com.cbb.MyRPC.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import com.cbb.MyRPC.registry.Registry;
import com.cbb.MyRPC.registry.RegistryFactory;
import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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

        try {
            //序列化请求
            byte[] bytes = serializer.serialize(request);

            // 得到注册中心，并从中获取服务信息
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getDefaultRegistry();
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(request.getServiceName());
            serviceMetaInfo.setVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("未找到服务");
            }

            // 从服务信息中获取地址
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);

            //发送请求
            try (HttpResponse response = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bytes).execute()) {
                //得到响应
                byte[] body = response.bodyBytes();
                //反序列化响应
                RpcResponse rpcResponse = serializer.deserialize(body, RpcResponse.class);
                //返回响应结果
                return rpcResponse.getData();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
