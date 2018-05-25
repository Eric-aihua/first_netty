package com.eric.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 最常用的 ByteBuf 模式是将数据存储在 JVM 的堆空间中。这种模式被称为支撑数组
 * （backing array），它能在没有使用池化的情况下提供快速的分配和释放
 */
public class DirectBuf {
    public static void main(String args[]) {
        ByteBuf directBuf = Unpooled.directBuffer(100);
        directBuf.writeBytes("direct buffer".getBytes());
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            System.out.println(Arrays.toString(array));
            System.out.println(length);
        }
    }
}
