package com.z.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

/**
 * mock服务代理(JDK动态代理)
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法的返回值类型，生成特定的默认值对象
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());

        return getDefault(returnType);
    }

    /**
     * 生成指定类型的默认对象
     * @param returnType
     * @return
     */
    private Object getDefault(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == boolean.class) {
                return false;
            } else if (returnType == char.class) {
                return '\u0000';
            } else if (returnType == byte.class) {
                return (byte) 0;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == float.class) {
                return 0;
            }
        }
        return null;
    }
}
