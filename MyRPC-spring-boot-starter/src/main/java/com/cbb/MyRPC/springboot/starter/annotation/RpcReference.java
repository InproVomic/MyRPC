package com.cbb.MyRPC.springboot.starter.annotation;

import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.fault.retry.RetryStrategyKeys;
import com.cbb.MyRPC.fault.tolerant.TolerantStrategyKeys;
import com.cbb.MyRPC.loadbalancer.LoadBalancerKeys;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// 消费者注解
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {
    // 服务接口类
    Class<?> interfaceClass() default void.class;

    // 版本
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    // 负载均衡策略
    String loadBalance() default LoadBalancerKeys.ROUND_ROBIN;

    // 重试策略
    String retryStrategy() default RetryStrategyKeys.FIXED_INTERVAL;

    // 容错策略
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_SAFE;

    // 模拟调用
    boolean mock() default false;
}
