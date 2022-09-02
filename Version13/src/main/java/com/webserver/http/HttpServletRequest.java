package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * 请求对象
 * 该类的每一个实例用于表示HTTP协议规定的请求，即:客户端给服务端发送的内容
 * 一个请求由三部分构成:
 * 请求行，消息头，消息正文
 */
public class HttpServletRequest {

    private Socket socket;

    // 请求行相关信息
    private String method; // 请求方式
    private String uri; // 抽象路径
    private String protocol; // 协议版本

    // 消息头相关信息
    // 这个Map存所有消息头，key为消息头的名字 value为消息头的值
    private Map<String,String> headers = new HashMap<>();

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;

        // 解析请求行
        parseRequestLine();

        // 解析消息头
        parseHeaders();

        // 解析消息正文
        parseContent();
    }

    /**
     * 解析请求行
     */
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();
        if(line.isEmpty()) { // 如果请求行没有内容，则说明本次为空请求
            throw new EmptyRequestException();
        }
        System.out.println("请求行内容: " + line);

        // 将请求行按照空格拆分为三部分，并分别用上述三个变量保存
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];

        System.out.println("method: " + method);
        System.out.println("uri: " + uri);
        System.out.println("protocol: " + protocol);
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
        while(true) {
            String line = readLine();
            if(line.isEmpty()) {
                break;
            }
            String[] data = line.split(":\\s");
            headers.put(data[0], data[1]);
            System.out.println("消息头: " + line);
        }
        System.out.println("headers: " + headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent() {

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
            if(pre == 13 && cur == 10){ // 判断上次读取的是否为回车符并且本次读取到的是否为换行符
                // 如果连续读取到了回车+换行符，则停止本行字符串的读取工作
                break;
            }
            builder.append(cur); // 将本次读取的字符拼接到字符串中
            pre = cur; // 在读取下一个字符前，将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

}












