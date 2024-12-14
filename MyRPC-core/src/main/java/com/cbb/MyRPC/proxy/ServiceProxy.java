package com.cbb.MyRPC.proxy;

import cn.hutool.core.collection.CollUtil;
import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.fault.retry.RetryStrategy;
import com.cbb.MyRPC.fault.retry.RetryStrategyFactory;
import com.cbb.MyRPC.fault.tolerant.TolerantStrategy;
import com.cbb.MyRPC.fault.tolerant.TolerantStrategyFactory;
import com.cbb.MyRPC.loadbalancer.LoadBalancer;
import com.cbb.MyRPC.loadbalancer.LoadBalancerFactory;
import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import com.cbb.MyRPC.registry.Registry;
import com.cbb.MyRPC.registry.RegistryFactory;
import com.cbb.MyRPC.serializer.Serializer;
import com.cbb.MyRPC.serializer.SerializerFactory;
import com.cbb.MyRPC.server.tcp.VertxTcpClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // 获取负载均衡器
            LoadBalancer loadBalancer = LoadBalancerFactory.getDefaultLoadbalancer();
            // 将方法名作为作为负载均衡参数
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put(method.getName(), method.getName());

            // 从服务信息中获取地址
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfos);
            RpcResponse response = null;
            try {
                // 使用重试机制
                RetryStrategy retryStrategy = RetryStrategyFactory.getDefaultRetryStrategy();
                response = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(request, selectedServiceMetaInfo));
            }catch (Exception e) {
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getDefaultTolerantStrategy();
                response = tolerantStrategy.doTolerant(requestParams, e);
            }
            return response.getData();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
