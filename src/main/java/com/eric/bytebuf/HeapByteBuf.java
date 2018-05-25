package com.eric.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.Arrays;

/**
 * 最常用的 ByteBuf 模式是将数据存储在 JVM 的堆空间中。这种模式被称为支撑数组
 * （backing array），它能在没有使用池化的情况下提供快速的分配和释放
 */
public class HeapByteBuf {
    public static void main(String args[]) {
        ByteBuf heapBuf = Unpooled.copiedBuffer("heap space",
                CharsetUtil.UTF_8);
        if (heapBuf.hasArray()) { //检查 ByteBuf 是否有一个支撑数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            System.out.println(Arrays.toString(array));
            System.out.println(offset);
            System.out.println(length);
        } else {
            System.out.println("No Heap Array");
        }
    }
}
