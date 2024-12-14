package com.cbb.MyRPC.springboot.starter.bootstrap;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.server.tcp.VertxTcpServer;
import com.cbb.MyRPC.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取注解属性
        boolean needService = (boolean) importingClassMetadata
                .getAnnotationAttributes(EnableRpc.class.getName()).get("needService");

        // 框架初始化
        RpcApplication.init();

        // 获取全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 根据属性决定是否启动服务器
        if (needService) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else {
            log.info("不需要启动服务器");
        }
    }
}
