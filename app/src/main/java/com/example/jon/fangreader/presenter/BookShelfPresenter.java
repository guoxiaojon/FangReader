package com.example.jon.fangreader.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.model.bean.SyncBookShelfEvent;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.BookShelfContract;
import com.example.jon.fangreader.service.DownloadService;
import com.example.jon.fangreader.utils.CollectionUtil;
import com.example.jon.fangreader.utils.DateUtil;
import com.example.jon.fangreader.utils.SharePreferenceUtil;
import com.example.jon.fangreader.utils.SystemUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/1/4.
 */

public class BookShelfPresenter extends RxPresenter<BookShelfContract.View> implements BookShelfContract.Presenter {

    private RetrofitHelper mRetrofitHelper;
    private Context mContext;
    @Inject
    public BookShelfPresenter(RetrofitHelper retrofitHelper, Context context){
        this.mRetrofitHelper = retrofitHelper;
        this.mContext = context;
        registerEvent();

    }

    private void registerEvent(){
        Subscription subscription = RxBus.getDefault().toObservable(SyncBookShelfEvent.class)
                .observeOn(Schedulers.io())
                .map(new Func1<SyncBookShelfEvent, List<RecommendBean.Book>>() {
                    @Override
                    public List<RecommendBean.Book> call(SyncBookShelfEvent syncBookShelfEvent) {
                        return CollectionUtil.getCollectionList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RecommendBean.Book>>() {
                    @Override
                    public void call(List<RecommendBean.Book> list) {
                        mView.showBookShelf(list);
                    }
                });
        addSubscription(subscription);
        subscription = RxBus.getDefault().toObservable(DownloadEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadEvent>() {
                    @Override
                    public void call(DownloadEvent event) {
                        mView.showCacheProgress(event);
                    }
                });
        addSubscription(subscription);

    }


    @Override
    public void setTop(RecommendBean.Book book,final boolean setTop) {
        if(book == null)
            return;

        Subscription subscription = Observable.just(book)
                .observeOn(Schedulers.io())
                .map(new Func1<RecommendBean.Book, List<RecommendBean.Book>>() {
                    @Override
                    public List<RecommendBean.Book> call(RecommendBean.Book book) {
                        if(setTop){
                            CollectionUtil.setTop(book);
                        }else {
                            CollectionUtil.cancelTop(book);
                        }
                        return CollectionUtil.getCollectionList() ;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RecommendBean.Book>>() {
                    @Override
                    public void call(List<RecommendBean.Book> list) {
                        if(setTop){
                            mView.showSetTopSuccessful();
                        }
                        mView.showBookShelf(list);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("出错啦~");
                    }
                });
        addSubscription(subscription);


    }

    @Override
    public void cacheBook(final RecommendBean.Book book) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookToc(book.getId())
                .subscribeOn(Schedulers.io())
                .map(new Func1<BookTocBean, BookTocBean.MixToc>() {
                    @Override
                    public BookTocBean.MixToc call(BookTocBean bookTocBean) {

                        return bookTocBean.getMixToc();
                    }
                })
                .subscribe(new Action1<BookTocBean.MixToc>() {
                    @Override
                    public void call(BookTocBean.MixToc mixToc) {
                        List<BookTocBean.MixToc.Chapter> chapters = mixToc.getChapters();
                        for(int i=0;i<5;i++){
                            Intent intent = new Intent(mContext, DownloadService.class);
                            intent.putExtra("url",chapters.get(i).getLink());
                            intent.putExtra("bookId",book.getId());
                            intent.putExtra("currChapter",i+1);
                            intent.putExtra("count",mixToc.getChaptersCount1());
                            intent.putExtra("title",book.getTitle());
                            mContext.startService(intent);


                        }

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
    public void deleteBooks(List<RecommendBean.Book> books, final boolean deleteCache) {
        if(books == null)
            return;
        Subscription subscription = Observable.just(books)
                .observeOn(Schedulers.io())
                .map(new Func1<List<RecommendBean.Book>, List<RecommendBean.Book>>() {
                    @Override
                    public List<RecommendBean.Book> call(List<RecommendBean.Book> books) {
                        for(RecommendBean.Book book : books){
                            CollectionUtil.deleteCollectionItem(book.getId());
                            if(deleteCache){
                                ACache.deleteCache(book.getId());
                            }
                        }
                        return CollectionUtil.getCollectionList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RecommendBean.Book>>() {
                    @Override
                    public void call(List<RecommendBean.Book> list) {
                        mView.showDeleteSuccessful();
                        mView.showBookShelf(list);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getRecommendList() {
        Observable<List<RecommendBean.Book>> getListFromDisk = Observable.create(new Observable.OnSubscribe<List<RecommendBean.Book>>() {
            @Override
            public void call(Subscriber<? super List<RecommendBean.Book>> subscriber) {
                List<RecommendBean.Book> list = CollectionUtil.getCollectionList();
                if(list !=null && list.size() != 0){
                    subscriber.onNext(list);
                    Log.d("data","本地收藏列表");
                }else {
                    subscriber.onCompleted();
                    Log.d("data","网络收藏列表");
                }


            }
        });

        Observable<List<RecommendBean.Book>> getListFromNet = mRetrofitHelper.fetchRecommend(SharePreferenceUtil.getGender())
                .map(new Func1<RecommendBean, List<RecommendBean.Book>>() {
                    @Override
                    public List<RecommendBean.Book> call(RecommendBean recommendBean) {
                        Logger.e("网络获取了收藏列表");
                        return recommendBean.getBooks();

                    }
                });

        Subscription rxSubscription = Observable.concat(getListFromDisk,getListFromNet)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<RecommendBean.Book>, List<RecommendBean.Book>>() {
                    @Override
                    public List<RecommendBean.Book> call(List<RecommendBean.Book> books) {
                        int shift = 1;//时间差距明显化
                        for(RecommendBean.Book book : books){
                            if(book.getCollecTime() != 0)
                                continue;
                            book.setCollecTime(DateUtil.getCurrentMillioneSceond()+(shift++));
                        }
                        CollectionUtil.saveCollectionList(books);
                        return books;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RecommendBean.Book>>() {
                    @Override
                    public void call(List<RecommendBean.Book> list) {
                        mView.showBookShelf(list);
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
