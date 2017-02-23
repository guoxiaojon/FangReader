package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.BookMark;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.DownloadEvent;

import java.util.List;

/**
 * Created by jon on 2017/1/5.
 */

public interface ReadContract {
    interface View extends BaseView{
        void setChapterList(List<BookTocBean.MixToc.Chapter> chapterList);
        void getChapterSuccessfulOnStart(int chapterIndex);
        void getChapterSuccessful(int chapterIndex,boolean loadNext,boolean useVolume);
        void setCacheProgress(DownloadEvent downloadEvent);
        void setBookMark(List<BookMark> marks);
        void getChapterForBookMarkSuccessful(BookMark bookMark);


    }
    interface Presenter extends BasePresenter<View>{
        void getChapterList(String bookId);
        void getChapterOnStart(int chapterIndex, String bookId, String url,String title);
        void getChapter(int chapterIndex, String bookId, String url,boolean loadNext,String title,boolean useVolume);
        void saveBookMark(List<BookMark> marks,String bookId);
        void getBookMark(String bookId);
        void getChapterForBookMark(BookMark bookMark,String bookId,String url,String title);
    }
}
