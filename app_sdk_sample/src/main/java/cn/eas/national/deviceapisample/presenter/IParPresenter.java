package cn.eas.national.deviceapisample.presenter;

/**
 * Created by Czl on 2017/7/23.
 */

public interface IParPresenter {
    void isExist();
    void isFirstRun();
    void read(int keyType, String key);
    void write(int keyType, String key, Object value);
}
