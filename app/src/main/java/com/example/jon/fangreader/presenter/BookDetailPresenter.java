package com.example.jon.fangreader.presenter;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.model.bean.BookDetailBean;
import com.example.jon.fangreader.model.bean.HotReviewBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookDetailContract;
import com.example.jon.fangreader.utils.SystemUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/22.
 */

public class BookDetailPresenter extends RxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter  {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public BookDetailPresenter(RetrofitHelper retrofitHelper){
        this.mRetrofitHelper = retrofitHelper;
    }

    @Override
    public void getBookDetail(String bookId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookDetail(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BookDetailBean>() {
                    @Override
                    public void call(BookDetailBean bookDetailBean) {
                        mView.setBookDetail(bookDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("网络出错啦");
                        }
                    }
                });
        addSubscription(rxSubscription);

    }

    @Override
    public void getHotReview(String bookId) {
        Subscription rxSubscription = mRetrofitHelper.fetchHotReview(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HotReviewBean>() {
                    @Override
                    public void call(HotReviewBean hotReviewBean) {
                        mView.setHotReview(hotReviewBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("网络出错啦");
                        }
                    }
                });
        addSubscription(rxSubscription);
    }

    @Override
    public void getRecommendBookList(String bookId, String limit) {
        Subscription rxSubscription = mRetrofitHelper.fetchRecommendBookList(bookId, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RecommendBookList>() {
                    @Override
                    public void call(RecommendBookList recommendBookList) {
                        mView.setRecommendBookList(recommendBookList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("网络出错啦");
                        }
                    }
                });
        addSubscription(rxSubscription);

    }
}
