package com.example.jon.fangreader.di.module;

import android.app.Activity;

import com.example.jon.fangreader.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jon on 2017/1/3.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity){
        this.mActivity = activity;
    }

    @PerActivity
    @Provides
    Activity provideActivity(){
        return mActivity;
    }
}
