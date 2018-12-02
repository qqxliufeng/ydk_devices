package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.PsamCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IPsamCardPresenter;

/**
 * Created by Czl on 2017/7/23.
 */

public class PsamCardPresenterImpl implements IPsamCardPresenter {
    /** 卡座 */
    private static final int SLOT = 1;

    private IDeviceView view;
    private PsamCardReaderImpl psamCardReader;

    public PsamCardPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.psamCardReader = new PsamCardReaderImpl(SLOT) {
            @Override
            protected void onDeviceServiceCrash() {
                PsamCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                PsamCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void cardPower(int slot) {
        view.displayInfo("card power");
        this.psamCardReader = new PsamCardReaderImpl(slot) {
            @Override
            protected void onDeviceServiceCrash() {
                PsamCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                PsamCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
        psamCardReader.cardPower();
    }

    @Override
    public void cardHalt() {
        psamCardReader.cardHalt();
        view.displayInfo("card halt");
    }

    @Override
    public void exist() {
        boolean exist = psamCardReader.exist();
        if (exist) {
            view.displayInfo("card exist");
        } else {
            view.displayInfo("card not exist");
        }
    }

    @Override
    public void exchangeApdu() {
        byte[] apdu = new byte[] { 0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x3F, 0x00 };
        psamCardReader.exchangeApdu(apdu);
    }
}
