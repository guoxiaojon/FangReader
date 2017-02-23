package com.example.jon.fangreader.di.module;

import android.content.Context;

import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.model.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jon on 2017/1/3.
 */
@Module
public class AppModule {
    private App mApp;

    public AppModule(App context){
        this.mApp = context;
    }

    @Singleton
    @Provides
    RetrofitHelper provideRetrofitHelper(){
        return new RetrofitHelper();
    }

    @Singleton
    @Provides
    Context provideApp(){
        return mApp;
    }

}
