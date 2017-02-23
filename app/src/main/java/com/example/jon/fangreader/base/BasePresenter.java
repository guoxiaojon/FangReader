package com.example.jon.fangreader.base;

/**
 * Created by jon on 2017/1/2.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}
