package com.example.jon.fangreader.model.bean;

/**
 * Created by jon on 2017/1/3.
 */

public class ChapterForReadBean {
    /**
     * {"ok":true,
     "chapter":{
     "title":"074章 你还真变态"
     ,"body":"“哥们！不用打电话了，我就是警察。”司机是一名四十多岁的中年人，听见杨洛的话疑惑的看向他。\n
     杨洛拿出证件扔给他，然后上了重卡。司机拿着杨洛的证件没有看，而是瞪着惊恐的眼睛看着自己的卡车开始慢慢向前行驶，轮胎碾压物体的“咔咔”声传入耳中。鲜红的血液在车轮下缓缓流出，有如溪水顺着路面微微的坡度流到他的脚下。随着最后两个轮胎碾压过后，那两辆别克已经成了铁饼。\n
     "
     }
     }
     */

    private boolean ok;
    private Chapter chapter;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public static class Chapter{
        private String title;
        private String body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
