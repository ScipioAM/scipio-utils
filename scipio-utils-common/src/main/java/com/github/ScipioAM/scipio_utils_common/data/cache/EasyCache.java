package com.github.ScipioAM.scipio_utils_common.data.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 简单易用的小缓存的实现
 * @param <K> 键的类型
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/29
 */
public class EasyCache<K> implements IEasyCache<K> {

    /** 缓存池 */
    protected final Map<K,CacheEntity<K>> cachePool = new ConcurrentHashMap<>();
    /** 定时器线程池，用于清除过期缓存 */
    protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    /** 缓存数据添加时的监听回调(不保证线程安全) */
    protected CacheAddListener addListener;
    /** 缓存数据移除时的监听回调(不保证线程安全) */
    protected CacheRemoveListener removeListener;

    /**
     * 缓存数据
     * @param key 键
     * @param data 要缓存的数据
     * @param expire 有效时长(单位毫秒)，为0则代表不过期
     */
    @Override
    public void putData(K key, Object data, long expire) {
        if(addListener != null && !addListener.onAdd(cachePool,key,data,expire)) {
            return;
        }
        cachePool.putIfAbsent(key,new CacheEntity<>(data, expire));
        if(expire > 0) { //只有大于0时才生效
            executor.schedule(new CacheRemoveThread<>(cachePool,key), expire, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 缓存数据
     * @param key 键
     * @param data 要缓存的数据
     */
    @Override
    public void putData(K key, Object data) {
        if(addListener != null && !addListener.onAdd(cachePool,key,data,null)) {
            return;
        }
        cachePool.putIfAbsent(key,new CacheEntity<>(data));
    }

    /**
     * 获取数据
     * @param key 缓存数据的键
     * @return 缓存的数据
     */
    @Override
    public Object getData(K key) {
        CacheEntity<K> entity = cachePool.get(key);
        return (entity == null ? null : entity.getData());
    }

    /**
     * 获取缓存数据bean
     * @param key 键
     * @return 缓存数据bean
     */
    @Override
    public CacheEntity<K> getCacheEntity(K key) {
        return cachePool.get(key);
    }

    /**
     * 获取全部存储的数据
     */
    @Override
    public Map<K, Object> getAllData() {
        Map<K, Object> dataMap = new LinkedHashMap<>();
        cachePool.forEach((key, cacheEntity) -> dataMap.put(key,cacheEntity.getData()));
        return dataMap;
    }

    /**
     * 获取全部存储的{@link CacheEntity}
     */
    @Override
    public Map<K, CacheEntity<K>> getAllCacheEntity() {
        return cachePool;
    }

    /**
     * 移除缓存数据(如果不存在则等于什么都不做)
     * @param key 缓存数据的键
     */
    @Override
    public void removeData(K key) {
        cachePool.remove(key);
        if(removeListener != null) {
            removeListener.onRemove(cachePool,key);
        }
    }

    /**
     * 清空缓存
     */
    @Override
    public void clearAll() {
        cachePool.clear();
    }

    /**
     * 设置过期时长
     * @param key 键
     * @param expire 有效时长(单位毫秒)，为0则代表不过期
     */
    @Override
    public void setExpire(K key, long expire) {
        cachePool.computeIfPresent(key, (k, v) -> {
            v.setExpire(expire);
            return v;
        });
        if(expire > 0) { //只有大于0时才生效
            executor.schedule(new CacheRemoveThread<>(cachePool,key), expire, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 指定的缓存是否过期
     * @param key 键
     * @return true代表过期，false代表没过期
     */
    @Override
    public boolean isExpired(K key) {
        CacheEntity<K> entity = cachePool.get(key);
        if(entity == null) {
            return true;
        }
        long expire = entity.getExpire();
        long lastRefreshTime = entity.getLastRefreshTime();
        if(expire <= 0) {
            return false;
        }
        return ((System.currentTimeMillis() - lastRefreshTime) >= expire);
    }

    /**
     * 获取指定数据最后一次刷新时间的时间(时间戳)
     * @param key 键
     * @return 指定数据最后一次刷新时间的时间(时间戳)
     * @throws CacheNotFoundException 未找到缓存数据
     */
    @Override
    public long getLastRefreshTime(K key) throws CacheNotFoundException {
        CacheEntity<K> entity = cachePool.get(key);
        if(entity == null) {
            throw new CacheNotFoundException("Cache data not found by key[" + key + "] !");
        }
        return entity.getLastRefreshTime();
    }

    /**
     * 是否包含指定键的缓存数据
     * @param key 键
     * @return true代表包含，false代表不包含
     */
    @Override
    public boolean isContainsKey(K key) {
        return cachePool.containsKey(key);
    }

    /**
     * 当前已缓存的数据总数
     */
    @Override
    public int size() {
        return cachePool.size();
    }

    /**
     * 设置缓存添加时的监听器
     */
    @Override
    public EasyCache<K> setCacheAddListener(CacheAddListener listener) {
        this.addListener = listener;
        return this;
    }

    /**
     * 设置缓存移除时的监听器
     */
    @Override
    public EasyCache<K> setCacheRemoveListener(CacheRemoveListener listener) {
        this.removeListener = listener;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("EasyCache{");
        cachePool.forEach((key, cacheEntity) ->
                s.append("[")
                        .append("key=").append(key).append(",")
                        .append("value=").append(cacheEntity.getData()).append(",")
                        .append("expire=").append(cacheEntity.getExpire()).append(",")
                        .append("lastRefreshTime=").append(cacheEntity.getLastRefreshTime())
                        .append("],")
        );
        s.deleteCharAt(s.length() - 1).append("}");
        return s.toString();
    }

    /**
     * 创建key为string的EasyCache实例
     */
    public static IEasyCacheS newStrEasyCache() {
        return new EasyCacheS();
    }

    /**
     * 创建key为integer的EasyCache实例
     */
    public static IEasyCacheInt newIntEasyCache() {
        return new EasyCacheInt();
    }

}
