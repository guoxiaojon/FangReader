package com.example.jon.fangreader.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.jon.fangreader.app.App;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jon on 2017/1/2.
 */

public abstract class BaseActivity<T extends RxPresenter> extends AppCompatActivity implements BaseView {

    @Inject
    protected T mPresenter;

    protected Activity mContext;

    private Unbinder mUnbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initInject();
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
        App.getInstance().addActivity(this);

        initDataAndEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
        if(mPresenter != null){
            mPresenter.detachView();
        }
        App.getInstance().removeActivity(this);

    }

    @Override
    public void showError(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }



    protected abstract void initDataAndEvent();
    protected abstract @LayoutRes int getLayoutId();
    protected abstract void initInject();



}
