package com.youdaike.checkticket.model;

/**
 * Created by yuanxxx on 2016/12/7.
 */

public class PrintEvent {
    private int falg;//0:打印失败  1:打印成功

    public PrintEvent(int falg) {
        this.falg = falg;
    }

    public int getFalg() {
        return falg;
    }
}
