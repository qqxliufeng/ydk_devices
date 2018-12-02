package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.file.ParameterFile;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;

import static cn.eas.national.deviceapisample.activity.ParActivity.KEY_TYPE_STRING;

/**
 * par参数文件操作接口。
 */

public abstract class ParManagerImpl extends BaseDevice {
    private ParameterFile file;
    private String moduleName;
    private String fileName;

    public ParManagerImpl(IDeviceView view, String moduleName, String fileName) {
        file = new ParameterFile(moduleName, fileName);
        this.moduleName = moduleName;
        this.fileName = fileName;
    }

    public void isExist() {
        boolean exist = file.isExists();
        displayInfo("参数文件[moduleName = " + moduleName + ", fileName = " + fileName + "] " + (exist ? "存在" : "不存在"));
    }

    public void isFirstRun() {
        boolean isFirstRun = file.isFirstRun();
        displayInfo("参数文件[moduleName = " + moduleName + ", fileName = " + fileName + "] " + (isFirstRun ? "已读取过" : "未读取"));
    }

    public void readParam(int keyType, String key) {
        if (keyType == KEY_TYPE_STRING) {
            String value = file.getString(key, "");
            displayInfo("参数[" + key + "]读取结果：" + value);
        } else {
            boolean value = file.getBoolean(key, false);
            displayInfo("参数[" + key + "]读取结果：" + value);
        }
    }

    public void writeParam(int keyType, String key, Object value) {
        boolean result = false;
        if (keyType == KEY_TYPE_STRING) {
            result = file.setString(key, (String) value);
        } else {
            result = file.setBoolean(key, (boolean) value);
        }
        displayInfo("参数[" + key + "] 设置结果：" + result);
    }
}
