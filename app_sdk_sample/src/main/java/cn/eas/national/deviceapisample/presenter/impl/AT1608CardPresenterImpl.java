package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.SyncCardError;
import cn.eas.national.deviceapisample.device.AT1608CardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IAT1608CardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class AT1608CardPresenterImpl implements IAT1608CardPresenter {
    private static final byte[] READ_PWD = new byte[]{ 0x01, 0x02, 0x03 };
    private static final byte[] WRITE_PWD = new byte[]{ 0x01, 0x02, 0x03 };
    private static final byte[] SC_PWD = new byte[]{ 0x01, 0x02, 0x03 };
    private static final int READ_LENGTH = 5;
    private static final int ADDRESS = 22;

    private IDeviceView view;
    private AT1608CardReaderImpl reader;

    public AT1608CardPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.reader = new AT1608CardReaderImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                AT1608CardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                AT1608CardPresenterImpl.this.view.displayInfo(info);
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
        byte[] result = reader.read(READ_PWD, ADDRESS, READ_LENGTH);
        if (result != null) {
            view.displayInfo("read success, result = " + ByteUtil.bytes2HexString(result));
        } else {
            view.displayInfo("read fail");
        }
    }

    @Override
    public void write() {
        int ret = reader.write(WRITE_PWD, ADDRESS, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 });
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

    @Override
    public void readCardStatus() {
        reader.readCardStatus();
    }

    @Override
    public void testIO() {
        int ret = reader.ioTest();
        if (ret == SyncCardError.SUCCESS) {
            view.displayInfo("io test success");
        } else {
            view.displayInfo("io test fail, error = " + ret);
        }
    }
}
