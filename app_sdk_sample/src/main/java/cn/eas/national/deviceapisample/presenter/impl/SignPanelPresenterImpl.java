package cn.eas.national.deviceapisample.presenter.impl;

import android.os.Handler;
import android.os.Looper;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.SignPanelImpl;
import cn.eas.national.deviceapisample.presenter.ISignPanelPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class SignPanelPresenterImpl implements ISignPanelPresenter {
    private IDeviceView view;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private SignPanelImpl signPanel;

    public SignPanelPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.signPanel = new SignPanelImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                SignPanelPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                SignPanelPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                toast(msg);
            }
        };
    }

    @Override
    public void startSign() {
        signPanel.startSign();
    }

}
