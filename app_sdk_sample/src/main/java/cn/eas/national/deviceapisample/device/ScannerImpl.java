package cn.eas.national.deviceapisample.device;

import android.util.Log;

import com.landicorp.android.eptapi.device.BRScanner;
import com.landicorp.android.eptapi.device.Scanner;

/**
 * 该模块针对外接扫码枪设备。
 */
public abstract class ScannerImpl extends BaseDevice {
    private static final String TAG = "ScannerImpl";

    private BRScanner brScanner = BRScanner.getInstance();
    private BRScanner.OnScanListener brScanListener = new BRScanner.OnScanListener() {
        @Override
        public void onScanSuccess(byte[] code) {
            displayInfo("scan result[" + new String(code) + "]");
        }

        @Override
        public void onScanFail(int error) {
            displayInfo("scan fail[error = " + error + "]");
        }

        @Override
        public void onCrash() {
            onDeviceServiceCrash();
        }
    };
    private Scanner scanner = new Scanner("");
    private Scanner.OnScanListener scanListener = new Scanner.OnScanListener() {
        @Override
        public void onScanSuccess(String code) {
            displayInfo("scan success[" + code + "]");
            runOnUi(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }

        @Override
        public void onScanFail(int error) {
            displayInfo("scan fail[error = " + error + "]");
            runOnUi(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }

        @Override
        public void onCrash() {
            onDeviceServiceCrash();
        }
    };

    public ScannerImpl() {
    }

    public void startBrScan() {
        Log.d(TAG, "// startBrScan | ");
        boolean result = brScanner.startScan(brScanListener);
        if (!result) {
            displayInfo("start BR200 Scanner fail");
        }
    }

    public void stopBrScan() {
        boolean result = brScanner.stopScan();
        if (!result) {
            displayInfo("stop BR200 Scanner fail");
        }
    }

    public int open() {
        scanner.setOnScanListener(scanListener);
        return scanner.open();
    }

    public void startScan() {
        scanner.scan();
    }

    private void close() {
        scanner.close();
    }

    public static String getDescription(int error) {
        switch (error) {
            case Scanner.ERROR_DEVICE_NOT_EXIST:
                return "设备不存在";
            case Scanner.ERROR_IS_ALEADY_OPEN:
                return "设备已经打开";
            case Scanner.ERROR_NONE:
                return "成功";
            case Scanner.ERROR_FAIL:
            default:
                return "其他错误";
        }
    }

    private void runOnUi(Runnable runnable) {
        uiHandler.post(runnable);
    }
}
