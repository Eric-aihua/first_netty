package com.eric.transfer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 通过OIO的方式实现给客户端恢复hi的功能
 * 这段代码完全可以处理中等数量的并发客户端。但是随着应用程序变得流行起来，你会发现
 * 它并不能很好地伸缩到支撑成千上万的并发连入连接。
 */

public class OIOHIServer {
    public void serve(int port) throws IOException {
        System.out.println("Hi Server listener on 8080 port");
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (;;) {
                final Socket clientSocket = socket.accept();
//                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {
                            try {
                                clientSocket.close();
                            }
                            catch (IOException ex) {
// ignore on close
                            }
                        }
                    }
                }).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        try {
            new OIOHIServer().serve(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
