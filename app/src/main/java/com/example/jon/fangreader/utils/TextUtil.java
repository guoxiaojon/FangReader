package com.example.jon.fangreader.utils;

/**
 * Created by jon on 2017/2/22.
 */

public class TextUtil {
    public static String formatNum(int wordCount){

            if (wordCount / 10000 > 0) {
                return (int) ((wordCount / 10000f) + 0.5) + "万字";
            } else if (wordCount / 1000 > 0) {
                return (int) ((wordCount / 1000f) + 0.5) + "千字";
            } else {
                return wordCount + "字";
            }

    }
}
