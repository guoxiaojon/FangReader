package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.model.bean.RecommendBean;

import java.util.List;

/**
 * Created by jon on 2017/1/4.
 */

public interface BookShelfContract {
    interface View extends BaseView{
        void showBookShelf(List<RecommendBean.Book> books);
        void showCacheProgress(DownloadEvent event);
        void showSetTopSuccessful();
        void showDeleteSuccessful();
    }

    interface Presenter extends BasePresenter<View>{
        void setTop(RecommendBean.Book book,boolean setTop);
        void cacheBook(RecommendBean.Book book);
        void deleteBooks(List<RecommendBean.Book> boos,boolean deleteCache);
        void getRecommendList();
    }
}
