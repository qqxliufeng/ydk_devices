package com.youdaike.checkticket.model;

import java.io.Serializable;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class TicketConsumeModel implements Serializable {
    private String id;
    private String couponnum;
    private String createtime;

    public String getCouponnum() {
        return couponnum;
    }

    public void setCouponnum(String couponnum) {
        this.couponnum = couponnum;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
