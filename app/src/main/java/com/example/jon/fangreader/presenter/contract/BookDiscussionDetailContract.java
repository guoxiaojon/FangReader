package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.DiscussionDetailBean;

/**
 * Created by jon on 2017/2/15.
 */

public interface BookDiscussionDetailContract {
    interface View extends BaseView{
        void setDiscussionDetail(DiscussionDetailBean bean);
        void setBestComments(CommentListBean bestComments);
        void setDiscussionComments(CommentListBean commentListBean);

    }
    interface Presenter extends BasePresenter<View>{
        void getDiscussionDetail(String discussionId);
        void getBestComments(String discussionId);
        void getDiscussionComments(String discussionId,String start,String limit);
    }
}
