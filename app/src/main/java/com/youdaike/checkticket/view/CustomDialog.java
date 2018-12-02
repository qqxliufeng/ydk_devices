package com.youdaike.checkticket.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.utils.ScreenUtil;

/**
 * Created by yuanxxx on 2016/12/7.
 */

public class CustomDialog extends Dialog {
    private Context mContext;
    private View mRootView;

    public CustomDialog(Context context) {
        super(context, R.style.progress_dialog);
        mContext = context;
        init();
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, R.style.progress_dialog);
        mContext = context;
        init();
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_custom_dialog, null);
        setContentView(mRootView);
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        int width = ScreenUtil.dpToPxInt(mContext, 300);
        int height = ScreenUtil.dpToPxInt(mContext, 150);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        dialogWindow.setAttributes(lp);
    }

    private CustomDialog setMessage(String message) {
        TextView text = (TextView) mRootView.findViewById(R.id.view_custom_dialog_text);
        text.setText(message);
        return this;
    }

    public static CustomDialog show(Context context, String message) {
        CustomDialog customDialog = new CustomDialog(context);
        customDialog.setMessage(message);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        return customDialog;
    }
}
