package com.example.jon.fangreader.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.jon.fangreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jon on 2017/2/13.
 */
/**
 * activity without MVP
 * */
public abstract class SimpleActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    protected Toolbar mToolBar;
    protected Context mContext;
    protected Activity mActivity;
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        setContentView(getLayouId());
        mUnbinder = ButterKnife.bind(this);
        initDataAndEvent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }

    }

    protected abstract void initDataAndEvent();
    protected abstract @LayoutRes int getLayouId();
}
