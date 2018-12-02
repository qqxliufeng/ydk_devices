package cn.eas.national.deviceapisample.presenter;

/**
 * Created by Czl on 2017/7/23.
 */

public interface IICCpuCardPresenter {
    void cardPower();
    void cardHalt();
    void exist();
    void exchangeApdu();
}
