package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.card.At24CxxDriver;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;

import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.data.SyncCardError;

import static cn.eas.national.deviceapisample.data.BaseError.FAIL;

public abstract class AT24CxxCardReaderImpl extends BaseDevice {
    private At24CxxDriver driver;

    public AT24CxxCardReaderImpl(int cardType) {
        if (cardType == Constants.SyncCard.CARD_TYPE_AT24C01) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C01);
        } else if (cardType == Constants.SyncCard.CARD_TYPE_AT24C02) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C02);
        } else if (cardType == Constants.SyncCard.CARD_TYPE_AT24C04) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C04);
        } else if (cardType == Constants.SyncCard.CARD_TYPE_AT24C08) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C08);
        } else if (cardType == Constants.SyncCard.CARD_TYPE_AT24C16) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C16);
        } else if (cardType == Constants.SyncCard.CARD_TYPE_AT24C32) {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C32);
        } else {
            driver = new At24CxxDriver(At24CxxDriver.CARDTYPE_CARDTYPE_AT24C64);
        }
    }

    public void cardPower() {
        int ret = powerUp();
        if (ret == SyncCardError.SUCCESS) {
            displayInfo("card power success");
        } else {
            displayInfo("card power fail, error = " + ret);
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
            ret = driver.powerUp();
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("power up fail, error = " + ret);
            } else {
                displayInfo("power up success");
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
            displayInfo("card halt fail, error = " + ret);
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

    public byte[] read(int address, int len) {
        byte[] result = null;
        int ret = FAIL;
        try {
            BytesBuffer data = new BytesBuffer();
            ret = driver.read(address, len, data);
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("read fail, error = " + ret);
                return null;
            }
            return data.getData();
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int write(int address, byte[] data) {
        int ret = FAIL;
        try {
            return driver.write(address, data);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
