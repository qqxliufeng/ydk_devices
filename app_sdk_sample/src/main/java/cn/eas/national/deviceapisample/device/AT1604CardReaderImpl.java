package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.card.At1604Driver;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;

import cn.eas.national.deviceapisample.data.SyncCardError;
import cn.eas.national.deviceapisample.util.ByteUtil;

import static cn.eas.national.deviceapisample.data.BaseError.FAIL;

public abstract class AT1604CardReaderImpl extends BaseDevice {
    private At1604Driver driver;

    public AT1604CardReaderImpl() {
        driver = new At1604Driver(At1604Driver.CARDTYPE_102_CARD);
    }

    public void cardPower() {
        int ret = powerUp();
        if (ret == SyncCardError.SUCCESS) {
            displayInfo("card power success");
        } else {
            displayInfo("card power fail [ret = " + ret + "]");
        }
    }

    private int powerUp() {
        int ret = SyncCardError.FAIL;
        try {
            boolean exist = driver.exists();
            if (!exist) {
                displayInfo("card not exist");
                return ret;
            }
            BytesBuffer atr = new BytesBuffer();
            ret = driver.powerUp(At1604Driver.VOL_5, atr);
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("power up fail, error = " + ret);
            } else {
                displayInfo("power up success, atr = " + ByteUtil.bytes2HexString(atr.getData()));
            }
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void cardHalt() {
        int ret = powerDown();
        halt();
        if (ret == SyncCardError.SUCCESS) {
            displayInfo("card halt success");
        } else {
            displayInfo("card halt fail [ret = " + ret + "]");
        }
    }

    private int powerDown() {
        if (driver != null) {
            try {
                return driver.powerDown();
            } catch (RequestException e) {
                e.printStackTrace();
                return FAIL;
            }
        }
        return SyncCardError.SUCCESS;
    }

    private void halt() {
        if (driver != null) {
            try {
                driver.halt();
            } catch (RequestException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] read(byte[] pwd, int address, int len) {
        byte[] result = null;
        int ret = FAIL;
        try {
            if (pwd != null && pwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(At1604Driver.KEYTYPE_1604_SC, pwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail [ret = " + ret + "]");
                    return null;
                }
            }
            BytesBuffer data = new BytesBuffer();
            ret = driver.read(address, len, data);
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("read fail [ret = " + ret + "]");
                return null;
            }
            return data.getData();
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int write(byte[] pwd, int address, byte[] data) {
        int ret = FAIL;
        try {
            if (pwd != null && pwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(At1604Driver.KEYTYPE_1604_SC, pwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail [ret = " + ret + "]");
                    return ret;
                }
            }
            return driver.write(address, data);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int changePassword(byte[] originalPwd, byte[] newPwd) {
        int ret = FAIL;
        try {
            if (originalPwd != null && originalPwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(At1604Driver.KEYTYPE_1604_SC, originalPwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail [ret = " + ret + "]");
                    return ret;
                }
                return driver.changeKey(At1604Driver.KEYTYPE_1604_SC, newPwd);
            } else {
                return ret;
            }
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
