package cn.eas.national.deviceapisample.presenter;

/**
 * Created by Czl on 2017/7/23.
 */

public interface IRFCpuCardPresenter {
    void cardPower();
    void cardHalt();
    void exchangeApdu();
    void exist();
}
