package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * 响应对象，该类的每一个实例用于表示服务端给客户端发送的一个响应
 * HTTP协议要求一个响应的格式由三部分构成:
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {

    private Socket socket;

    // 状态行相关信息
    private int statusCode = 200;
    private String statusReason = "OK";

    // 响应头相关信息
    private Map<String,String> headers = new HashMap<>();

    // 响应正文相关信息
    private File contentFile; // 正文文件

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }

    /**
     *  将当前响应对象内容以标准的响应格式发送给客户端
     */
    public void response() throws IOException {
        // 发送状态行
        sendStatusLine();

        // 发送响应头
        sendHeaders();

        // 发送响应正文
        sendContent();
    }

    // 发送状态行
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }

    // 发送响应头
    private void sendHeaders() throws IOException {
        /*
            headers这个Map里在发送时应当已经存好了所有要发送的响应头
            key                     value
            Content-Type            text/html
            Content-Length          124
            ...                     ...
         */
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();

        for(Map.Entry<String,String> e : entrySet) {
            String name = e.getKey(); // Content-Type
            String value = e.getValue(); // text/html
            println(name + ": " + value);
        }

        println(""); // 单独发送回车+换行
    }

    // 发送响应正文
    private void sendContent() throws IOException {
        try(
            FileInputStream fis = new FileInputStream(contentFile);
        )
        {
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[1024 * 10]; // 10kb
            int len; // 记录每次实际读取到的字节数
            while ((len = fis.read(data)) != -1) {
                out.write(data, 0, len);
            }
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }

    public void addHeader(String name,String value){
        this.headers.put(name,value);
    }

}















