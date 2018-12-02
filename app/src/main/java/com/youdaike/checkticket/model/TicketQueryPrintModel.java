package com.youdaike.checkticket.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanxx on 2017/2/06.
 */
public class TicketQueryPrintModel implements Serializable {
    private String prname; //商家名称;
    private String teamtitle;//产品名称
    private String fxs;//分销商
    private String couponno;//凭证号
    private String couponnum;//消费数量
    private String orderlink;//联系人
    private String ordermoblie;//手机
    private String ordertime;//购票时间
    private String createtime;//消费时间
    private String teamtotime;//有效期

    public String getCouponno() {
        return couponno;
    }

    public void setCouponno(String couponno) {
        this.couponno = couponno;
    }

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

    public String getFxs() {
        return fxs;
    }

    public void setFxs(String fxs) {
        this.fxs = fxs;
    }

    public String getOrderlink() {
        return orderlink;
    }

    public void setOrderlink(String orderlink) {
        this.orderlink = orderlink;
    }

    public String getOrdermoblie() {
        if (ordermoblie != null && ordermoblie.trim().length() == 11) {
            return ordermoblie.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return ordermoblie;
    }

    public void setOrdermoblie(String ordermoblie) {
        this.ordermoblie = ordermoblie;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    public String getTeamtitle() {
        return teamtitle;
    }

    public void setTeamtitle(String teamtitle) {
        this.teamtitle = teamtitle;
    }

    public String getTeamtotime() {
        return teamtotime;
    }

    public void setTeamtotime(String teamtotime) {
        this.teamtotime = teamtotime;
    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(prname);
//        parcel.writeString(teamtitle);
//        parcel.writeString(fxs);
//        parcel.writeString(couponno);
//        parcel.writeString(couponnum);
//        parcel.writeString(orderlink);
//        parcel.writeString(ordermoblie);
//        parcel.writeString(ordertime);
//        parcel.writeString(createtime);
//        parcel.writeString(teamtotime);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }


}
