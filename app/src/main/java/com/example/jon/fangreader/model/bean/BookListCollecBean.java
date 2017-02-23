package com.example.jon.fangreader.model.bean;

/**
 * Created by jon on 2017/2/22.
 */

public class BookListCollecBean {
    private String id;
    private String cover;
    private String title;
    private String author;
    private String desc;
    private int collecCount;
    private int booksCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCollecCount() {
        return collecCount;
    }

    public void setCollecCount(int collecCount) {
        this.collecCount = collecCount;
    }

    public int getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }
}
