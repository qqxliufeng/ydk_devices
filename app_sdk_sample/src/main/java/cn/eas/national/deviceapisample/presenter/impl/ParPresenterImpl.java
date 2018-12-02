package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.ParManagerImpl;
import cn.eas.national.deviceapisample.presenter.IParPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class ParPresenterImpl implements IParPresenter {
    private IDeviceView view;
    private ParManagerImpl parManager;

    public ParPresenterImpl(IDeviceView deviceView, String moduleName, String fileName) {
        this.view = deviceView;
        this.parManager = new ParManagerImpl(deviceView, moduleName, fileName) {
            @Override
            protected void onDeviceServiceCrash() {
                ParPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                ParPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void isExist() {
        parManager.isExist();
    }

    @Override
    public void isFirstRun() {
        parManager.isFirstRun();
    }

    @Override
    public void read(int keyType, String key) {
        parManager.readParam(keyType, key);
    }

    @Override
    public void write(int keyType, String key, Object value) {
        parManager.writeParam(keyType, key, value);
    }
}
