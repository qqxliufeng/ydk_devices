package com.youdaike.checkticket.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.youdaike.checkticket.model.BaseResponseModel;

import java.lang.reflect.Type;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class ResponseUtil {
    public static <T> BaseResponseModel<T> getStringResponse(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseResponseModel<T>>() {
        }.getType();
        BaseResponseModel<T> result = gson.fromJson(json, type);
        return result;
    }

    public static <T> BaseResponseModel<T> getObjectResponse(String json, Class<T> clazz) {
        Log.i("json", "getObjectResponse: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<BaseResponseModel<T>>() {
        }.getType();
        BaseResponseModel<T> result = gson.fromJson(json, type);
        String dataJson = gson.toJson(result.getData());
        try {
            if (TextUtils.isEmpty(dataJson) || "\"\"".equals(dataJson)) {
                return result;
            } else {
                result.setData(gson.fromJson(dataJson, clazz));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> BaseResponseModel<T> getListResponse(String json, Class<T> clazz) {
        Gson gson = new Gson();
        Type type = new TypeToken<BaseResponseModel<T>>() {
        }.getType();
        BaseResponseModel<T> result = gson.fromJson(json, type);
        String dataJson = gson.toJson(result.getData());
        if (TextUtils.isEmpty(dataJson) || "\"\"".equals(dataJson)) {
            return result;
        } else {
            result.setData((T) JsonUtil.getListFromJSON(dataJson, clazz));
        }
        return result;
    }
}
