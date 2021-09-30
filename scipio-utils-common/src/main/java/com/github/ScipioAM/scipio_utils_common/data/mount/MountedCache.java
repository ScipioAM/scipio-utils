package com.github.ScipioAM.scipio_utils_common.data.mount;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/30
 */
public interface MountedCache {

    /** 默认ID */
    int DEFAULT_ID = 1;

    default void putData(int id, Object key, Object data) throws Exception {
        //System.err.println("Method [MountedCache.putData()] not overrode yet, nothing happened");
    }

    default void putData(Object key, Object data) throws Exception {
        putData(DEFAULT_ID,key,data);
    }

    default void putData(Object data) throws Exception {
        putData(DEFAULT_ID,null,data);
    }

    default <T> T getData(int id, Object key) throws Exception {
        return null;
    }

//    default <T> T getData(Object key) throws Exception {
//        return getData(DEFAULT_ID, key);
//    }

//    default <T> T getData() throws Exception {
//        return getData(DEFAULT_ID,null);
//    }

}
