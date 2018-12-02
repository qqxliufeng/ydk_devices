package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.device.C10DigledDeivceImpl;
import cn.eas.national.deviceapisample.presenter.IC10DigledDevicePresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class C10DigledDevicePresenterImpl implements IC10DigledDevicePresenter {
    private BaseDeviceActivity view;
    private C10DigledDeivceImpl digledDevice;

    public C10DigledDevicePresenterImpl(BaseDeviceActivity deviceView) {
        this.view = deviceView;
        this.digledDevice = new C10DigledDeivceImpl(deviceView) {
            @Override
            protected void onDeviceServiceCrash() {
                C10DigledDevicePresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                C10DigledDevicePresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void display() {
        digledDevice.display();
    }

    @Override
    public void clear() {
        digledDevice.clear();
    }

    @Override
    public void flashLight() {
        digledDevice.flashLight();
    }

    @Override
    public void stopFlash() {
        digledDevice.stopFlash();
    }

    @Override
    public void getDigledInfo() {
        digledDevice.getDigledInfo();
    }

}
