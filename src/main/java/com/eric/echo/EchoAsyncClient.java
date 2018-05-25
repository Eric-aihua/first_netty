package com.eric.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class EchoAsyncClient {
    private int port;
    private String ip;

    public EchoAsyncClient(String ip, int port) {
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
            // 使用异步的方式连接Server,不管成功失败，都是执行下面System.out的语句，最后的连接结果由FutureListener进行处理
            ChannelFuture future = bootstrap.connect();
            System.out.println("Finished connect operation");
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()){
                    ByteBuf buffer = Unpooled.copiedBuffer(
                            "Hello", Charset.defaultCharset());
                    ChannelFuture wf = future1.channel()
                            .writeAndFlush(buffer);
                    System.out.println("Connect successful!");
                }else{
                    System.out.println("Connect failed!");
                    Throwable cause = future1.cause();
                    cause.printStackTrace();
                }
            });
            System.out.println("Finished connect operation2");
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
        new EchoAsyncClient("localhost",8888).start();
    }

}
