package com.github.ScipioAM.scipio_utils_common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JavaBean转换工具类
 * @author Alan Scipio
 * @since 1.0.9 2021/12/17
 */
public class TransformUtil {

    /**
     * 批量将map转为javaBean，map的key是javaBean的字段名（大小写敏感）
     * @param originalList 原始数据
     * @param beanClass javaBean的类型
     * @param <T> javaBean的类型
     * @return 转换后的list
     * @throws NoSuchMethodException javaBean没有空参构造方法
     * @throws InvocationTargetException javaBean构造方法抛异常
     * @throws InstantiationException javaBean的这个空参构造方法只在抽象类里实现
     * @throws IllegalAccessException 调用javaBean空参构造方法、访问字段出现访问非法的问题（例如该构造方法为private的）
     * @throws NoSuchFieldException 根据map的key找不到对应的javaBean内的字段
     */
    public static <T> List<T> transformMap(List<Map<String, Object>> originalList, Class<T> beanClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<T> list = new ArrayList<>();
        for(Map<String, Object> map : originalList) {
            T bean = transformMap(map,beanClass);
            list.add(bean);
        }
        return list;
    }

    /**
     * 将map转为javaBean，map的key是javaBean的字段名（大小写敏感）
     * @param map 原始数据
     * @param beanClass javaBean的类型
     * @param <T> javaBean的类型
     * @return 转换后的javaBean
     * @throws NoSuchMethodException javaBean没有空参构造方法
     * @throws InvocationTargetException javaBean构造方法抛异常
     * @throws InstantiationException javaBean的这个空参构造方法只在抽象类里实现
     * @throws IllegalAccessException 调用javaBean空参构造方法、访问字段出现访问非法的问题（例如该构造方法为private的）
     * @throws NoSuchFieldException 根据map的key找不到对应的javaBean内的字段
     */
    public static <T> T transformMap(Map<String, Object> map, Class<T> beanClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T bean = beanClass.getDeclaredConstructor().newInstance();
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Field field = beanClass.getDeclaredField(key);
            field.setAccessible(true);
            field.set(bean,value);
        }
        return bean;
    }

}
