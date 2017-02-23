package com.example.jon.fangreader.component;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jon.fangreader.model.http.Apis;

/**
 * Created by jon on 2017/1/4.
 */

public class ImageLoader {
    public static void load(Activity activity, String url, ImageView imageView){
        Glide.with(activity)
                .load(Apis.IMG_BASE_URL+url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(imageView);
    }
    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(Apis.IMG_BASE_URL+url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(imageView);
    }
}
