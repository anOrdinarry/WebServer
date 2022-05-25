package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author ChrStart
 * @create 2022-05-25 11:06
 */

/*
 * 处理请求的类
 *
 */

public class DispatcherServlet {

    private static File rootDir;
    private static File staticDir;

    static {

        try {

            /*
               定义为resource目录(maven项目中src/main/java和src/main/resources实际上是一个目录)
               只不过java目录中存放的都是.java的源代码文件
               而resources目录下存放的就是非.java的其他程序中需要用到的资源文件

               实际开发中，我们在定位目录时，常使用相对路径，而实际应用的相对路径都是类加载路径
               类加载路径可以用:
               类名.class.getClassLoader().getResource(".")就是类加载路径
               这里可以理解为时src/main/java目录或src/main/resources
               实际表达的是编译后这两个目录最终合并的target/classes目录
            */

            // 这里相当于定位的是当前项目下的resources目录
            // 固定写法:
            rootDir = new File (
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );

            /*
               定位resources下的static目录
               注: resources下的static目录是sprint boot项目中用于存放所有静态资源
                   的目录，相当于我们写的"网站"中用到的页面，图片等资源都放在static下
            */
            staticDir = new File(rootDir,"static");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void service(HttpServletRequest request, HttpServletResponse response) {

        String path = request.getUri();

        // 定位页面: resources/static/myweb/index.html

        // 方法一:
        // 地址栏写: http://localhost:8088 -- 成功运行
        // 运行错误时，可查看该module的target文件中是否有对应文件，无则复制相应文件至target文件下classes中对应位置
        // File file = new File(staticDir,"/myweb/index.html");

        // 方法二:
        // 地址栏写: http://localhost:8088/myweb/index.html or http://localhost:8088/myweb/classTable.html -- 成功运行
        File file = new File(staticDir, path);

        if(file.isFile()) { // file表示的是否为一个文件

            response.setContentFile(file);

        }
        else { // file表示的是一个目录或file表示的路径并不存在

            response.setStatusCode(404);
            response.setStatusReason("NotFound");

            file = new File(staticDir,"/root/404.html");

            response.setContentFile(file);

        }

    }

}





























