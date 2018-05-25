package com.eric.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用Java net使用数据请求处理，此时为单线程模型，且只能处理一个客户端的请求
 *
 */
public class JavaNetBIOServer {
    public static void main(String args[]) {
        int portNumber = 8000;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            while (true) {
//                System.out.println("Wait request...............");
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                String request, response;
                while ((request = in.readLine()) != null) {
                    if ("Done".equals(request)) {
                        break;
                    }
                    response = processRequest(request);
                    out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request) {
//        System.out.println("Received Request:" + request);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return request.toUpperCase();
    }
}
