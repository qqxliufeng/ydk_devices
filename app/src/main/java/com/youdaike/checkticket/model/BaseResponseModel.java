package com.youdaike.checkticket.model;

import java.io.Serializable;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class BaseResponseModel<T> implements Serializable {
    protected String status;
    protected String message;
    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
