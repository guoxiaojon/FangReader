package com.example.jon.fangreader.presenter.contract;

import com.example.jon.fangreader.base.BasePresenter;
import com.example.jon.fangreader.base.BaseView;
import com.example.jon.fangreader.model.bean.BookDetailBean;
import com.example.jon.fangreader.model.bean.HotReviewBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;

/**
 * Created by jon on 2017/2/20.
 */

public interface BookDetailContract {
    interface View extends BaseView{
        void setBookDetail(BookDetailBean bookDetail);
        void setHotReview(HotReviewBean hotReview);
        void setRecommendBookList(RecommendBookList recommendBookList);

    }
    interface Presenter extends BasePresenter<View>{
        void getBookDetail(String bookId);
        void getHotReview(String bookId);
        void getRecommendBookList(String bookId,String limit);
    }
}
