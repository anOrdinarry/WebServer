package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChrStart
 * @create 2022-05-23 18:58
 */

/*
 * 该任务负责与指定的客户端进行HTTP交互
 *
 */

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            // 1:解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            // 2:处理请求
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(request, response);

            // 3:发送响应
            response.response();

            System.out.println("响应发送完毕！！");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

























