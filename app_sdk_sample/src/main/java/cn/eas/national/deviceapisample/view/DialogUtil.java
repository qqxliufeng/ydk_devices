package cn.eas.national.deviceapisample.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Czl on 2017/8/30.
 */

public class DialogUtil {
    private static AlertDialog dialog;

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle(title);
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", listener);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.setMessage(message);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void hide() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
