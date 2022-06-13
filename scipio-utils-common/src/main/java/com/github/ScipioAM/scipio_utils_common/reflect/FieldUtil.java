package com.github.ScipioAM.scipio_utils_common.reflect;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 类里字段相关的反射工具方法
 *
 * @author Alan Scipio
 * @date 2021/11/22
 */
public class FieldUtil {

    protected FieldUtil() {
    }

    public static class FieldExt<T extends Annotation> {
        private Field field;
        private T annotation;

        public FieldExt() {
        }

        public FieldExt(Field field, T annotation) {
            this.field = field;
            this.annotation = annotation;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public T getAnnotation() {
            return annotation;
        }

        public void setAnnotation(T annotation) {
            this.annotation = annotation;
        }
    }

    /**
     * 获取类字段（类成员变量）
     *
     * @param obj 实例对象
     * @return 目标对象的类字段
     */
    public static Field[] getFields(@NotNull Object obj) {
        assert obj != null;
        Class<?> clazz = obj.getClass();
        return clazz.getFields();
    }

    /**
     * 获取类字段（类成员变量）
     *
     * @param obj 实例对象
     * @return key：字段名，value：目标对象的类字段
     */
    public static Map<String, Field> getFieldsWithName(@NotNull Object obj) {
        assert obj != null;
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getFields();
        Map<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    /**
     * 获取目标类里所有字段上的指定注解
     *
     * @param targetClass     目标类
     * @param annotationClass 指定要查找获取的注解类型
     * @param <T>             注解类型
     * @return 目标类里所有字段上的所有指定注解，如果没有找到注解则返回空list
     */
    public static <T extends Annotation> List<T> getAnnotationFromFields(@NotNull Class<?> targetClass, @NotNull Class<T> annotationClass) {
        List<T> annotations = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) { //跳过没有被注解的字段
                T annotation = field.getDeclaredAnnotation(annotationClass);
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    /**
     * 获取类里，所有打了指定注解的字段
     *
     * @param targetClass     目标类
     * @param annotationClass 指定注解
     * @return 所有打了指定注解的字段
     */
    public static List<Field> getFieldsByAnnotationPresent(@NotNull Class<?> targetClass, @NotNull Class<? extends Annotation> annotationClass) {
        List<Field> finalFields = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                finalFields.add(field);
            }
        }
        return finalFields;
    }

    /**
     * 获取所有打了指定注解的字段（包括这个注解）
     *
     * @param targetClass     目标类
     * @param annotationClass 指定注解
     * @param <T>             指定注解的类型
     * @return 所有打了指定注解的字段及这个注解
     */
    public static <T extends Annotation> List<FieldExt<T>> getFieldsAndAnnotations(@NotNull Class<?> targetClass, @NotNull Class<T> annotationClass) {
        List<FieldExt<T>> fieldExtList = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationClass)) {
                T annotation = field.getDeclaredAnnotation(annotationClass);
                FieldExt<T> ext = new FieldExt<>(field, annotation);
                fieldExtList.add(ext);
            }
        }
        return fieldExtList;
    }

    /**
     * 获取字段的泛型类型
     *
     * @param field 字段对象
     * @return 泛型类型，如果字段没有泛型则抛出异常
     */
    public static Class<?>[] getParameterizedTypes(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            Type[] types = pType.getActualTypeArguments();
            Class<?>[] classes = new Class[types.length];
            for (int i = 0; i < types.length; i++) {
                classes[i] = (Class<?>) types[i];
            }
            return classes;
        } else {//不是泛型
            throw new IllegalStateException("field`s generic type is not a Parameterized type");
        }
    }

    /**
     * 获取字段的泛型类型
     *
     * @param field                  字段对象
     * @param parameterizedTypeIndex 第几个泛型(0-based)
     * @return 泛型类型，如果字段没有泛型则抛出异常
     */
    public static Class<?> getParameterizedType(Field field, int parameterizedTypeIndex) {
        Class<?>[] classes = getParameterizedTypes(field);
        return classes[parameterizedTypeIndex];
    }

    /**
     * 获取字段的泛型类型(如果有多个泛型，则固定获取第1个)
     *
     * @param field 字段对象
     * @return 泛型类型，如果字段没有泛型则抛出异常
     */
    public static Class<?> getParameterizedType(Field field) {
        Class<?>[] classes = getParameterizedTypes(field);
        return classes[0];
    }

    /**
     * 深度寻找字段（不断往父类上去找）
     *
     * @param clazz     目标类
     * @param fieldName 字段名
     * @return 字段对象
     * @throws NoSuchFieldException 最终还是找不到
     */
    public static Field getFieldDeep(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        if (clazz == Object.class) {
            throw new NoSuchFieldException(fieldName);
        }
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e0) {
            Class<?> superClass = clazz.getSuperclass();
            return getFieldDeep(superClass, fieldName);
        }
    }

    /**
     * 获取所有字段（包括父类的）
     *
     * @param clazz    起始类（包含）
     * @param endClass 终止类（不包含）
     * @return 所有字段
     */
    public static Field[] getAllFieldsDeep(Class<?> clazz, Class<?> endClass) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != endClass) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        Field[] f = new Field[fieldList.size()];
        return fieldList.toArray(f);
    }

    /**
     * 获取所有字段（包括父类的，一直查到Object类为止，不包括Object的字段）
     *
     * @param clazz 起始类
     * @return 所有字段
     */
    public static Field[] getAllFieldsDeep(Class<?> clazz) {
        return getAllFieldsDeep(clazz, Object.class);
    }

}
