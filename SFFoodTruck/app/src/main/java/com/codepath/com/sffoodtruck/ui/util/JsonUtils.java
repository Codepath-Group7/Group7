package com.codepath.com.sffoodtruck.ui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by rob2le on 10/18/2017.
 */

public class JsonUtils {
    public static String toJson(Object data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(data);
    }

    public static <T> T fromJson(String jsonString, Class<T> type) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, type);
    }

    public static <T> T fromJson(String jsonString, Type typeToken) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, typeToken);
    }
}
