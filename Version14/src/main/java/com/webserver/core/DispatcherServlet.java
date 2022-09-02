package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/*
 * 处理请求的类
 */
public class DispatcherServlet {

    private static File rootDir;

    private static File staticDir;

    static {
        try {
            rootDir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            staticDir = new File(rootDir,"static");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {

        String path = request.getUri();

        /*
            http://localhost:8088/myweb/index.html
            path:/myweb/index.html
            总是去static目录下顺着path指定的路径定位文件的
         */
        File file = new File(staticDir,path);

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

        response.addHeader("Server","WebServer");
    }

}












