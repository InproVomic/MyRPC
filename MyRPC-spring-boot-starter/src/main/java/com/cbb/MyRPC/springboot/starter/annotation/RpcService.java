package com.cbb.MyRPC.springboot.starter.annotation;

import com.cbb.MyRPC.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 服务提供者注解
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    // 服务接口类
    Class<?> interfaceClass() default void.class;

    // 版本
    String version() default RpcConstant.DEFAULT_SERVICE_VERSION;
}
