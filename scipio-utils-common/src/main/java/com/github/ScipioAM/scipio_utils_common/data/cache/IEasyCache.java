package com.github.ScipioAM.scipio_utils_common.data.cache;

import java.util.Map;

/**
 * 简单易用的小缓存的操作API
 * @param <K> 键的类型
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/28
 */
public interface IEasyCache<K> {

    /**
     * 缓存数据
     * @param key 键
     * @param data 要缓存的数据
     * @param expire 有效时长(单位毫秒)，为0则代表不过期
     */
    void putData(K key, Object data, long expire);

    /**
     * 缓存数据
     * @param key 键
     * @param data 要缓存的数据
     */
    void putData(K key, Object data);

    /**
     * 获取数据
     * @param key 缓存数据的键
     * @return 缓存的数据
     */
    Object getData(K key);

    /**
     * 获取缓存数据bean
     * @param key 键
     * @return 缓存数据bean
     */
    CacheEntity<K> getCacheEntity(K key);

    /**
     * 获取全部存储的数据
     */
    Map<K,Object> getAllData();

    /**
     * 获取全部存储的{@link CacheEntity}
     */
    Map<K,CacheEntity<K>> getAllCacheEntity();

    /**
     * 移除缓存数据(如果不存在则等于什么都不做)
     * @param key 缓存数据的键
     */
    void removeData(K key);

    /**
     * 清空缓存
     */
    void clearAll();

    /**
     * 重置缓存(其实就是{@link #clearAll()}的别名)
     */
    default void reset() {
        clearAll();
    }

    /**
     * 设置过期时长
     * @param key 键
     * @param expire 有效时长(单位毫秒)，为0则代表不过期
     */
    void setExpire(K key, long expire);

    /**
     * 指定的缓存是否过期
     * @param key 键
     * @return true代表过期，false代表没过期
     */
    boolean isExpired(K key);

    /**
     * 获取指定数据最后一次刷新时间的时间(时间戳)
     * @param key 键
     * @return 指定数据最后一次刷新时间的时间(时间戳)
     * @throws CacheNotFoundException 未找到缓存数据
     */
    long getLastRefreshTime(K key) throws CacheNotFoundException;

    /**
     * 是否包含指定键的缓存数据
     * @param key 键
     * @return true代表包含，false代表不包含
     */
    boolean isContainsKey(K key);

    /**
     * 当前已缓存的数据总数
     */
    int size();

    /**
     * 设置缓存添加时的监听器
     */
    EasyCache<K> setCacheAddListener(CacheAddListener listener);

    /**
     * 设置缓存移除时的监听器
     */
    EasyCache<K> setCacheRemoveListener(CacheRemoveListener listener);

}
