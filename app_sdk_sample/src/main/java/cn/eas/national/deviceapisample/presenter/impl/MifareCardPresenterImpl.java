package cn.eas.national.deviceapisample.presenter.impl;

import com.landicorp.android.eptapi.utils.BytesBuffer;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.data.RFCardError;
import cn.eas.national.deviceapisample.device.MifareCardReaderImpl;
import cn.eas.national.deviceapisample.presenter.IMifareCardPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class MifareCardPresenterImpl implements IMifareCardPresenter {
    private IDeviceView view;
    private MifareCardReaderImpl mifareCardReader;

    public MifareCardPresenterImpl(IDeviceView deviceView, String deviceName) {
        this.view = deviceView;
        this.mifareCardReader = new MifareCardReaderImpl(deviceName) {
            @Override
            protected void onDeviceServiceCrash() {
                MifareCardPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                MifareCardPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void cardPower() {
        mifareCardReader.cardPower();
    }

    @Override
    public void cardHalt() {
        mifareCardReader.cardHalt();
    }

    @Override
    public void authBlock(int blockNo, int keyType, byte[] key) {
        view.displayInfo("auth card[blockNo = " + blockNo + ", keyType = " + keyType
                + ", key = " + ByteUtil.bytes2HexString(key) + "]");
        int ret = mifareCardReader.auth(blockNo, keyType, key);
        if (ret == RFCardError.SUCCESS) {

            view.displayInfo("auth success!");
        } else {
            view.displayInfo("auth fail, error = " + ret);
        }
    }

    @Override
    public void read(int blockNo, BytesBuffer buffer) {
        view.displayInfo("read card[blockNo = " + blockNo + "]");
        int ret = mifareCardReader.read(blockNo, buffer);
        if (ret == RFCardError.SUCCESS) {
            byte[] data = buffer.getData();
            view.displayInfo("read result[" + ByteUtil.bytes2HexString(data) + "]");
        } else {
            view.displayInfo("read fail, error = " + ret);
        }
    }

    @Override
    public void write(int blockNo, byte[] data) {
        view.displayInfo("read card[blockNo = " + blockNo + ", data = " + ByteUtil.bytes2HexString(data) + "]");
        int ret = mifareCardReader.write(blockNo, data);
        if (ret == RFCardError.SUCCESS) {
            view.displayInfo("write success");
        } else {
            view.displayInfo("write fail, error = " + ret);
        }
    }

    @Override
    public void addValue(int blockNo, int value) {
        view.displayInfo("card addValue [blockNo = " + blockNo + ", value = " + value + "]");
        int ret = mifareCardReader.increase(blockNo, value);
        if (ret == RFCardError.SUCCESS) {
            ret = mifareCardReader.transfer(blockNo);
            if (ret == RFCardError.SUCCESS) {
                view.displayInfo("addValue success");
            } else {
                view.displayInfo("transfer fail, error = " + ret);
            }
        } else {
            view.displayInfo("increase fail, error = " + ret);
        }
    }

    @Override
    public void reduceValue(int blockNo, int value) {
        view.displayInfo("card decrease [blockNo = " + blockNo + ", value = " + value + "]");
        int ret = mifareCardReader.decrease(blockNo, value);
        if (ret == RFCardError.SUCCESS) {
            ret = mifareCardReader.transfer(blockNo);
            if (ret == RFCardError.SUCCESS) {
                view.displayInfo("reduceValue success");
            } else {
                view.displayInfo("transfer fail, error = " + ret);
            }
        } else {
            view.displayInfo("decrease fail, error = " + ret);
        }
    }

    @Override
    public void exist() {
        boolean exist = mifareCardReader.exist();
        if (exist) {
            view.displayInfo("card exist");
        } else {
            view.displayInfo("card not exist");
        }
    }
}
