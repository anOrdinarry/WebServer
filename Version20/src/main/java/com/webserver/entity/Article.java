package com.webserver.entity;

import java.io.Serializable;

/*
 * 文章对象
 */
public class Article implements Serializable {

    public static final long serialVersionUID = 1L;

    private String title; // 标题

    private String author; // 作者

    private String content; // 内容

    public Article() {

    }

    public Article(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}













