package com.github.ScipioAM.scipio_utils_common.reflect;

import com.github.ScipioAM.scipio_utils_common.AssertUtil;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 类里字段相关的反射工具方法
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/18
 */
public class FieldUtil {

    /**
     * 从实例对象中获取其类字段（类成员变量）
     * @param obj 实例对象
     * @return 目标对象的类字段
     */
    public static Field[] getFieldsFromObj(@NotNull Object obj) {
        AssertUtil.notNull(obj);
        Class<?> clazz = obj.getClass();
        return clazz.getFields();
    }

    /**
     * 获取目标类里所有字段上的指定注解
     * @param targetClass 目标类
     * @param annotationClass 指定要查找获取的注解类型
     * @param <T> 注解类型
     * @return 目标类里所有字段上的所有指定注解，如果没有找到注解则返回空list
     */
    public static <T extends Annotation> List<T> getAnnotationFromFields(@NotNull Class<?> targetClass, @NotNull Class<T> annotationClass) {
        List<T> annotations = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(annotationClass)) { //跳过没有被注解的字段
                T annotation = field.getDeclaredAnnotation(annotationClass);
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    /**
     * 获取类里，所有打了指定注解的字段
     * @param targetClass 目标类
     * @param annotationClass 指定注解
     * @return 所有打了指定注解的字段
     */
    public static List<Field> getFieldsByAnnotationPresent(@NotNull Class<?> targetClass, @NotNull Class<? extends Annotation> annotationClass) {
        List<Field> finalFields = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(annotationClass)) { //跳过没有被注解的字段
                finalFields.add(field);
            }
        }
        return finalFields;
    }

}
