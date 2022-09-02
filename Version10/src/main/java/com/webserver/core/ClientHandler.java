package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

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
            HttpServletResponse response = new HttpServletResponse(socket);

            // 2 处理请求
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(request,response);

            // 3 发送响应
            response.response();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                // 一问一答后断开连接。HTTP协议要求。
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}













