package com.github.ScipioAM.scipio_utils_common.data.cache;

import com.github.ScipioAM.scipio_utils_common.reflect.FieldUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * TODO 简单易用的缓存
 * <p>目前仅计划支持{@link List},{@link Set},{@link Map},{@link Vector}</p>
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/28
 */
public interface EasyCache<K,V> {

    /**
     * 缓存数据
     * @param data 要缓存的数据
     */
    default void setData(V data) {
        //获取有注解的字段
        List<Field> fields = FieldUtil.getFieldsByAnnotationPresent(this.getClass(),EasyCacheField.class);
        if(fields.size() <= 0) { //没有注解，直接结束
            System.err.println("None of annotation[@EasyCacheField] can be found in \"" + this.getClass() + "\"");
            return;
        }

    }

    /**
     * 获取数据
     * @param key 缓存数据的键
     * @return 缓存的数据
     */
//    V getData(K key);

    /**
     * 移除缓存数据
     * @param key 缓存数据的键
     * @throws EasyCacheException 移除失败
     */
//    void removeData(K key) throws EasyCacheException;

}
