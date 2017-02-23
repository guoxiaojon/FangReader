package com.example.jon.fangreader.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jon on 2017/1/2.
 */

public class RxPresenter <T extends BaseView> implements BasePresenter<T> {

    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(T view) {
        this.mView = view;

    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscription();


    }

    protected void addSubscription(Subscription subscription){
        if(mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    private void unSubscription(){
        if(mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
}
