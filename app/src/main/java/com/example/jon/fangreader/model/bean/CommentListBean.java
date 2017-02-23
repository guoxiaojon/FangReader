package com.example.jon.fangreader.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jon on 2017/2/15.
 */

public class CommentListBean {

    /**
     * _id : 57bf0be8c2099ef01d9d35d6
     * content : 看过 但不是很喜欢 太压抑 虽然感情戏写的不错 但虐过头了
     * author : {"_id":"55c43916ad75a05059fd23d7",
     * "avatar":"/avatar/5a/1f/5a1fb41215c3e7f9cedb8310ad76d3d8","nickname":"恋旧的人",
     * "type":"normal","lv":8,"gender":"female"}
     * floor : 105
     * likeCount : 0
     * created : 2016-08-25T15:16:56.437Z
     * replyTo : {"_id":"57bf04e65889e74a6f0808bf","floor":104,
     * "author":{"_id":"5794ad7ffda61987396d6216","nickname":"从未改变"}}
     */

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        @SerializedName("_id")
        private String id;
        private String content;
        /**
         * _id : 55c43916ad75a05059fd23d7
         * avatar : /avatar/5a/1f/5a1fb41215c3e7f9cedb8310ad76d3d8
         * nickname : 恋旧的人
         * type : normal
         * lv : 8
         * gender : female
         */

        private AuthorBean author;
        private int floor;
        private int likeCount;
        private String created;
        /**
         * _id : 57bf04e65889e74a6f0808bf
         * floor : 104
         * author : {"_id":"5794ad7ffda61987396d6216","nickname":"从未改变"}
         */

        private ReplyToBean replyTo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public ReplyToBean getReplyTo() {
            return replyTo;
        }

        public void setReplyTo(ReplyToBean replyTo) {
            this.replyTo = replyTo;
        }

        public static class AuthorBean {
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

        public static class ReplyToBean {
            @SerializedName("_id")
            private String id;
            private int floor;
            /**
             * _id : 5794ad7ffda61987396d6216
             * nickname : 从未改变
             */

            private AuthorBean author;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getFloor() {
                return floor;
            }

            public void setFloor(int floor) {
                this.floor = floor;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public static class AuthorBean {
                @SerializedName("_id")
                private String id;
                private String nickname;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }
            }
        }
    }
}
