package com.example.jon.fangreader.di.component;

import android.content.Context;

import com.example.jon.fangreader.di.module.AppModule;
import com.example.jon.fangreader.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jon on 2017/1/3.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getApp();
    RetrofitHelper getRetrofitHelper();
}
