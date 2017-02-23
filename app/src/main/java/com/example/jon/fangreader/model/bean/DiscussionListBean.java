package com.example.jon.fangreader.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jon on 2017/2/13.
 */

public class DiscussionListBean {
    /**
     * _id : 57c564e9818f568675624358
     * title : 【活动预告】9月2-4日三天两觉《惊悚乐园》签名书抢楼   (评论的内容)   <--
     * author : {"_id":"52f840b982cfcc3a74031693",
     * "avatar":"/avatar/56/a9/56a96462a50ca99f9cf83440899e46f3","nickname":"追书首席打杂",
     * "type":"official","lv":9,"gender":"male"}
     * type : vote          <---
     * likeCount : 651      <---
     * block : ramble
     * state : hot          <---
     * updated : 2016-09-01T12:30:52.286Z
     * created : 2016-08-30T10:50:17.927Z    <---
     * commentCount : 5366                   <---
     * voteCount : 6337
     */

    private List<PostBean> posts;

    public List<PostBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostBean> posts) {
        this.posts = posts;
    }

    public static class PostBean{
        @SerializedName("_id")
        private String id;
        private String title;
        private Author author;
        private String type;
        private int likeCount;
        private String block;
        private String state;
        private String updated;
        private String created;
        private int commentCount;
        private int voteCount;

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

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }


        public static class Author{
            @SerializedName("_id")
            private String id;
            private String avatar;
            private String nickname;
            private String type;
            private int lv;
            private String gender;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getLv() {
                return lv;
            }

            public void setLv(int lv) {
                this.lv = lv;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }
        }



    }
}
