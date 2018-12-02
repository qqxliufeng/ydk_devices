package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.device.CameraScannerImpl;
import cn.eas.national.deviceapisample.presenter.ICameraScannerPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class CameraScannerPresenterImpl implements ICameraScannerPresenter {
    private BaseDeviceActivity view;
    private CameraScannerImpl scanner;

    public CameraScannerPresenterImpl(BaseDeviceActivity activity) {
        this.view = activity;
        this.scanner = new CameraScannerImpl(activity) {
            @Override
            protected void onDeviceServiceCrash() {
                CameraScannerPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                CameraScannerPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void startScan(int cameraId) {
        scanner.startScan(view, cameraId);
    }
}
