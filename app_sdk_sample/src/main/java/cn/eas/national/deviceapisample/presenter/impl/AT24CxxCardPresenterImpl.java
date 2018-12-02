package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.SyncCardError;
import cn.eas.national.deviceapisample.device.AT24CxxCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IAT24CxxCardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class AT24CxxCardPresenterImpl implements IAT24CxxCardPresenter {
    private static final int READ_LENGTH = 5;
    private static final int ADDRESS = 22;

    private IDeviceView view;
    private AT24CxxCardReaderImpl reader;

    public AT24CxxCardPresenterImpl(IDeviceView deviceView, int cardType) {
        this.view = deviceView;
        this.reader = new AT24CxxCardReaderImpl(cardType) {
            @Override
            protected void onDeviceServiceCrash() {
                AT24CxxCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                AT24CxxCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void cardPower() {
        reader.cardPower();;
    }

    @Override
    public void cardHalt() {
        reader.cardHalt();
    }

    @Override
    public void read() {
        byte[] result = reader.read(ADDRESS, READ_LENGTH);
        if (result != null) {
            view.displayInfo("read success, result = " + ByteUtil.bytes2HexString(result));
        } else {
            view.displayInfo("read fail");
        }
    }

    @Override
    public void write() {
        int ret = reader.write(ADDRESS, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 });
        if (ret == SyncCardError.SUCCESS) {
            view.displayInfo("write success");
        } else {
            view.displayInfo("write fail, ret = " + ret);
        }
    }
}
