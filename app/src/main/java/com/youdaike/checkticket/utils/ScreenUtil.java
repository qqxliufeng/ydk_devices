package com.youdaike.checkticket.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by yuanxx on 2016/12/11.
 */

public class ScreenUtil {
    public final static int WIDTH_1080 = 1080;
    public static int mScreenWidth = 0;

    /**
     * dp转px  返回float类型
     *
     * @param context 上下文环境
     * @param dp      要转换的dp值
     * @return float类型
     */
    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * dp转px  返回int类型
     *
     * @param context
     * @param dp      要转换的dp值
     * @return int类型
     */
    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文环境
     * @param spValue 要转换的spValue值
     * @return int类型
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文环境
     * @param px      要转换的px值
     * @return float类型
     */
    public static float pxToDp(Context context, float px) {
        if (context == null || px == 0) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * px转dp
     *
     * @param context 上下文环境
     * @param px      要转换的px值
     * @return int类型
     */
    public static int pxToDpInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    /**
     * 获取 status_bar_height 的高度
     *
     * @param
     * @return int类型
     */
    public static int getStatusBarHeight(Context mContext) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = mContext.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context 上下文环境
     * @return int类型
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context 上下文环境
     * @return int类型
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        if (mScreenWidth == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metric = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metric);
            mScreenWidth = metric.heightPixels;
        }
        return mScreenWidth;
    }


}
