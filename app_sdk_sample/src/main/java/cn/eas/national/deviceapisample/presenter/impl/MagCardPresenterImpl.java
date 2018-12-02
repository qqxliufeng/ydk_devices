package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IMagCardDeviceView;
import cn.eas.national.deviceapisample.device.MagCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IMagCardPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class MagCardPresenterImpl implements IMagCardPresenter{
    private IMagCardDeviceView view;
    private MagCardReaderImpl magCardReader;

    public MagCardPresenterImpl(IMagCardDeviceView deviceView) {
        this.view = deviceView;
        this.magCardReader = new MagCardReaderImpl() {
            @Override
            protected void displayMagCardInfo(String cardInfo) {
                MagCardPresenterImpl.this.view.displayInfo(cardInfo);
                MagCardPresenterImpl.this.view.finishSwipeCard();
            }

            @Override
            protected void onDeviceServiceCrash() {
                MagCardPresenterImpl.this.view.displayInfo("device service crash");
                MagCardPresenterImpl.this.view.finishSwipeCard();
            }

            @Override
            protected void displayInfo(String info) {
                MagCardPresenterImpl.this.view.displayInfo(info);
                MagCardPresenterImpl.this.view.finishSwipeCard();
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void searchCard() {
        magCardReader.searchCard();
    }

    @Override
    public void stopSearch() {
        magCardReader.stopSearch();
    }
}
