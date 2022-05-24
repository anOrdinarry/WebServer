package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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

            // 解析请求行
            String line = readLine();
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
            uri = data[1]; // 这里可能出现数组下标越界异常: ArrayIndexOutOfBoundsException，这是由于浏览器发送了空请求导致的，解决办法: 换一个浏览器请求试试，最好用chrome浏览器运行
            protocol = data[2];

            // 测试路径: http://localhost:8088/myweb/index.html
            System.out.println("method: " + method); // method: GET
            System.out.println("uri: " + uri); // uri: /myweb/index.html
            System.out.println("protocol: " + protocol); // protocol: HTTP/1.1

            // 解析消息头

            // 这个Map存所有消息头，key为消息头的名字 value为消息头的值
            Map<String, String> headers = new HashMap<>();

            while (true) {

                line = readLine();

                // 读取消息头时，如果 readLine方法 返回空字符串，说明单独读取了CRLF
                // ① line.isEmpty() / ② "".equals(line) / ③ line.length() == 0
                if(line.isEmpty()) { // 读到空字符串，结束循环
                    break;
                }

                // 将消息头按照 冒号(非中文冒号)空格 拆分为名字和对应的值，并作为key，value存入headers
                data = line.split(":\\s");
                headers.put(data[0], data[1]);

                System.out.println("消息头: " + line);

            }

            System.out.println("headers: " + headers);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 被解析请求的逻辑复用的方法，目的: 读取一行字符串(以CRLF结尾的)
     *
     * 注: 复用代码的方法中如果出现异常，通常直接抛出给调用者解决
     *
     */
    private String readLine() throws IOException {

        /*
           测试路径:
           http://localhost:8088

           切记! 不要用 https://localhost:8088
           否则读取的内容是乱码!
        */

        /*
           只要socket对象是同一个，无论调用多少次 getInputStream
           获取回来的输入对象始终也是同一个
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

        return builder.toString().trim();

    }

}

























