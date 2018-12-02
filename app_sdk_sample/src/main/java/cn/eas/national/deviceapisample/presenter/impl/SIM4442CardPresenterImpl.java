package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.SyncCardError;
import cn.eas.national.deviceapisample.device.SIM4442CardReaderImpl;
import cn.eas.national.deviceapisample.presenter.ISIMCardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class SIM4442CardPresenterImpl implements ISIMCardPresenter {
    private static final byte[] SC_PWD = new byte[]{ 0x01, 0x02, 0x03 };
    private static final int READ_LENGTH = 5;
    private static final int ADDRESS = 22;

    private IDeviceView view;
    private SIM4442CardReaderImpl reader;

    public SIM4442CardPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.reader = new SIM4442CardReaderImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                SIM4442CardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                SIM4442CardPresenterImpl.this.view.displayInfo(info);
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
        byte[] result = reader.read(SC_PWD, ADDRESS, READ_LENGTH);
        if (result != null) {
            view.displayInfo("read success, result = " + ByteUtil.bytes2HexString(result));
        } else {
            view.displayInfo("read fail");
        }
    }

    @Override
    public void write() {
        int ret = reader.write(SC_PWD, ADDRESS, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 });
        if (ret == SyncCardError.SUCCESS) {
            view.displayInfo("write success");
        } else {
            view.displayInfo("write fail, ret = " + ret);
        }
    }

    @Override
    public void changePassword() {
        int ret = reader.changePassword(SC_PWD, new byte[] { 0x06, 0x05, 0x04, 0x03, 0x02, 0x01 });
        if (ret == SyncCardError.SUCCESS) {
            view.displayInfo("change password success");
        } else {
            view.displayInfo("change password fail, ret = " + ret);
        }
    }
}
