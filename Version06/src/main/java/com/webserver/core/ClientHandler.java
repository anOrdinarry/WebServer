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

            // 2 处理请求
            String path = request.getUri();

            // 3 发送响应
            /*
                定义为resource目录(maven项目中src/main/java和src/main/resources实际上是一个目录)
                只不过java目录中存放的都是.java的源代码文件
                而resources目录下存放的就是非.java的其他程序中需要用到的资源文件

                实际开发中，我们在定位目录时，常使用相对路径，而实际应用的相对路径都是类加载路径
                类加载路径可以用:
                类名.class.getClassLoader().getResource(".")就是类加载路径
                这里可以理解为时src/main/java目录或src/main/resources
                实际表达的是编译后这两个目录最终合并的target/classes目录。
             */

            // 这里相当于定位的是当前项目下的resources目录
            File rootDir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );

            /*
                定位resources下的static目录
                注: resources下的static目录是sprint boot项目中用于存放所有静态资源
                   的目录，相当于我们写的"网站"中用到的页面，图片等资源都放在static下
             */
            File staticDir = new File(rootDir,"static");

            // 定位页面: resources/static/myweb/index.html
            // File file = new File(staticDir, "/myweb/index.html");

            File file = new File(staticDir,path);
            /*
                http://localhost:8088/myweb/index.html
                http://localhost:8088/myweb/classTable.html

                http://localhost:8088/
                http://localhost:8088/myweb/
                http://localhost:8088/myweb/123.html

                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */

            // 发送状态行
            println("HTTP/1.1 200 OK");

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
                out.write(data,0, len);
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


















