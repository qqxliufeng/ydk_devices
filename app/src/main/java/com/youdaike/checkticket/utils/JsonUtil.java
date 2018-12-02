package com.youdaike.checkticket.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class JsonUtil {
    public static boolean isPrintException = true;

    private JsonUtil() {
        throw new AssertionError();
    }

    public static <T> List<T> getListFromJSON(String json, Class<T> clazz) {
        Log.i("JSONUtil", "getListFromJSON: " + json);
        List<T> lst = new ArrayList<T>();
        if (TextUtils.isEmpty(json)) {
            return lst;
        }
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                lst.add(new Gson().fromJson(elem, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }
}
