package cn.eas.national.deviceapisample.presenter.impl;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.data.SerialPortError;
import cn.eas.national.deviceapisample.device.SerialPortImpl;
import cn.eas.national.deviceapisample.presenter.ISerialPortPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class SerialPortPresenterImpl implements ISerialPortPresenter {
    private IDeviceView view;
    private SerialPortImpl serialPort;

    public SerialPortPresenterImpl(IDeviceView deviceView) {
        this.view = deviceView;
        this.serialPort = new SerialPortImpl(Constants.SerialPort.DEVICE_USBD) {
            @Override
            protected void onDeviceServiceCrash() {
                SerialPortPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                SerialPortPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public int init(int baud, int parity, int dataBits) {
        int ret = serialPort.init(baud, parity, dataBits);
        if (ret == SerialPortError.SUCCESS) {
            view.displayInfo("init success");
        } else {
            view.displayInfo("init fail, ret = " + ret);
        }
        return ret;
    }

    @Override
    public int open() {
        int ret = serialPort.open();
        if (ret == SerialPortError.SUCCESS) {
            view.displayInfo("open success");
        } else {
            view.displayInfo("open fail, ret = " + ret);
        }
        return ret;
    }

    @Override
    public int read(byte[] buffer) {
        int ret = serialPort.read(buffer, 1 * 1000);
        if (ret > 0) {
            byte[] data = new byte[ret];
            System.arraycopy(buffer, 0, data, 0, ret);
            view.displayInfo("read success, len = " + ret + ", data = " + ByteUtil.bytes2HexString(data));
        } else {
            view.displayInfo("read fail");
        }
        return ret;
    }

    @Override
    public int write(byte[] data) {
        int ret = serialPort.write(data, 1 * 1000);
        if (ret > 0) {
            view.displayInfo("write success, write len = " + ret);
        } else {
            view.displayInfo("write fail");
        }
        return 0;
    }

    @Override
    public int close() {
        int ret = serialPort.close();
        if (ret == SerialPortError.SUCCESS) {
            view.displayInfo("close success");
        } else {
            view.displayInfo("close fail, ret = " + ret);
        }
        return ret;
    }
}
