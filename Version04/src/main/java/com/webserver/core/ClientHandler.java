package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            // 解析请求行
            String line = readLine();
            System.out.println("请求行内容: " + line);

            // 请求行相关信息
            String method; // 请求方式
            String uri; // 抽象路径
            String protocol; // 协议版本

            // 将请求行按照空格拆分为三部分，并分别用上述三个变量保存
            String[] data = line.split("\\s");
            // String[] data = line.split(" "); // 直接按照空格拆分也行

            method = data[0];
            uri = data[1]; // 这里可能出现数组下标越界异常:ArrayIndexOutOfBoundsException,
                           // 这是由于浏览器发送了空请求导致的，解决办法: 换一个浏览器请求试试
            protocol = data[2];

            // 测试路径: http://localhost:8088/myweb/index.html
            System.out.println("method: " + method); // method: GET
            System.out.println("uri: " + uri); // uri: /myweb/index.html
            System.out.println("protocol: " + protocol); // protocol: HTTP/1.1

            // 解析消息头
            // 这个Map存所有消息头，key为消息头的名字 value为消息头的值
            Map<String,String> headers = new HashMap<>();

            while(true) {
                line = readLine();
                // 读取消息头时，如果readLine方法返回空字符串，说明单独读取了CRLF
                if(line.isEmpty()) {
                    break;
                }
                // 将消息头按照冒号空格拆分为名字和对应的值，并作为key，value存入headers
                data = line.split(":\\s");
                headers.put(data[0], data[1]);
                System.out.println("消息头: " + line);
            }
            System.out.println("headers: "+ headers);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 被解析请求的逻辑复用的方法，目的:读取一行字符串(以CRLF结尾的)
     *
     * 注：复用代码的方法中如果出现异常，通常直接抛出给调用者解决。
     * @return
     */
    private String readLine() throws IOException {
        /*
            只要socket对象是同一个，无论调用多少次getInputStream获取回来的
            输入对象始终也是同一个。
         */
        InputStream in = socket.getInputStream();

        // 使用StringBuilder用于拼接每一个读取到的字符，并最终组成一个字符串使用
        StringBuilder builder = new StringBuilder();

        int d; // 记录每次读取到的字节
        char cur = 'a'; // 表示本次读取到的字符
        char pre = 'a'; // 表示上次读取到的字符

        while((d = in.read()) != -1) {
            cur = (char)d; // 将本次读取到的字节转换为char记录
            if(pre == 13 && cur == 10) { // 判断上次读取的是否为回车符并且本次读取到的是否为换行符
                // 如果连续读取到了回车+换行符，则停止本行字符串的读取工作
                break;
            }
            builder.append(cur); // 将本次读取的字符拼接到字符串中
            pre = cur; // 在读取下一个字符前，将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();
    }

}















