package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.ReviewListBean;
import com.example.jon.fangreader.model.bean.SortEvent;

/**
 * Created by jon on 2017/2/17.
 */

public interface BookDetailCommunityReviewContract {
    interface View extends BaseView{
        void setBookReviewList(ReviewListBean bean);
        void receiveEvent(SortEvent event);
    }
    interface Presenter extends BasePresenter<View>{
        void getBookReviewList(String bookId,String sort,String start,String limit,boolean requestLastest);
    }
}
