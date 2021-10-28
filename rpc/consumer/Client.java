package myrpc.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import myrpc.rpc.exception.MyException;

public class Client {

    private final String ip;

    private final Integer port;

    public Client(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() throws MyException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });

            System.out.println("【log】客户端启动成功");

            ChannelFuture cf = bootstrap.connect(ip, port).sync();
            ConsumerFactory.addressAndChannelMap.put(ip + ":" + port, (SocketChannel) cf.channel());
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new MyException("【error】客户端启动失败");
        } finally {
            System.out.println("【log】客户端关闭");
            group.shutdownGracefully();
        }
    }
}
