package com.example.jon.fangreader.presenter;

import com.example.jon.fangreader.base.RxPresenter;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.model.bean.RecommendBean;
import com.example.jon.fangreader.model.http.RetrofitHelper;
import com.example.jon.fangreader.presenter.contract.MainContract;
import com.example.jon.fangreader.utils.CollectionUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jon on 2017/1/2.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private RetrofitHelper mRetrofitHelper;

    @Inject
    public MainPresenter(RetrofitHelper retrofitHelper){
        this.mRetrofitHelper = retrofitHelper;
    }

    @Override
    public void syncBookShelf() {
        //更新收藏列表最新状态
        List<RecommendBean.Book> books = CollectionUtil.getCollectionList();
        if(books == null){
            return;
        }
        List<Observable<BookTocBean.MixToc>> mixTocs = new ArrayList<>();
        for(RecommendBean.Book book : books){
            if(book.isFromLocal())
                return;
            Observable<BookTocBean.MixToc> observable = mRetrofitHelper.fetchBookToc(book.getId())
                    .map(new Func1<BookTocBean, BookTocBean.MixToc>() {
                        @Override
                        public BookTocBean.MixToc call(BookTocBean bookTocBean) {
                            return bookTocBean.getMixToc();

                        }
                    });
            mixTocs.add(observable);

        }

        Subscription rxSubscription = Observable.mergeDelayError(mixTocs)
                .subscribeOn(Schedulers.io())
                .map(new Func1<BookTocBean.MixToc, BookTocBean.MixToc>() {
                    @Override
                    public BookTocBean.MixToc call(BookTocBean.MixToc mixToc) {
                        List<BookTocBean.MixToc.Chapter> list = mixToc.getChapters();
                        CollectionUtil.setLastChapterAndUpdateTime(mixToc.getBook(),list.get(list.size()-1).getTitle(),mixToc.getChaptersUpdated());
                        return mixToc;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BookTocBean.MixToc>() {
                    @Override
                    public void call(BookTocBean.MixToc mixToc) {
                        mView.syncBookShelfCompleted();
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
