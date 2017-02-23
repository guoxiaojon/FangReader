package com.example.jon.fangreader.presenter;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.ReviewDetailBean;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookReviewDetailContract;
import com.example.jon.fangreader.utils.SystemUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/17.
 */

public class BookReviewDetailPresenter extends RxPresenter<BookReviewDetailContract.View> implements BookReviewDetailContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public BookReviewDetailPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }


    @Override
    public void getReviewDetail(String reviewId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookReviewDetail(reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ReviewDetailBean>() {
                    @Override
                    public void call(ReviewDetailBean reviewDetailBean) {
                        mView.setReviewDetail(reviewDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("无网络~");
                        }
                    }
                });
        addSubscription(rxSubscription);

    }

    @Override
    public void getBestComments(String reviewId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBestCommentList(reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommentListBean>() {
                    @Override
                    public void call(CommentListBean commentListBean) {
                        mView.setBestComments(commentListBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("无网络~");
                        }
                    }
                });
        addSubscription(rxSubscription);
    }

    @Override
    public void getReviewComments(String reviewId, String start, String limit) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookReviewCommentList(reviewId,start,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommentListBean>() {
                    @Override
                    public void call(CommentListBean commentListBean) {
                        mView.setReviewComments(commentListBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("无网络~");
                        }
                    }
                });
        addSubscription(rxSubscription);
    }
}
