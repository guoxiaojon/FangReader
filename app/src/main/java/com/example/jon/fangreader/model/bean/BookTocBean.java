package com.example.jon.fangreader.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jon on 2017/1/3.
 */

public class BookTocBean {

    /**
     *
     {"mixToc":
     {
     "_id":"53a2d0f4fda0a68d82ff89d3" //作用未知
     ,"book":"536c79e8958ff186030000a3"  //书的ID
     ,"chaptersCount1":2155  //书的总共章节数
     ,"chaptersUpdated":"2017-01-02T21:36:45.224Z"   //书章节的最后更新时间
     ,"chapters":[
     {
     "title":"001章 引子"
     ,"link":"http://www.luoqiu.com/read/26083/6882042.html"
     ,"unreadble":false
     },
     ·····
     ]
     ,"updated":"2017-01-02T21:36:45.224Z"    //书的最后更新时间，同chapterUpdated一样
     }
     ,"ok":true}*/

    private boolean ok;
    private MixToc mixToc;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public MixToc getMixToc() {
        return mixToc;
    }

    public void setMixToc(MixToc mixToc) {
        this.mixToc = mixToc;
    }

    public static class MixToc{
        @SerializedName("_id")
        private String id;
        private String book; //书的Id
        private int chaptersCount1; //一共多少章
        private String chaptersUpdated;
        private List<Chapter> chapters;
        private String updated;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public int getChaptersCount1() {
            return chaptersCount1;
        }

        public void setChaptersCount1(int chaptersCount1) {
            this.chaptersCount1 = chaptersCount1;
        }

        public String getChaptersUpdated() {
            return chaptersUpdated;
        }

        public void setChaptersUpdated(String chaptersUpdated) {
            this.chaptersUpdated = chaptersUpdated;
        }

        public List<Chapter> getChapters() {
            return chapters;
        }

        public void setChapters(List<Chapter> chapters) {
            this.chapters = chapters;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public static class Chapter implements Serializable{
            private String title;
            private String link;
            private boolean unreadble;

            public boolean isUnreadble() {
                return unreadble;
            }

            public void setUnreadble(boolean unreadble) {
                this.unreadble = unreadble;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Override
            public String toString() {
                return String.valueOf(title);
            }
        }


    }

}
