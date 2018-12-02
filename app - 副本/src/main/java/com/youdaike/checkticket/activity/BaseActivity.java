package com.youdaike.checkticket.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.youdaike.checkticket.R;

/**
 * Created by yuanxx on 16/7/19.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final Resources res = getResources();
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_F1:
                    Log.i(TAG, "F1");
                    break;
                case KeyEvent.KEYCODE_F2:
                    Log.i(TAG, "F2");
                    break;
                case KeyEvent.KEYCODE_F3:
                    Log.i(TAG, "F3");
                    break;
                case KeyEvent.KEYCODE_F12:
                    Log.i(TAG, res.getString(R.string.scan_left));
                    break;
                case KeyEvent.KEYCODE_F11:
                    Log.i(TAG, res.getString(R.string.scan));
                    break;
                case KeyEvent.KEYCODE_CALL:
                    Log.i(TAG, res.getString(R.string.call));
                    break;
                case KeyEvent.KEYCODE_ENDCALL:
                    Log.i(TAG, res.getString(R.string.end_call));
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    Log.i(TAG, res.getString(R.string.up));
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Log.i(TAG, res.getString(R.string.down));
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    Log.i(TAG, res.getString(R.string.enter));
                    break;
                case KeyEvent.KEYCODE_HOME:
                    Log.i(TAG, res.getString(R.string.home));
                    break;
                case KeyEvent.KEYCODE_DEL:
                    Log.i(TAG, res.getString(R.string.del));
                    break;
                case KeyEvent.KEYCODE_0:
                    Log.i(TAG, "0");
                    break;
                case KeyEvent.KEYCODE_1:
                    Log.i(TAG, "1");
                    break;
                case KeyEvent.KEYCODE_2:
                    Log.i(TAG, "2");
                    break;
                case KeyEvent.KEYCODE_3:
                    Log.i(TAG, "3");
                    break;
                case KeyEvent.KEYCODE_4:
                    Log.i(TAG, "4");
                    break;
                case KeyEvent.KEYCODE_5:
                    Log.i(TAG, "5");
                    break;
                case KeyEvent.KEYCODE_6:
                    Log.i(TAG, "6");
                    break;
                case KeyEvent.KEYCODE_7:
                    Log.i(TAG, "7");
                    break;
                case KeyEvent.KEYCODE_8:
                    Log.i(TAG, "8");
                    break;
                case KeyEvent.KEYCODE_9:
                    Log.i(TAG, "9");
                    break;
                case KeyEvent.KEYCODE_POUND:
                    Log.i(TAG, "#");
                    break;
                case KeyEvent.KEYCODE_STAR:
                    Log.i(TAG, "*");
                    break;
                case KeyEvent.KEYCODE_PERIOD:
                    Log.i(TAG, ".");
                    break;
                case KeyEvent.KEYCODE_BACK:
                    Log.i(TAG, res.getString(R.string.back));
                    return super.dispatchKeyEvent(event);
                case KeyEvent.KEYCODE_ESCAPE:
                    Log.i(TAG, res.getString(R.string.esc));
                    break;
                case KeyEvent.KEYCODE_POWER:
                    Log.i(TAG, res.getString(R.string.power));
                    break;
                case KeyEvent.KEYCODE_F10:
                    Log.i(TAG, "fn");
                    break;
                case KeyEvent.KEYCODE_F9:
                    Log.i(TAG, res.getString(R.string.letter));
                    break;
                case KeyEvent.KEYCODE_CAMERA:
                    Log.i(TAG, res.getString(R.string.camera));
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    Log.i(TAG, res.getString(R.string.vol_dec));
                    break;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    Log.i(TAG, res.getString(R.string.vol_inc));
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 获取设备IMEI
     * @return
     */
    protected String getIMEI() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}
