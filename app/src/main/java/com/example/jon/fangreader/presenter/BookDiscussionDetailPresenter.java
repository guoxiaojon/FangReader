package com.example.jon.fangreader.presenter;

import android.content.Context;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.model.bean.CommentListBean;
import com.example.jon.fangreader.model.bean.DiscussionDetailBean;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookDiscussionDetailContract;
import com.example.jon.fangreader.utils.SystemUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/15.
 */

public class BookDiscussionDetailPresenter extends RxPresenter<BookDiscussionDetailContract.View> implements BookDiscussionDetailContract.Presenter {
    private RetrofitHelper mRetrofitHelper;
    private Context mContext;

    @Inject
    public BookDiscussionDetailPresenter(RetrofitHelper mRetrofitHelper, Context mContext) {
        this.mRetrofitHelper = mRetrofitHelper;
        this.mContext = mContext;
    }


    @Override
    public void getDiscussionDetail(String discussionId) {
        Subscription rxSubscription = mRetrofitHelper.fetchDiscussionDetail(discussionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DiscussionDetailBean>() {
                    @Override
                    public void call(DiscussionDetailBean discussionDetailBean) {
                        mView.setDiscussionDetail(discussionDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("出错啦~");
                    }
                });
        addSubscription(rxSubscription);
    }

    @Override
    public void getBestComments(String discussionId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBestCommentList(discussionId)
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
                        mView.showError("出错啦~");
                    }
                });
        addSubscription(rxSubscription);

    }

    @Override
    public void getDiscussionComments(String discussionId, String start, String limit) {
        Subscription rxSubscription = mRetrofitHelper.fetchDiscussionCommentList(discussionId, start, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommentListBean>() {
                    @Override
                    public void call(CommentListBean commentListBean) {
                        mView.setDiscussionComments(commentListBean);
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
