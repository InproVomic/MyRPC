package com.cbb.MyRPC.springboot.starter.bootstrap;

import com.cbb.MyRPC.proxy.ServiceProxy;
import com.cbb.MyRPC.proxy.ServiceProxyFactory;
import com.cbb.MyRPC.springboot.starter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

// 服务消费者启动类
@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        // 遍历bean对象的所有属性
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if(rpcReference != null) {
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if(interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                Object proxy = ServiceProxyFactory.getProxy(interfaceClass);
                // 设置属性可以注入
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入对象失败" + e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
