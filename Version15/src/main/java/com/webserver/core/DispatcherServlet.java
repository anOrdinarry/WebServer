package com.webserver.core;

import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;

/*
 * 处理请求的类
 */
public class DispatcherServlet {

    private static File rootDir;

    public static File staticDir;

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
        /*
            /myweb/index.html
            /myweb/reg?username=fanchuanqi&password=123456&nickname=chuanqi&age=22
         */
//        String path = request.getUri(); // 由于抽象路径可能含有参数，且参数值不是固定的(受用户输入信息影响)。因此不能再用它判断请求的行为了
        String path = request.getRequestURI();

        // 首先根据请求路径判断是否为请求某个业务处理
        /*
            由于注册页面reg.html上的form表单里action="/myweb/reg"
            因此，当用户在注册页面点击注册按钮提交表单是，浏览器会发送一个请求，该请求的抽象路径如下:
            /myweb/reg?username=fanchuanqi&password=123456&nickname=chuanqi&age=22
            那么我们判断?左侧的请求部分(HttpServletRequest解析后专门保存在属性requestURI上)的值是"/myweb/reg"
            就可以断定这个请求是注册页面表单提交的请求了，于是我们就可以完成注册业务了。
         */
        if("/myweb/reg".equals(path)) {
            System.out.println("开始处理用户注册!!!!!!!!!!!!!!!!!!!!");
            UserController controller = new UserController();
            controller.reg(request,response);
        }
        else {
            File file = new File(staticDir, path);
            if (file.isFile()) { // file表示的是否为一个文件
                response.setContentFile(file);
            }
            else {
                // file表示的是一个目录或file表示的路径并不存在
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "/root/404.html");
                response.setContentFile(file);
            }
        }

        response.addHeader("Server","WebServer");
    }

}












