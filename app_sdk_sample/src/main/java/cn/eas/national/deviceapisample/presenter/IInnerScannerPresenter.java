package cn.eas.national.deviceapisample.presenter;

/**
 * Created by Czl on 2017/7/23.
 */

public interface IInnerScannerPresenter {
    void startScan(int timeout);
    void stopScan();
}
