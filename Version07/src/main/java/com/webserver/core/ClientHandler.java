package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/*
 * 该任务负责与指定的客户端进行HTTP交互
 */
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 1 解析请求
            HttpServletRequest request = new HttpServletRequest(socket);

            // 2 处理请求
            String path = request.getUri();
            File rootDir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );
            File staticDir = new File(rootDir,"static");
            File file = new File(staticDir,path);

            int statusCode; // 状态代码
            String statusReason; // 状态描述

            if(file.isFile()) { // file表示的是否为一个文件
                statusCode = 200;
                statusReason = "OK";
            }
            else { // file表示的是一个目录或file表示的路径并不存在
                statusCode = 404;
                statusReason = "NotFound";
                file = new File(staticDir,"/root/404.html");
            }

            // 3 发送响应
            // 发送状态行
            println("HTTP/1.1" + " " + statusCode + " " + statusReason);

            // 发送响应头
            println("Content-Type: text/html");
            println("Content-Length: " + file.length());
            println("");

            // 发送响应正文
            FileInputStream fis = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[1024 * 10]; // 10kb
            int len; // 记录每次实际读取到的字节数

            while((len = fis.read(data)) != -1) {
                out.write(data,0,len);
            }

            System.out.println("响应发送完毕!!!!!!!!!!!!!!!!");

        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

}







