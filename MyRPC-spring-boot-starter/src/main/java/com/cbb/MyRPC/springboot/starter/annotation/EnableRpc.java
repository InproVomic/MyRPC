package com.cbb.MyRPC.springboot.starter.annotation;

import com.cbb.MyRPC.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.cbb.MyRPC.springboot.starter.bootstrap.RpcInitBootstrap;
import com.cbb.MyRPC.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 启用Rpc注解
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcProviderBootstrap.class, RpcConsumerBootstrap.class, RpcInitBootstrap.class})
public @interface EnableRpc {
    // 是否需要启动服务器
    boolean needService() default true;
}
