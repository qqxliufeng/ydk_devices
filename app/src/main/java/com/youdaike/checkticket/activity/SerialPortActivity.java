/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.youdaike.checkticket.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.YDKApplication;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.utils.DeviceUtil;
import com.youdaike.checkticket.utils.PreferencesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public abstract class SerialPortActivity extends Activity {
    private static final String TAG = "SerialPortActivity";
    protected YDKApplication mApplication;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    protected ReadThread mReadThread;
    //private static final String TAG = "SerialPortActivity";
    private int n = 0;

    class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size, n);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = YDKApplication.getInstance();
        if (PreferencesUtil.getBoolean(this, StringContract.ISHDXDEVICE, false)) {
            try {
                mSerialPort = mApplication.getSerialPort();
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (SecurityException e) {
                //DisplayError(R.string.error_security);
            } catch (IOException e) {
                //DisplayError(R.string.error_unknown);
            } catch (InvalidParameterException e) {
                //DisplayError(R.string.error_configuration);
            }
        }
    }

    protected abstract void onDataReceived(final byte[] buffer, final int size, final int n);

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
     *
     * @return
     */
    protected String getIMEI() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        mApplication.closeSerialPort();
        mSerialPort = null;
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }
        } catch (IOException e) {
        }
        super.onDestroy();
    }
}
