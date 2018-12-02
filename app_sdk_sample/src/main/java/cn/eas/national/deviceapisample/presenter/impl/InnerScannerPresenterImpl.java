package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.InnerScannerImpl;
import cn.eas.national.deviceapisample.presenter.IInnerScannerPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class InnerScannerPresenterImpl implements IInnerScannerPresenter{
    private IDeviceView view;
    private InnerScannerImpl scanner;

    public InnerScannerPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.scanner = new InnerScannerImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                InnerScannerPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                InnerScannerPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void startScan(int timeout) {
        scanner.startScan(timeout);
    }

    @Override
    public void stopScan() {
        scanner.stopScan();
    }
}
