package com.webserver.controller;

import com.webserver.entity.Article;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/*
 * 处理与文章相关的业务
 */
public class ArticleController {

    private static File articleDir;

    static {
        articleDir = new File("./articles");
        if(!articleDir.exists()) {
            articleDir.mkdirs();
        }
    }

    /**
     * 发表文章
     * @param request
     * @param response
     */
    public void writeArticle(HttpServletRequest request, HttpServletResponse response) {

        // 1 获取表单数据
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        System.out.println(title+","+author+","+content);

        // 2 保存文章内容
        Article article = new Article(title,author,content);
        File articleFile = new File(articleDir,title + ".obj");
        try (
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(articleFile))
        )
        {
            oos.writeObject(article);

            // 3响应发表结果页面
            response.sendRedirect("/myweb/article_success.html");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}









