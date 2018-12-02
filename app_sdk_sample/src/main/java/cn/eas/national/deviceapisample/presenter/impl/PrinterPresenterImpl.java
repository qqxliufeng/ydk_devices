package cn.eas.national.deviceapisample.presenter.impl;

import com.landicorp.android.eptapi.device.Printer;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.device.PrinterImpl;
import cn.eas.national.deviceapisample.presenter.IPrinterPresenter;

/**
 * Created by Czl on 2017/7/27.
 */

public class PrinterPresenterImpl implements IPrinterPresenter {
    private BaseDeviceActivity view;
    private PrinterImpl printer;

    public PrinterPresenterImpl(BaseDeviceActivity deviceView) {
        this.view = deviceView;
        this.printer = new PrinterImpl(deviceView) {
            @Override
            protected void onDeviceServiceCrash() {
                PrinterPresenterImpl.this.view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                PrinterPresenterImpl.this.view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void start() {
        int ret = printer.getPrinterStatus();
        if (ret != Printer.ERROR_NONE) {
            view.displayInfo(printer.getDescribe(ret));
            return;
        }
        printer.init();
        if(!printer.addBitmap()) {
            view.displayInfo("add bitmap fail");
            return;
        }
        if (!printer.addText()) {
            view.displayInfo("add text fail");
            return;
        }
        if (!printer.addBarcode()) {
            view.displayInfo("add barcode fail");
            return;
        }
        if (!printer.addQRcode()) {
            view.displayInfo("add qrcode fail");
            return;
        }
        if (!printer.feedLine(3)) {
            view.displayInfo("feed line fail");
            return;
        }
        if (!printer.cutPaper()) {
            view.displayInfo("cut page fail");
            return;
        }
        printer.startPrint();
    }
}
