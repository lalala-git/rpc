package myrpc.rpc.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("【log】接收到消息" + s);

        SocketChannel socketChannel = (SocketChannel) channelHandlerContext.channel();
        ConsumerFactory.channelAndResultMap.put(socketChannel, s);

        synchronized (socketChannel) {
            socketChannel.notify();
        }
    }

}
