package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.RFCpuCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IRFCpuCardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class RFCpuCardPresenterImpl implements IRFCpuCardPresenter {
    private IDeviceView view;
    private RFCpuCardReaderImpl rfCardReader;

    public RFCpuCardPresenterImpl(IDeviceView deviceView, String deviceName) {
        this.view = deviceView;
        this.rfCardReader = new RFCpuCardReaderImpl(deviceName) {
            @Override
            protected void onDeviceServiceCrash() {
                RFCpuCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                RFCpuCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void cardPower() {
        rfCardReader.cardPower();
    }

    @Override
    public void cardHalt() {
        rfCardReader.cardHalt();
    }

    @Override
    public void exchangeApdu() {
        final String APDU_DATA = "00A4040008A000000333010101";
        view.displayInfo("apdu = " + APDU_DATA);
        rfCardReader.exchangeApdu(ByteUtil.hexString2Bytes(APDU_DATA));
    }

    @Override
    public void exist() {
        boolean exist = rfCardReader.exist();
        if (exist) {
            view.displayInfo("card exist");
        } else {
            view.displayInfo("card not exist");
        }
    }
}
