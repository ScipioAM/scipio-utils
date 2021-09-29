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
public class EasyCache<K> implements EasyCacheApi<K> {

    /** 缓存池 */
    protected final Map<K,CacheEntity<K>> cachePool = new ConcurrentHashMap<>();
    /** 定时器线程池，用于清除过期缓存 */
    protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    /** 缓存数据添加时的监听回调(线程安全) */
    protected CacheAddListener addListener;
    /** 缓存数据移除时的监听回调(不保证线程安全) */
    protected CacheRemoveListener removeListener;

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

    @Override
    public void putData(K key, Object data) {
        if(addListener != null && !addListener.onAdd(cachePool,key,data,null)) {
            return;
        }
        cachePool.putIfAbsent(key,new CacheEntity<>(data));
    }

    @Override
    public Object getData(K key) {
        CacheEntity<K> entity = cachePool.get(key);
        return (entity == null ? null : entity.getData());
    }

    @Override
    public CacheEntity<K> getCacheEntity(K key) {
        return cachePool.get(key);
    }

    @Override
    public Map<K, Object> getAllData() {
        Map<K, Object> dataMap = new LinkedHashMap<>();
        cachePool.forEach((key, cacheEntity) -> dataMap.put(key,cacheEntity.getData()));
        return dataMap;
    }

    @Override
    public Map<K, CacheEntity<K>> getAllCacheEntity() {
        return cachePool;
    }

    @Override
    public void removeData(K key) {
        cachePool.remove(key);
        if(removeListener != null) {
            removeListener.onRemove(cachePool,key);
        }
    }

    @Override
    public void clearAll() {
        cachePool.clear();
    }

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

    @Override
    public long getLastRefreshTime(K key) throws CacheNotFoundException {
        CacheEntity<K> entity = cachePool.get(key);
        if(entity == null) {
            throw new CacheNotFoundException("Cache data not found by key[" + key + "] !");
        }
        return entity.getLastRefreshTime();
    }

    @Override
    public boolean isContains(K key) {
        return cachePool.containsKey(key);
    }

    @Override
    public int size() {
        return cachePool.size();
    }

    @Override
    public void setCacheAddListener(CacheAddListener listener) {
        this.addListener = listener;
    }

    @Override
    public void setCacheRemoveListener(CacheRemoveListener listener) {
        this.removeListener = listener;
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

}
