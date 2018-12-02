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

package com.youdaike.checkticket;

import android.app.Application;

import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.utils.DeviceUtil;
import com.youdaike.checkticket.utils.PreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import hdx.HdxUtil;


public class YDKApplication extends Application {

    private SerialPort mSerialPort = null;
    private static YDKApplication mApplication;

    public static synchronized YDKApplication getInstance() {
        return mApplication;
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            //SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
            if (PreferencesUtil.getBoolean(this, StringContract.ISHDXDEVICE, false)) {
                String path = HdxUtil.GetPrinterPort();
                //dev/ttyS3
                int baudrate = 115200;//Integer.decode(sp.getString("BAUDRATE", "-1"));

                /* Open the serial port */
                mSerialPort = new SerialPort(new File(path), baudrate, 0);
            }
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        if (DeviceUtil.isHDXDevice()) {
            PreferencesUtil.putBoolean(this, StringContract.ISHDXDEVICE, true);
        }
    }
}
