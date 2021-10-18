package com.github.ScipioAM.scipio_utils_common.data.cache;

import java.util.Map;

/**
 * 简单易用小缓存的静态单例工具
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/30
 */
public class EasyCacheUtil {

    private static final IEasyCacheS easyCache = new EasyCacheS();

    public static void putData(String key, Object data, long expire) {
        easyCache.putData(key, data, expire);
    }

    public static void putData(String key, Object data) {
        easyCache.putData(key, data);
    }

    public static Object getData(String key) {
        return easyCache.getData(key);
    }

    public static CacheEntity<String> getCacheEntity(String key) {
        return easyCache.getCacheEntity(key);
    }

    public static Map<String, Object> getAllData() {
        return easyCache.getAllData();
    }

    public static Map<String, CacheEntity<String>> getAllCacheEntity() {
        return easyCache.getAllCacheEntity();
    }

    public static void removeData(String key) {
        easyCache.removeData(key);
    }

    public static void clearAll() {
        easyCache.clearAll();
    }

    public static void setExpire(String key, long expire) {
        easyCache.setExpire(key, expire);
    }

    public static boolean isExpired(String key) {
        return easyCache.isExpired(key);
    }

    public static long getLastRefreshTime(String key) throws CacheNotFoundException {
        return easyCache.getLastRefreshTime(key);
    }

    public static boolean isContainsKey(String key) {
        return easyCache.isContainsKey(key);
    }

    public static int size() {
        return easyCache.size();
    }

}
