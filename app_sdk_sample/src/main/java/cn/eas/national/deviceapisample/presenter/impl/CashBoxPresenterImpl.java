package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.CashBoxImpl;
import cn.eas.national.deviceapisample.presenter.ICashBoxPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class CashBoxPresenterImpl implements ICashBoxPresenter {
    private IDeviceView view;
    private CashBoxImpl cashBox;

    public CashBoxPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.cashBox = new CashBoxImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                CashBoxPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                CashBoxPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void open() {
        cashBox.open();
    }
}
