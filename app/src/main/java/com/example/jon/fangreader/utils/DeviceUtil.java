package com.example.jon.fangreader.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by jon on 2017/1/2.
 */

public class DeviceUtil {
    /**
     * 获取设备号
     * */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        return IMEI;
    }
}
