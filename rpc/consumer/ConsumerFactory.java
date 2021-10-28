package myrpc.rpc.consumer;

import io.netty.channel.socket.SocketChannel;
import myrpc.rpc.exception.MyException;

import java.util.HashMap;

public class ConsumerFactory {

    //创建客户端
    static HashMap<String, String> interfaceAddressMap = new HashMap<>();
    //代理找到通道
    static HashMap<String, SocketChannel> addressAndChannelMap = new HashMap<>();
    //通道找到结果集
    static HashMap<SocketChannel, String> channelAndResultMap = new HashMap<>();

    public ConsumerFactory() {
        //读取配置文件填充interfaceAddressMap
        //读取根据interfaceAddressMap创建Client，并执行run方法
        interfaceAddressMap.put("myrpc.consumer.Inter", "localhost:8080");
        Client client = new Client("localhost", 8080);
        //初始化会可能创建大量线程
        new Thread(() -> {
            try {
                client.run();
            } catch (MyException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
