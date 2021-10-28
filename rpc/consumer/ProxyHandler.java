package myrpc.rpc.consumer;

import io.netty.channel.socket.SocketChannel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {

    private final Class clazz;

    public ProxyHandler (Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取方法名和类名
        String className = clazz.getName();
        String methodName = method.getName();
        String content = className + "#" + methodName;

        //获取工厂中的通道发送消息
        SocketChannel channel = null;
        String address = ConsumerFactory.interfaceAddressMap.get(className);
        while(ConsumerFactory.addressAndChannelMap.get(address) == null) {
            Thread.sleep(100);
            channel = ConsumerFactory.addressAndChannelMap.get(address);
        }

        channel.writeAndFlush(content);
        synchronized (channel) {
            channel.wait();
        }

        return ConsumerFactory.channelAndResultMap.get(channel);
    }
}
