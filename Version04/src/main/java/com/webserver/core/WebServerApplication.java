package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * WebServer是模拟Tomcat的一个Web容器
 * Web容器主要功能:
 * 1:管理部署在这里的多个网络应用(webapp)
 *   webapp:网络应用，就是俗称的一个"网站"的所有内容。通常包括网页，素材，java代码等
 * 2:和客户端(通常就是浏览器)进行TCP链接，并基于HTTP协议进行交互。使得客户端可以
 *   访问网络应用中的内容。
 */
public class WebServerApplication {

    private ServerSocket serverSocket;

    public WebServerApplication(){
        try {
            System.out.println("正在启动服务端...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            System.out.println("等待客户端链接...");
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端链接了!");

            // 启动一个线程处理该客户端交互
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);
            t.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServerApplication application = new WebServerApplication();
        application.start();
    }

}





