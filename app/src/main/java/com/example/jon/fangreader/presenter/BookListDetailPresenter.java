package com.example.jon.fangreader.presenter;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.model.bean.BookListDetailBean;
import com.example.jon.fangreader.model.bean.RecommendBookList;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookListDetailContract;
import com.example.jon.fangreader.utils.CollectionUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/2/23.
 */

public class BookListDetailPresenter extends RxPresenter<BookListDetailContract.View> implements BookListDetailContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public BookListDetailPresenter(RetrofitHelper retrofitHelper){
        this.mRetrofitHelper = retrofitHelper;
    }
    @Override
    public void getBookListDetail(String bookListId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookListDetail(bookListId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BookListDetailBean>() {
                    @Override
                    public void call(BookListDetailBean bookListDetailBean) {
                        mView.setBookListDetail(bookListDetailBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if(SystemUtils.isNetAvailable()){
                            mView.showError("出错啦~");
                        }else {
                            mView.showError("网络出错啦~");
                        }
                    }
                });
        addSubscription(rxSubscription);

    }

    @Override
    public void collecBookList(RecommendBookList.RecommendBook collecBean) {
        Subscription subscription = Observable.just(collecBean)
                .subscribeOn(Schedulers.io())
                .map(new Func1<RecommendBookList.RecommendBook, Boolean>() {
                    @Override
                    public Boolean call(RecommendBookList.RecommendBook collecBean) {
                        return CollectionUtil.addBookListCollection(collecBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(aBoolean){
                            mView.showMsg("添加成功");
                        }else {
                            mView.showMsg("已经存在啦~");
                        }
                    }
                });

        addSubscription(subscription);
    }
}

