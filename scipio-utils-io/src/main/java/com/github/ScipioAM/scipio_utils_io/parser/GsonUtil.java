package com.github.ScipioAM.scipio_utils_io.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gson工具类
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2018/5/16
 */
public class GsonUtil {

    private GsonUtil() { }

    /**
     * 从json字符串里解出一个值（根据对应的键）
     * @param json      原始数据
     * @param searchKey 查找的键
     */
    public static String getStrFromJson(String json, String searchKey) {
        JsonObject jObject = getJsonObject(json);
        return jObject.get(searchKey).getAsString();
    }


    /**
     * 从json字符串里解出多个值（根据对应的键）
     * @param json       原始数据
     * @param searchKeys 查找的键
     */
    public static List<String> getStrFromJson(String json, List<String> searchKeys) {
        List<String> result = new ArrayList<>();
        JsonObject jObject = getJsonObject(json);
        for (String key : searchKeys) {
            result.add(jObject.get(key).getAsString());
        }
        return result;
    }

    //解析json字符串，返回jsonObject对象
    public static JsonObject getJsonObject(String json) {
        return (JsonObject) JsonParser.parseString(json);
    }

    //解析json字符串，返回其中部分内容的jsonObject对象
    public static JsonObject getJsonObject(String json, String searchKey) {
        JsonObject root = (JsonObject) JsonParser.parseString(json);
        return root.get(searchKey).getAsJsonObject();
    }

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String toJson(Object obj, boolean isDisableHtmlEscaping) {
        //是否不转换HTML特殊字符
        Gson gson = (isDisableHtmlEscaping ? new GsonBuilder().disableHtmlEscaping().create() : new Gson());
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, clazz);
    }

    public static HashMap<String, String> fromHashMapJson(String jsonStr) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(jsonStr, type);
    }

    public static <T> T fromJsonWithType(String jsonStr, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, type);
    }

    /**
     * 将json格式化输出
     */
    public static String toPrettyPrint(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

}
