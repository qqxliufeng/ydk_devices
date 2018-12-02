package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.IDCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IIDCardPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class IDCardPresenterImpl implements IIDCardPresenter{
    private IDeviceView view;
    private IDCardReaderImpl reader;

    public IDCardPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.reader = new IDCardReaderImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                IDCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                IDCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void searchCard() {
        reader.searchCard();
    }

    @Override
    public void stopSearch() {
        reader.stopSearch();
    }
}
