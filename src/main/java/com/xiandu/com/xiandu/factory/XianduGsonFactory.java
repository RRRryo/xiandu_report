package com.xiandu.com.xiandu.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Administrator on 8/7/2020.
 */
public class XianduGsonFactory {

    public static Gson getXianduGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}
