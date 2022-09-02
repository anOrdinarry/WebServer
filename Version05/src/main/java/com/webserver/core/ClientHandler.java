package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.net.Socket;

/*
 * 该任务负责与指定的客户端进行HTTP交互
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1 解析请求
            HttpServletRequest request = new HttpServletRequest(socket);

            // 2 处理请求


            // 3 发送响应

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}














