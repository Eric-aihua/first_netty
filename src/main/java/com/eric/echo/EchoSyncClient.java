package com.eric.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoSyncClient {
    private int port;
    private String ip;

    public EchoSyncClient(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void start() {
        final EchoClientHandler clientHandler = new EchoClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(this.ip,this.port)).
                    handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(clientHandler);
                }
            });
            // 使用同步的方式连接Server,直到连接成功才会执行后面的代码，如果失败将直接跳到catch语句块
            ChannelFuture future = bootstrap.connect().sync();
            System.out.println("Finished Connect");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        new EchoSyncClient("localhost",8888).start();
    }

}
