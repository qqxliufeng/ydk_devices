package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.device.C10SubScreenDeivceImpl;
import cn.eas.national.deviceapisample.presenter.IC10SubScreenDevicePresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class C10SubScreenDevicePresenterImpl implements IC10SubScreenDevicePresenter {
    private BaseDeviceActivity view;
    private C10SubScreenDeivceImpl subscreenDeivce;

    public C10SubScreenDevicePresenterImpl(BaseDeviceActivity deviceView) {
        this.view = deviceView;
        this.subscreenDeivce = new C10SubScreenDeivceImpl(deviceView) {
            @Override
            protected void onDeviceServiceCrash() {
                C10SubScreenDevicePresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                C10SubScreenDevicePresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void connect() {
        subscreenDeivce.connect();
    }

    @Override
    public void startAppOnSubScreen() {
        subscreenDeivce.startAppOnSubScreen();
    }

    @Override
    public void startActivityOnSubScreen() {
        subscreenDeivce.startActivityOnSubScreen();
    }

    @Override
    public void setSubScreenApp() {
        subscreenDeivce.setSubScreenApp();
    }

    @Override
    public void removeSubScreenApp() {
        subscreenDeivce.removeSubScreenApp();
    }

    @Override
    public void getSubScreenInfo() {
        subscreenDeivce.getSubScreenInfo();
    }

    @Override
    public void sendDataToSubScreen() {
        int ret = subscreenDeivce.sendData();
        if (ret != 0) {
            view.displayInfo("send data fail[ret = " + ret + "]");
        } else {
            view.displayInfo("send data success");
        }
    }

    @Override
    public void disconnect() {
        subscreenDeivce.disconnect();
    }
}
