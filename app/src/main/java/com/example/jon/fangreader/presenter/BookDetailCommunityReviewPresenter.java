package com.example.jon.fangreader.presenter;

import android.text.TextUtils;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.ReviewListBean;
import com.example.jon.fangreader.model.bean.SortEvent;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookDetailCommunityReviewContract;
import com.example.jon.fangreader.utils.SystemUtils;
import com.google.gson.Gson;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/17.
 */

public class BookDetailCommunityReviewPresenter extends RxPresenter<BookDetailCommunityReviewContract.View> implements BookDetailCommunityReviewContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public BookDetailCommunityReviewPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
        registerEvent();
    }

    private void registerEvent() {
        Subscription subscription = RxBus.getDefault().toObservable(SortEvent.class)
                .subscribe(new Action1<SortEvent>() {
                    @Override
                    public void call(SortEvent event) {
                        mView.receiveEvent(event);
                    }
                });
        addSubscription(subscription);
    }


    @Override
    public void getBookReviewList(final String bookId, final String sort, final String start, String limit, final boolean requestLastest) {
        Observable<ReviewListBean> checkCache = Observable.create(new Observable.OnSubscribe<ReviewListBean>() {
            @Override
            public void call(Subscriber<? super ReviewListBean> subscriber) {
                if(requestLastest){
                    subscriber.onCompleted();
                    return;
                }
                String json = ACache.getCacheJson(ACache.getJsonCacheKey("book_detail_review_list",bookId,sort,start));
                if(TextUtils.isEmpty(json)){
                    subscriber.onCompleted();
                }else {
                    subscriber.onNext(new Gson().fromJson(json,ReviewListBean.class));
                }
            }
        });
        Observable<ReviewListBean> getFromNet = mRetrofitHelper.fetchBookDetailReviewList(bookId, sort, start, limit)
                .map(new Func1<ReviewListBean, ReviewListBean>() {
                    @Override
                    public ReviewListBean call(ReviewListBean reviewListBean) {
                        //缓存
                        String json = new Gson().toJson(reviewListBean);
                        ACache.saveCacheJson(ACache.getJsonCacheKey("book_detail_review_list",bookId,sort,start),json);
                        return reviewListBean;
                    }
                });
        Subscription rxSubscription = Observable.concat(checkCache,getFromNet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ReviewListBean>() {
                    @Override
                    public void call(ReviewListBean reviewListBean) {
                        mView.setBookReviewList(reviewListBean);
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
