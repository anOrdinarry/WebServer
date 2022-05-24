package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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

            /*
                测试路径:
                http://localhost:8088

                切记! 不要用 https://localhost:8088
                否则读取的内容是乱码!
             */

            InputStream in = socket.getInputStream();

            /*
                测试读取一行字符串

                思路:
                连续读取若干字符，当连续读取到了回车符和换行符就停止。
                并将之前连续读取的字符组成一个字符串。
             */

            // 使用 StringBuilder 用于拼接每一个读取到的字符，并最终组成一个字符串使用
            StringBuilder builder = new StringBuilder();


            int d; // 记录每次读取到的字节
            char cur = 'a'; // 表示本次读取到的字符
            char pre = 'a'; // 表示上次读取到的字符

            while((d = in.read()) != -1) {

                cur = (char) d; // 将本次读取到的字节转换为char记录

                if(pre == 13 && cur == 10) { // 判断 上次读取的是否为 回车符(pre == 13)
                                             // 并且 本次读取到的是否为 换行符(cur == 10)  ! ! ! !

                    // 如果连续读取到了 回车 + 换行符，则停止本行字符串的读取工作
                    break;

                }

                builder.append(cur); // 将本次读取的字符拼接到字符串中

                pre = cur; // 在读取下一个字符前，将本次读取的字符记作上次读取的字符

            }

            // String line = builder.toString() ? ? ? ?

            String line = builder.toString().trim();
            System.out.println("请求行内容: " + line);

            // 请求行相关信息
            String method; // 请求方式
            String uri; // 抽象路径
            String protocol; // 协议版本

            // 将请求行按照空格拆分为三部分，并分别用上述三个变量保存
            String[] data = line.split("\\s"); // java中"\s"为空格的意思，是转义字符，
                                                     // 但JVM语法: 在正则表达式中要表达转义字符要多加一个"\"，所以为"\\s"

//          String[] data = line.split(" "); // 直接按照空格拆分也行

            method = data[0];
            uri = data[1];
            protocol = data[2];

            // 测试路径: http://localhost:8088/myweb/index.html
            System.out.println("method: " + method); // method: GET
            System.out.println("uri: " + uri); // uri: /myweb/index.html
            System.out.println("protocol: " + protocol); // protocol: HTTP/1.1

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

























