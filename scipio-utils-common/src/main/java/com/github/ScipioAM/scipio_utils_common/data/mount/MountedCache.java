package com.github.ScipioAM.scipio_utils_common.data.mount;

/**
 * 外挂缓存
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/30
 */
public interface MountedCache {

    /** 默认{@link CacheField}的ID */
    int DEFAULT_ID = 1;

    /**
     * 给{@link CacheField}赋值
     * @param id 通过id指定是哪个{@link CacheField}
     * @param key 键（如果是list或set之类的则不需要，赋值为null即可）
     * @param data 值
     */
    default void putData(int id, Object key, Object data) throws Exception {}

    default void putData(Object key, Object data) throws Exception {
        putData(DEFAULT_ID,key,data);
    }

    default void putData(Object data) throws Exception {
        putData(DEFAULT_ID,null,data);
    }

    /**
     * 从{@link CacheField}取值
     * @param id 通过id指定是哪个{@link CacheField}
     * @param key 键（如果是list或set之类的则为下标）
     * @param <T> 值的类型
     * @return 值
     */
    default <T> T getData(int id, Object key) throws Exception {
        return null;
    }

    default <T> T getData(Object key) throws Exception {
        return getData(DEFAULT_ID, key);
    }

    default <T> T getData() throws Exception {
        return getData(DEFAULT_ID,null);
    }

}
