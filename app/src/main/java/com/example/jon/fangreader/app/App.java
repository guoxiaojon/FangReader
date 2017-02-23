package com.example.jon.fangreader.app;

import android.app.Activity;
import android.app.Application;
import android.os.Process;

import com.example.jon.fangreader.component.CrashHandler;
import com.example.jon.fangreader.di.component.AppComponent;
import com.example.jon.fangreader.di.component.DaggerAppComponent;
import com.example.jon.fangreader.di.module.AppModule;
import com.example.jon.fangreader.widget.AppBlockCanaryContext;
import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jon on 2017/1/2.
 */

public class App extends Application {
    private static App sInstance;
    private Set<Activity> mActivities;
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mActivities = new HashSet<Activity>();
        sInstance = this;

        //Logger
        Logger.init(getPackageName()).hideThreadInfo();

        //CrashHandler
        CrashHandler.init(new CrashHandler(this));

        //初始化过度绘制检查工具
        BlockCanary.install(this, new AppBlockCanaryContext()).start();


        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent(){
        return mAppComponent;
    }

    public static App getInstance() {
        return sInstance;
    }

    public void addActivity(Activity activity) {
        if (mActivities != null) {
            mActivities.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (mActivities != null) {
            mActivities.remove(activity);
        }
    }

    public void exitApp() {
        synchronized (mActivities) {
            if (mActivities != null) {
                for (Activity activity : mActivities) {
                    activity.finish();
                }
            }
            mActivities = null;
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
