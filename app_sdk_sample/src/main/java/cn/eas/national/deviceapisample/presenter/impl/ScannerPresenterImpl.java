package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.ScannerError;
import cn.eas.national.deviceapisample.device.ScannerImpl;
import cn.eas.national.deviceapisample.presenter.IScannerPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class ScannerPresenterImpl implements IScannerPresenter {
    private IDeviceView view;
    private ScannerImpl scanner;

    public ScannerPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.scanner = new ScannerImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                ScannerPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                ScannerPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void startBrScan() {
        scanner.startBrScan();
    }

    @Override
    public void stopBrScan() {
        scanner.stopBrScan();
    }

    @Override
    public void startScan() {
        int ret = scanner.open();
        if (ret != ScannerError.SUCCESS) {
            view.displayInfo("scanner open fail[" + ScannerImpl.getDescription(ret) + "]");
            return;
        }
        scanner.startScan();
    }
}
