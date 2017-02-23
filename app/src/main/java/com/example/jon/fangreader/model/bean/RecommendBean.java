package com.example.jon.fangreader.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jon on 2017/1/3.
 */

public class RecommendBean {
    /**
     {"books":[
     {"_id":"536c79e8958ff186030000a3"
     ,"title":"特种兵在都市"
     ,"author":"夜十三"
     ,"shortIntro":"他性格嚣张狂妄，为达目的不折手段。他痞气十足，各种美女为他痴狂。在这繁华的都市，杨洛上演了一场激情四射的热血人生。"
     ,"cover":"/agent/http://img1.readnovel.com/incoming/book/0/40/120040_mr.jpg"
     ,"hasCp":true
     ,"latelyFollower":114622
     ,"latelyFollowerBase":0
     ,"minRetentionRatio":0
     ,"retentionRatio":41.57
     ,"updated":"2017-01-03T02:18:08.784Z"
     ,"chaptersCount":2219
     ,"lastChapter":"第2218章 无法逃脱的命运"},

     ·····

     ]
     ,"ok":true}

     * */
    private List<Book> books;
    private boolean ok;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }


    public static class Book implements Serializable{
        /**
         * {"_id":"536c79e8958ff186030000a3"
         ,"title":"特种兵在都市"
         ,"author":"夜十三"
         ,"shortIntro":"他性格嚣张狂妄，为达目的不折手段。他痞气十足，各种美女为他痴狂。在这繁华的都市，杨洛上演了一场激情四射的热血人生。"
         ,"cover":"/agent/http://img1.readnovel.com/incoming/book/0/40/120040_mr.jpg"
         ,"hasCp":true
         ,"latelyFollower":114622
         ,"latelyFollowerBase":0
         ,"minRetentionRatio":0
         ,"retentionRatio":41.57
         ,"updated":"2017-01-03T02:18:08.784Z"
         ,"chaptersCount":2219
         ,"lastChapter":"第2218章 无法逃脱的命运"},*/

        @SerializedName("_id")
        private String id;

        private String title;
        private String author;
        private String shortIntro;
        private String cover;
        private boolean hasCp;
        private int latelyFollower;
        private int latelyFollowerBase;
        private double minRetentionRatio;
        private double retentionRatio;
        private String updated;
        private int chaptersCount;
        private String lastChapter;

        public boolean isFromLocal() {
            return isFromLocal;
        }

        public void setFromLocal(boolean fromLocal) {
            isFromLocal = fromLocal;
        }

        //
        private boolean isFromLocal = false;
        private boolean isTop = false;
        private long collecTime;
        private boolean isReaded=false;
        private boolean isSelectState = false;
        private boolean isSelected = false;


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }



        public boolean isReaded() {
            return isReaded;
        }

        public void setReaded(boolean readed) {
            isReaded = readed;
        }

        public boolean isSelectState() {
            return isSelectState;
        }

        public void setSelectState(boolean selectState) {
            isSelectState = selectState;
        }




        public long getCollecTime() {
            return collecTime;
        }

        public void setCollecTime(long collecTime) {
            this.collecTime = collecTime;
        }

        public boolean isTop() {
            return isTop;
        }

        public void setTop(boolean top) {
            isTop = top;
        }





        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getShortIntro() {
            return shortIntro;
        }

        public void setShortIntro(String shortIntro) {
            this.shortIntro = shortIntro;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public boolean isHasCp() {
            return hasCp;
        }

        public void setHasCp(boolean hasCp) {
            this.hasCp = hasCp;
        }

        public int getLatelyFollower() {
            return latelyFollower;
        }

        public void setLatelyFollower(int latelyFollower) {
            this.latelyFollower = latelyFollower;
        }

        public int getLatelyFollowerBase() {
            return latelyFollowerBase;
        }

        public void setLatelyFollowerBase(int latelyFollowerBase) {
            this.latelyFollowerBase = latelyFollowerBase;
        }

        public double getMinRetentionRatio() {
            return minRetentionRatio;
        }

        public void setMinRetentionRatio(double minRetentionRatio) {
            this.minRetentionRatio = minRetentionRatio;
        }

        public double getRetentionRatio() {
            return retentionRatio;
        }

        public void setRetentionRatio(double retentionRatio) {
            this.retentionRatio = retentionRatio;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public int getChaptersCount() {
            return chaptersCount;
        }

        public void setChaptersCount(int chaptersCount) {
            this.chaptersCount = chaptersCount;
        }

        public String getLastChapter() {
            return lastChapter;
        }

        public void setLastChapter(String lastChapter) {
            this.lastChapter = lastChapter;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof RecommendBean.Book){
                RecommendBean.Book book = (RecommendBean.Book)obj;
                return this.id.equals(book.getId());
            }
            return super.equals(obj);

        }
    }
}
