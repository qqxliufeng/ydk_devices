package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.ICCpuCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IICCpuCardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class ICCpuCardPresenterImpl implements IICCpuCardPresenter {
    private IDeviceView view;
    private ICCpuCardReaderImpl icCardReader;

    public ICCpuCardPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.view = view;
        this.icCardReader = new ICCpuCardReaderImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                ICCpuCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                ICCpuCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void cardPower() {
        icCardReader.cardPower();
    }

    @Override
    public void cardHalt() {
        icCardReader.cardHalt();
        view.displayInfo("card halt");
    }

    @Override
    public void exist() {
        boolean exist = icCardReader.exist();
        if (exist) {
            view.displayInfo("card exist");
        } else {
            view.displayInfo("card not exist");
        }
    }

    @Override
    public void exchangeApdu() {
        final String APDU_DATA = "00A4040008A000000333010101";
        view.displayInfo("apdu = " + APDU_DATA);
        icCardReader.exchangeApdu(ByteUtil.hexString2Bytes(APDU_DATA));
    }
}
