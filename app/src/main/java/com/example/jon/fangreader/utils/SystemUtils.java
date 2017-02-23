package com.example.jon.fangreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.jon.fangreader.app.App;

/**
 * Created by jon on 2017/1/4.
 */

public class SystemUtils {
    public static boolean isNetAvailable(){
        ConnectivityManager manager = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
    }
    public static float dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return  (dp * scale + 0.5f);
    }
    public static int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(sp * fontScale + 0.5f);
    }

    public static int getScreenWidth(){
        return App.getInstance().getResources().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight(){
        return App.getInstance().getResources().getDisplayMetrics().heightPixels;
    }
}
