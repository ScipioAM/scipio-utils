package com.github.ScipioAM.scipio_utils_common.data.cache;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/29
 */
public class CacheEntity<K> {

    /** 缓存的数据 */
    private Object data;

    /** 过期时长(单位毫秒)，为0则代表不过期 */
    private long expire = 0;

    /** 最后一次刷新时间(时间戳) */
    private long lastRefreshTime;

    public CacheEntity() {
        this.lastRefreshTime = System.currentTimeMillis();
    }

    public CacheEntity(Object data) {
        this.data = data;
        this.lastRefreshTime = System.currentTimeMillis();
    }

    public CacheEntity(Object data, long expire) {
        this.data = data;
        this.expire = expire;
        this.lastRefreshTime = System.currentTimeMillis();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }
}
