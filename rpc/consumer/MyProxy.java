package myrpc.rpc.consumer;

import java.lang.reflect.Proxy;

public class MyProxy {

    public static <T> T newInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new ProxyHandler(clazz));
    }
}
