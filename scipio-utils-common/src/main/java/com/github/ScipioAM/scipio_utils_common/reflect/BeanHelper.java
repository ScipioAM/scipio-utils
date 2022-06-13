package com.github.ScipioAM.scipio_utils_common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaBean相关基础工具类
 *
 * @author Alan Scipio
 * @date 2021/10/13
 * @since 1.0.2
 */
public class BeanHelper {

    public static void copyDifferenceSet(Object target, Object source) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Field[] tgtFields = target.getClass().getDeclaredFields();
        Field[] srcFields = source.getClass().getDeclaredFields();
        System.out.println("tgtFields.len: " + tgtFields.length);
        System.out.println("srcFields.len: " + srcFields.length);
        int innerLoop = 0;
        for (Field tgtField : tgtFields) {
            for (Field srcField : srcFields) {
                System.out.println("inner loop: " + (++innerLoop));
                if (!tgtField.getName().equals(srcField.getName()) || tgtField.getType() != srcField.getType()) {
                    continue;
                }
                Object value = getValueByMethod(srcField, source.getClass(), source);
                if (value == null) {
                    break;
                }
                Object originalValue = getValueByMethod(tgtField, target.getClass(), target);
                if (originalValue == null) {
                    break;
                }
                setValueByMethod(tgtField, target.getClass(), target, value);
                break;
            }
        }//end of outside for
    }// end of copyDifferenceSet

    private static Object getValueByMethod(Field field, Class<?> beanClass, Object beanInstance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fieldName = field.getName();
        Class<?> fieldClass = field.getType();
        //获取get方法
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method getMethod = beanClass.getDeclaredMethod(getMethodName);
        return getMethod.invoke(beanInstance);
    }

    private static void setValueByMethod(Field field, Class<?> beanClass, Object beanInstance, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fieldName = field.getName();
        Class<?> fieldClass = field.getType();
        //获取set方法
        String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method setMethod = beanClass.getDeclaredMethod(setMethodName, fieldClass);
        setMethod.invoke(beanInstance, value);
    }

    //==========================================================================================================================================

    /**
     * 批量将map转为javaBean，map的key是javaBean的字段名（大小写敏感）
     *
     * @param originalList 原始数据
     * @param beanClass    javaBean的类型
     * @param <T>          javaBean的类型
     * @return 转换后的list
     * @throws NoSuchMethodException     javaBean没有空参构造方法
     * @throws InvocationTargetException javaBean构造方法抛异常
     * @throws InstantiationException    javaBean的这个空参构造方法只在抽象类里实现
     * @throws IllegalAccessException    调用javaBean空参构造方法、访问字段出现访问非法的问题（例如该构造方法为private的）
     * @throws NoSuchFieldException      根据map的key找不到对应的javaBean内的字段
     */
    public static <T> List<T> transformMap(List<Map<String, Object>> originalList, Class<T> beanClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<T> list = new ArrayList<>();
        for (Map<String, Object> map : originalList) {
            T bean = transformMap(map, beanClass);
            list.add(bean);
        }
        return list;
    }

    /**
     * 将map转为javaBean，map的key是javaBean的字段名（大小写敏感）
     *
     * @param map       原始数据
     * @param beanClass javaBean的类型
     * @param <T>       javaBean的类型
     * @return 转换后的javaBean
     * @throws NoSuchMethodException     javaBean没有空参构造方法
     * @throws InvocationTargetException javaBean构造方法抛异常
     * @throws InstantiationException    javaBean的这个空参构造方法只在抽象类里实现
     * @throws IllegalAccessException    调用javaBean空参构造方法、访问字段出现访问非法的问题（例如该构造方法为private的）
     * @throws NoSuchFieldException      根据map的key找不到对应的javaBean内的字段
     */
    public static <T> T transformMap(Map<String, Object> map, Class<T> beanClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        T bean = beanClass.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Field field = beanClass.getDeclaredField(key);
            field.setAccessible(true);
            field.set(bean, value);
        }
        return bean;
    }

    /**
     * javaBean转为map，map的key是javaBean的字段名（大小写敏感）
     *
     * @param bean           javaBean对象
     * @param mapInitializer [可为null]map初始化（指定生成什么类型的map）
     * @param <T>            javaBean的类型
     * @return 转换后的map
     * @throws IllegalAccessException 访问字段出现访问非法的问题
     */
    public static <T> Map<String, Object> transformToMap(T bean, MapInitializer mapInitializer) throws IllegalAccessException {
        Map<String, Object> map = (mapInitializer != null ? mapInitializer.init() : MapInitializer.DEFAULT.init());
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            field.setAccessible(true);
            Object value = field.get(bean);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * javaBean转为map，map的key是javaBean的字段名（大小写敏感），默认为{@link HashMap}
     *
     * @param bean javaBean对象
     * @param <T>  javaBean的类型
     * @return 转换后的map
     * @throws IllegalAccessException 访问字段出现访问非法的问题
     */
    public static <T> Map<String, Object> transformToMap(T bean) throws IllegalAccessException {
        return transformToMap(bean, null);
    }

    /**
     * Map构造器
     */
    @FunctionalInterface
    public interface MapInitializer {

        /**
         * 构造map
         *
         * @return map的类型自行指定
         */
        Map<String, Object> init();

        /**
         * 默认构造器：构造为{@link HashMap}
         */
        MapInitializer DEFAULT = HashMap::new;

    }

}
