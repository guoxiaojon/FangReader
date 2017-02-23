package com.example.jon.fangreader.presenter;

import android.text.TextUtils;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.DiscussionListBean;
import com.example.jon.fangreader.model.bean.SortEvent;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookDetailCommunityDiscussionContract;
import com.example.jon.fangreader.utils.SystemUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/14.
 */

public class BookDetailCommunityDiscussionPresenter extends RxPresenter<BookDetailCommunityDiscussionContract.View> implements BookDetailCommunityDiscussionContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    @Inject
    public BookDetailCommunityDiscussionPresenter(RetrofitHelper retrofitHelper){
        this.mRetrofitHelper = retrofitHelper;
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
    public void getBookDetailDiscussionList(final String bookId, final String sort, final int start, int limit, final boolean requestLastest) {
        Observable<DiscussionListBean> checkDiskCahce = Observable.create(new Observable.OnSubscribe<DiscussionListBean>() {
            @Override
            public void call(Subscriber<? super DiscussionListBean> subscriber) {
                if(requestLastest){
                    subscriber.onCompleted();
                    return;
                }
                String json = ACache.getCacheJson(ACache.getJsonCacheKey("book_detail_discussion_list",bookId,sort,start));
                Logger.e("读取保存的Json : "+json);
                if(!TextUtils.isEmpty(json)){
                    DiscussionListBean bean = new Gson().fromJson(json,DiscussionListBean.class);
                    subscriber.onNext(bean);
                }else {
                    subscriber.onCompleted();
                }
            }
        });

        Observable<DiscussionListBean> getFromNet = mRetrofitHelper.fetchBookDetailDiscussionList(bookId,sort,"normal,vote",String.valueOf(start),String.valueOf(limit))
                .map(new Func1<DiscussionListBean, DiscussionListBean>() {
                    @Override
                    public DiscussionListBean call(DiscussionListBean discussionListBean) {
                        //缓存
                        String json = new Gson().toJson(discussionListBean);
                        Logger.e("缓存 json： "+json);
                        ACache.saveCacheJson(ACache.getJsonCacheKey("book_detail_discussion_list",bookId,sort,start),json);
                        return discussionListBean;
                    }
                });
        Subscription rxSubscription = Observable.concat(checkDiskCahce,getFromNet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DiscussionListBean>() {
                    @Override
                    public void call(DiscussionListBean discussionListBean) {
                        mView.setBookDetailDiscussionList(discussionListBean);
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
