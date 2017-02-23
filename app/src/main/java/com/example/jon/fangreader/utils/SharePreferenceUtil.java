package com.example.jon.fangreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jon.fangreader.app.App;
import com.example.jon.fangreader.app.Constants;

/**
 * Created by jon on 2017/1/3.
 */

public class SharePreferenceUtil {



    private static SharedPreferences getAppSP(){
        return App.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    }
    public static String getGender(){
        return getAppSP().getString(Constants.SP_GENDER,Constants.TYPE_MALE);

    }

    public static void setGender(String gender){
        getAppSP().edit().putString(Constants.SP_GENDER,gender).apply();
    }

    public static void saveReadStatus(String bookId,int[] readStatus){
        getAppSP().edit().putInt(Constants.SP_STATUS_CHAPTER+bookId,readStatus[0]).apply();
        getAppSP().edit().putInt(Constants.SP_STATUS_BEGIN+bookId,readStatus[1]).apply();
        getAppSP().edit().putInt(Constants.SP_STATUS_END+bookId,readStatus[2]).apply();

    }
    public static int[] getReadStatus(String bookId){
        int[] retVal = new int[3];
        retVal[0] = getAppSP().getInt(Constants.SP_STATUS_CHAPTER+bookId,0);
        retVal[1] = getAppSP().getInt(Constants.SP_STATUS_BEGIN+bookId,0);
        retVal[2] = getAppSP().getInt(Constants.SP_STATUS_END+bookId,0);
        return retVal;
    }

    public static void setMode(int id){
        getAppSP().edit().putInt(Constants.SP_MODE,id).apply();
    }
    public static int getMode(){
        return getAppSP().getInt(Constants.SP_MODE,0);
    }
    public static void setNight(boolean isNight){
        getAppSP().edit().putBoolean(Constants.SP_ISNIGHT,isNight).apply();
    }
    public static boolean isNight(){
        return getAppSP().getBoolean(Constants.SP_ISNIGHT,false);
    }

    public static void setAutoBright(boolean isAuto){
        getAppSP().edit().putBoolean(Constants.SP_AUTOBIRGHT,isAuto).apply();
    }
    public static boolean isAutoBirght(){
        return getAppSP().getBoolean(Constants.SP_AUTOBIRGHT,true);
    }
    public static void setUseVolume(boolean useVolume){
        getAppSP().edit().putBoolean(Constants.SP_USEVOLUME,useVolume).apply();
    }
    public static boolean isUseVolume(){
        return getAppSP().getBoolean(Constants.SP_USEVOLUME,true);
    }
    public static void setTextSize(int size){
        getAppSP().edit().putInt(Constants.SP_TEXTSIZE,size).apply();
    }
    public static int getTextSize(){
        Log.d("data2","getTextSize() "+getAppSP().getInt(Constants.SP_TEXTSIZE,SystemUtils.sp2px(App.getInstance().getApplicationContext(),6)));
        return getAppSP().getInt(Constants.SP_TEXTSIZE,SystemUtils.sp2px(App.getInstance().getApplicationContext(),6));
    }
    public static void setBirght(int value){
        getAppSP().edit().putInt(Constants.SP_BIRGHT,value).apply();
    }
    public static int getBirght(){
        return getAppSP().getInt(Constants.SP_BIRGHT,30);
    }

    public static void setNoImage(boolean noImage){
        getAppSP().edit().putBoolean(Constants.SP_NOIMAGE,noImage).apply();
    }
    public static boolean isNoImage(){
        return getAppSP().getBoolean(Constants.SP_NOIMAGE,false);
    }
}
