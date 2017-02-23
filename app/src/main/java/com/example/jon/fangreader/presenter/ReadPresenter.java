package com.example.jon.fangreader.presenter;

import android.util.Log;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.BookMark;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.ChapterForReadBean;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.ReadContract;
import com.example.jon.fangreader.utils.FileUtils;
import com.example.jon.fangreader.utils.SystemUtils;

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
 * Created by jon on 2017/1/5.
 */

public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public ReadPresenter(RetrofitHelper retrofitHelper) {
        this.mRetrofitHelper = retrofitHelper;
        registEvent();
    }

    private void registEvent() {
        Subscription subscription = RxBus.getDefault().toObservable(DownloadEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadEvent>() {
                    @Override
                    public void call(DownloadEvent event) {
                        mView.setCacheProgress(event);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getChapterList(String bookId) {
        Subscription rxSubscription = mRetrofitHelper.fetchBookToc(bookId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<BookTocBean, List<BookTocBean.MixToc.Chapter>>() {
                    @Override
                    public List<BookTocBean.MixToc.Chapter> call(BookTocBean bookTocBean) {
                        List<BookTocBean.MixToc.Chapter> chapters = bookTocBean.getMixToc().getChapters();
                        Log.d("data", "=====>>>" + bookTocBean.getMixToc().getChapters());
                        return chapters;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BookTocBean.MixToc.Chapter>>() {
                    @Override
                    public void call(List<BookTocBean.MixToc.Chapter> chapters) {
                        mView.setChapterList(chapters);
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
    public void getChapterOnStart(final int chapterIndex, final String bookId, final String url, final String title) {

        Subscription rxSubscription = mRetrofitHelper.fetchChapter(url)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ChapterForReadBean, ChapterForReadBean>() {
                    @Override
                    public ChapterForReadBean call(ChapterForReadBean readBean) {
                        readBean.getChapter().setTitle(title);
                        ACache.saveChapterForRead(bookId, url, readBean);
                        return readBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChapterForReadBean>() {
                    @Override
                    public void call(ChapterForReadBean readBean) {
                        mView.getChapterSuccessfulOnStart(chapterIndex);
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
    public void getChapter(final int chapterIndex, final String bookId, final String url, final boolean loadNext, final String title, final boolean useVolume) {
        Subscription rxSubscription = mRetrofitHelper.fetchChapter(url)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ChapterForReadBean, ChapterForReadBean>() {
                    @Override
                    public ChapterForReadBean call(ChapterForReadBean readBean) {
                        readBean.getChapter().setTitle(title);
                        ACache.saveChapterForRead(bookId, url, readBean);
                        return readBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChapterForReadBean>() {
                    @Override
                    public void call(ChapterForReadBean readBean) {

                        mView.getChapterSuccessful(chapterIndex, loadNext, useVolume);


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
    public void saveBookMark(List<BookMark> marks, final String bookId) {
        Subscription subscription = Observable.just(marks)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<BookMark>>() {
                    @Override
                    public void call(List<BookMark> marks) {
                        FileUtils.saveBookMark(marks,bookId);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getBookMark(final String bookId) {
        Log.d("data","getBookMArk()");
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<BookMark>>() {
            @Override
            public void call(Subscriber<? super List<BookMark>> subscriber) {
                List<BookMark> marks = FileUtils.getBookMark(bookId);
                subscriber.onNext(marks);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BookMark>>() {
                    @Override
                    public void call(List<BookMark> marks) {
                        Log.d("data","获取 BookMarks"+marks.toString());
                        mView.setBookMark(marks);
                    }
                });
        addSubscription(subscription);

    }

    @Override
    public void getChapterForBookMark(final BookMark bookMark, final String bookId, final String url, final String title) {
        Subscription rxSubscription = mRetrofitHelper.fetchChapter(url)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ChapterForReadBean, ChapterForReadBean>() {
                    @Override
                    public ChapterForReadBean call(ChapterForReadBean readBean) {
                        readBean.getChapter().setTitle(title);
                        ACache.saveChapterForRead(bookId, url, readBean);
                        return readBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChapterForReadBean>() {
                    @Override
                    public void call(ChapterForReadBean readBean) {

                        mView.getChapterForBookMarkSuccessful(bookMark);


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
