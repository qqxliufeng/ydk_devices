package cn.eas.national.deviceapisample.presenter.impl;

import android.os.Handler;
import android.os.Looper;

import com.landicorp.android.eptapi.device.RFCardReader;
import com.landicorp.android.eptapi.exception.RequestException;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.LedImpl;
import cn.eas.national.deviceapisample.presenter.ILedPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class LedPresenterImpl implements ILedPresenter {
    private IDeviceView view;
    private LedImpl led;
    private int[] flashList = new int[] { 0x01, 0x02, 0x04, 0x08, 0x03, 0x05, 0x09, 0x06, 0x0A, 0x0C, 0x07, 0x0D, 0x0E, 0x0F };
    private int current = 0;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private Runnable flashRunnable = new Runnable() {
        @Override
        public void run() {
            flash();
        }
    };

    public LedPresenterImpl(IDeviceView deviceView, String deviceName) {
        this.view = deviceView;
        this.led = new LedImpl(deviceName) {
            @Override
            protected void onDeviceServiceCrash() {
                LedPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                LedPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void flash() {
        if (current == flashList.length) {
            current = 0;
        }
        int value = flashList[current];
        if ((value & 0x01) == 0x01) {
            led.turnOn(RFCardReader.LED_BLUE);
        } else {
            led.turnOff(RFCardReader.LED_BLUE);
        }
        if ((value & 0x02) == 0x02) {
            led.turnOn(RFCardReader.LED_YELLOW);
        } else {
            led.turnOff(RFCardReader.LED_YELLOW);
        }
        if ((value & 0x04) == 0x04) {
            led.turnOn(RFCardReader.LED_GREEN);
        } else {
            led.turnOff(RFCardReader.LED_GREEN);
        }
        if ((value & 0x08) == 0x08) {
            led.turnOn(RFCardReader.LED_RED);
        } else {
            led.turnOff(RFCardReader.LED_RED);
        }
        current++;
        uiHandler.postDelayed(flashRunnable, 500);
    }

    @Override
    public void stop() {
        uiHandler.removeCallbacks(flashRunnable);
        led.turnOff(RFCardReader.LED_BLUE);
        led.turnOff(RFCardReader.LED_YELLOW);
        led.turnOff(RFCardReader.LED_GREEN);
        led.turnOff(RFCardReader.LED_RED);
        try {
            RFCardReader.getInstance().halt();
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
}
