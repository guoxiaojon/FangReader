package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.ReviewDetailBean;

/**
 * Created by jon on 2017/2/17.
 */

public interface BookReviewDetailContract {
    interface View extends BaseView{
        void setReviewDetail(ReviewDetailBean bean);
        void setBestComments(CommentListBean commentListBean);
        void setReviewComments(CommentListBean commentListBean);
    }
    interface Presenter extends BasePresenter<View>{
        void getReviewDetail(String reviewId);
        void getBestComments(String reviewId);
        void getReviewComments(String reviewId,String start,String limit);

    }
}
