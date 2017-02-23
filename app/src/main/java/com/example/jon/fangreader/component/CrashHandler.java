
package com.example.jon.fangreader.component;

import android.content.Context;
import android.widget.Toast;

import com.example.jon.fangreader.app.App;
import com.orhanobut.logger.Logger;

/**
 * Created by jon on 2017/1/2.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static Thread.UncaughtExceptionHandler sDefaultUncaughtExceptionHandler;
    private static String TAG = CrashHandler.class.getSimpleName();

    private Context mContext;

    public CrashHandler(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Logger.e(TAG,throwable);
        sDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        Toast.makeText(mContext,"程序异常退出····",Toast.LENGTH_SHORT).show();
        App.getInstance().exitApp();
    }

    public static void init(CrashHandler crashHandler){
        sDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

    }
}
