package com.github.ScipioAM.scipio_utils_common.data.cache;

import java.util.Map;

/**
 * {@link EasyCacheApi}添加缓存数据时的监听回调
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/29
 */
@FunctionalInterface
public interface CacheAddListener {

    /**
     * 添加缓存数据时的监听回调
     * @param cachePool 缓存池
     * @param key 键
     * @param data 要缓存的值
     * @param expire 有效时长(单位毫秒)，为0代表永不过期，调用者未指定时为null
     * @param <K> 键的类型
     * @return 是否继续执行（添加），返回true代表要执行
     */
    <K> boolean onAdd(Map<K, CacheEntity<K>> cachePool, Object key, Object data, Long expire);

}
