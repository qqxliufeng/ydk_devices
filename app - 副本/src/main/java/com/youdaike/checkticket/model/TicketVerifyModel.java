package com.youdaike.checkticket.model;

import java.io.Serializable;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class TicketVerifyModel implements Serializable {
    private String id;
    private String title;
    private String surplusnm;//剩余数量
    private String minnum;//最小消费数量 0为不限制

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMinnum() {
        return minnum;
    }

    public void setMinnum(String minnum) {
        this.minnum = minnum;
    }

    public String getSurplusnm() {
        return surplusnm;
    }

    public void setSurplusnm(String surplusnm) {
        this.surplusnm = surplusnm;
    }
}
