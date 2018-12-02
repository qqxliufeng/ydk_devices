package com.youdaike.checkticket.model;

import java.io.Serializable;

/**
 * Created by yuanxx on 2016/12/19.
 */
public class VersionModel implements Serializable {
    private String version;
    private String isreset;  //1 强制更新 0 不强制更新
    private String appurl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIsreset() {
        return isreset;
    }

    public void setIsreset(String isreset) {
        this.isreset = isreset;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }
}
