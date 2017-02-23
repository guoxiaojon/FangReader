package com.example.jon.fangreader.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.component.RxBus;
import com.example.jon.fangreader.model.bean.ChapterForReadBean;
import com.example.jon.fangreader.model.bean.DownloadEvent;
import com.example.jon.fangreader.model.http.RetrofitHelper;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jon on 2017/1/4.
 */

public class DownloadService extends IntentService {


    private RetrofitHelper mRetrofitHelper;
    private CompositeSubscription compositeSubscription;

    public DownloadService() {
        super("downloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRetrofitHelper = new RetrofitHelper();
        compositeSubscription = new CompositeSubscription();
        Log.d("data","Downloadservice create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("data","Download OnStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final String url = intent.getStringExtra("url");
        final String bookId=intent.getStringExtra("bookId");
        final int currChapter = intent.getIntExtra("currChapter",-1);
        final String title = intent.getStringExtra("title");
        final int count = intent.getIntExtra("count",-1);
        Log.d("data","url : "+url);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Subscription subscription = mRetrofitHelper.fetchChapter(url)
                .map(new Func1<ChapterForReadBean, ChapterForReadBean>() {
                    @Override
                    public ChapterForReadBean call(ChapterForReadBean readBean) {
                        ACache.saveChapterForRead(bookId,url,readBean);
                        return readBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ChapterForReadBean>() {
                    @Override
                    public void call(ChapterForReadBean readBean) {
                        Log.d("data2",""+readBean.getChapter().getBody());

                        RxBus.getDefault().post(new DownloadEvent(
                                count==currChapter?true:false
                                ,currChapter,count,title));
                        Log.d("data3",""+currChapter+"/"+count);
                    }
                });
        compositeSubscription.add(subscription);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("data","DownloadService OnDestory");
        mRetrofitHelper = null;
        compositeSubscription.unsubscribe();


    }
}
