package com.cbb.MyRPC.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("method invoke:{}", method.getName());
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> returnType) {
        //首先判断是否是基本数据类型
        if(returnType.isPrimitive()) {
            if(returnType == boolean.class){
                return false;
            }else if(returnType == int.class){
                return 0;
            }else if(returnType == long.class){
                return 0L;
            }else if(returnType == float.class){
                return 0F;
            }else if(returnType == double.class){
                return 0D;
            }else if(returnType == byte.class){
                return (byte)0;
            }else if(returnType == short.class){
                return (short)0;
            }
        }
        // 对象类型就返回null
        return null;
    }
}
