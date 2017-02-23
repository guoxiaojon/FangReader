package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.DiscussionListBean;
import com.example.jon.fangreader.model.bean.SortEvent;

/**
 * Created by jon on 2017/2/13.
 */

public interface BookDetailCommunityDiscussionContract {
    interface View extends BaseView{
        void setBookDetailDiscussionList(DiscussionListBean discussionList);
        void receiveEvent(SortEvent event);

    }
    interface Presenter extends BasePresenter<View>{
        void getBookDetailDiscussionList(String bookId,String sort,int start,int limit,boolean requestLastest);

    }

}
