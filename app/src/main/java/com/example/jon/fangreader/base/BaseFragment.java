package com.example.jon.fangreader.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jon on 2017/1/2.
 */

public abstract class BaseFragment <T extends RxPresenter> extends Fragment implements BaseView {
    @Inject
    protected T mPresenter;

    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity)mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(getLayoutId(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this,view);
        initInject();
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
        initDataAndEvent();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public void showError(String msg) {

    }

    protected abstract void initInject();
    protected abstract @LayoutRes int getLayoutId();
    protected abstract void initDataAndEvent();

}
