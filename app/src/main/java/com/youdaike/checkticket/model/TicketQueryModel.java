package com.youdaike.checkticket.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class TicketQueryModel implements Serializable {
    private String id;
    private String title;
    private String surplusnm;//剩余数量
    private String buynum;//购买数量
    private String buytime;//购买时间 "2016-08-09 10:10:10"
    private String moblie;//手机号码 "15789909898"
    private String allowdate;//有效期时间 "2016-08-09-2017-08-09"
    private List<TicketConsumeModel> couponlog;//消费记录

    public String getAllowdate() {
        return allowdate;
    }

    public void setAllowdate(String allowdate) {
        this.allowdate = allowdate;
    }

    public String getBuynum() {
        return buynum;
    }

    public void setBuynum(String buynum) {
        this.buynum = buynum;
    }

    public String getBuytime() {
        return buytime;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }

    public List<TicketConsumeModel> getCouponlog() {
        return couponlog;
    }

    public void setCouponlog(List<TicketConsumeModel> couponlog) {
        this.couponlog = couponlog;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getSurplusnm() {
        return surplusnm;
    }

    public void setSurplusnm(String surplusnm) {
        this.surplusnm = surplusnm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
