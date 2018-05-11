package com.eric.transfer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 用netty nio的API重新实现hi server,并增加ChannelFuture 监听功能
 */

public class NIOChannelFutureListenerServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("GBK")));
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(
                                                ChannelHandlerContext ctx)
                                                throws Exception {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener((future) -> {
                                                        // add listener for ChannelFuture
                                                                if (future.isSuccess()) {
                                                                    System.out.println("Write Successful!");
                                                                } else {
                                                                    System.out.println("Write Failed:");
                                                                    future.cause().printStackTrace();
                                                                }
                                                            }
                                                    );
                                        }
                                    });
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String args[]) {
        try {
            new NIOChannelFutureListenerServer().server(8084);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
