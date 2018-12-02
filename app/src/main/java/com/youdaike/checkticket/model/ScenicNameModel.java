package com.youdaike.checkticket.model;

import java.io.Serializable;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class ScenicNameModel implements Serializable {
    private String id;
    private String title;

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
}
