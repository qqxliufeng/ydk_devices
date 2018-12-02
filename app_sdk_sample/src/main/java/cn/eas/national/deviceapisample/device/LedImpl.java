package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.device.RFCardReader;

import cn.eas.national.deviceapisample.data.Constants;

/**
 * 针对终端上已经非接模块可直接显示，否则需外接设备进行显示。
 * C10/W280P等终端外接密码键盘使用时，非接操作实例初始化时传入设备名称为“EXTRFCARD”。
 */

public abstract class LedImpl extends BaseDevice {
    private String deviceName;

    public LedImpl(String deviceName) {
        this.deviceName = deviceName;
    }

    public void turnOn(int led) {
        if (deviceName.equals(Constants.Led.DEVICE_INNER)) {
            RFCardReader.getInstance().turnOnLed(led);
        } else {
            RFCardReader.getOtherInstance("EXTRFCARD").turnOnLed(led);
        }
    }

    public void turnOff(int led) {
        if (deviceName.equals(Constants.Led.DEVICE_INNER)) {
            RFCardReader.getInstance().turnOffLed(led);
        } else {
            RFCardReader.getOtherInstance("EXTRFCARD").turnOffLed(led);
        }
    }
}
