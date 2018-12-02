package com.youdaike.checkticket.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by yuanxxx on 2017/1/10.
 */

public class DeviceUtil {
    private static final String TAG = "DeviceUtil";

    private static final String MODEL = "HDX";

    /**
     * 是否是好德芯设备
     *
     * @return
     */
    public static boolean isHDXDevice() {
        String model = Build.MODEL;
        Log.i(TAG, "isHDXDevice: model=" + model);
        if (!TextUtils.isEmpty(model) && model.startsWith(MODEL)) {
            return true;
        }
        return false;
    }
}
