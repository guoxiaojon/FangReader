package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.BookListDetailBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;

/**
 * Created by jon on 2017/2/22.
 */

public interface BookListDetailContract {
    interface View extends BaseView{
        void setBookListDetail(BookListDetailBean bean);
        void showMsg(String msg);
    }
    interface Presenter extends BasePresenter<View>{
        void getBookListDetail(String bookListId);
        void collecBookList(RecommendBookList.RecommendBook collecBean);
    }
}
