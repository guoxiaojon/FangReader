package com.example.jon.fangreader.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jon on 2017/1/4.
 */

public class DateUtil {
    public static String formatTime(String time){
        /**
         * "2017-01-03T02:18:08.784Z"
         * */
        String dateStr = time.substring(0,10);
        String timeStr = time.substring(11,19);
       // Log.d("data",dateStr+"||||"+timeStr);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dateStr+" "+timeStr);
            Date today = new Date();
            Logger.e(date.toString());
            Logger.e(today.toString());
            int year = today.getYear()-date.getYear();
            int month = today.getMonth()-date.getMonth();
            int day = today.getDay()-date.getDay();
            int hour = today.getHours()-date.getHours();
            int minute = today.getMinutes()-date.getMinutes();

            if(year>0){
                int temp = year*12+month;
                if(temp/12 >1){
                    return temp/12+"年前";
                }else {
                    return temp+"月前";
                }

            }else if(month>0){
                int temp = month*30+day;
                if(temp/30>1){
                    return temp/30+"月前";
                }
                return temp+"天前";
            }else if (day>0){
                int temp = day*24+hour;
                if(temp/24>0){
                    return temp/24+"天前";
                }else {
                    return temp+"小时前";
                }

            }else if(hour>0){
                int temp = hour*60+minute;
                if(temp/60>0){
                    return temp/60+"小时前";
                }else {
                    return temp+"分钟前";
                }
            }else if(minute>0){
                return minute+"分钟前";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return " ";
    }

    public static long getCurrentMillioneSceond(){
        return System.currentTimeMillis();
    }

}
