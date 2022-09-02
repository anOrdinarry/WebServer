package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

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
            String path = request.getUri();
            File rootDir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );
            File staticDir = new File(rootDir,"static");
            File file = new File(staticDir, path);

            if(file.isFile()) { // file表示的是否为一个文件
                response.setContentFile(file);
            }
            else {
                // file表示的是一个目录或file表示的路径并不存在
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir,"/root/404.html");
                response.setContentFile(file);
            }

            // 3 发送响应
            response.response();

            System.out.println("响应发送完毕!!!!!!!!!!!!!!!!");

        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

}













