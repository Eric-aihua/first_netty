package com.eric.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.Arrays;


/**
 * Netty 通过一个 ByteBuf 子类——CompositeByteBuf——实现了组合模式，它提供了一
 个将多个缓冲区表示为单个合并缓冲区的虚拟表示
 */
public class CompositeBuf {
    public static void main(String args[]){
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.copiedBuffer("head", CharsetUtil.UTF_8); // can be backing or direct
        ByteBuf bodyBuf = Unpooled.copiedBuffer("body", CharsetUtil.UTF_8); // can be backing or direct
        messageBuf.addComponents(headerBuf, bodyBuf);
        System.out.println("Remove Head Before------------------");
        printCompositeBuffer(messageBuf);
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
        messageBuf.removeComponent(0); // remove the header
        System.out.println("Remove Head After------------------");
        printCompositeBuffer(messageBuf);
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
    }

    public static void printCompositeBuffer(CompositeByteBuf compBuf){
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        System.out.println (Arrays.toString(array));
        System.out.println (length);
    }
}
