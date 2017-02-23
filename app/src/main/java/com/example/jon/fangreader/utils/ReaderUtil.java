package com.example.jon.fangreader.utils;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.app.App;

/**
 * Created by jon on 2017/2/8.
 */

public class ReaderUtil {

    public static int NIGHT_BAKCGROUND = 4;

    public static Bitmap getBackground(int id){
        Bitmap bitmap = Bitmap.createBitmap(SystemUtils.getScreenWidth(),SystemUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        switch (id){
            case 0:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_yellow));
                break;
            case 1:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_gray));
                break;
            case 2:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_green));
                break;
            case 3:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_white));
                break;
            case 4:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_night));
                break;
            default:
                bitmap.eraseColor(ContextCompat.getColor(App.getInstance().getApplicationContext(), R.color.read_theme_yellow));
        }
        return bitmap;

    }
}
