package cn.eas.national.deviceapisample.presenter;

/**
 * Created by Czl on 2017/7/23.
 */

public interface ISIMCardPresenter {
    void cardPower();
    void cardHalt();
    void read();
    void write();
    void changePassword();
}
