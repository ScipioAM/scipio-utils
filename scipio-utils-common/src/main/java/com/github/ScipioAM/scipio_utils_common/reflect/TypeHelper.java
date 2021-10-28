package com.github.ScipioAM.scipio_utils_common.reflect;

import net.jodah.typetools.TypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class类型相关工具类
 * @author Alan Min
 * @since 2021/7/10
 */
public class TypeHelper {

    /**
     * 获取interface上泛型的具体类型
     * @param obj 要获取的类对象
     * @param interfaceIndex 接口索引(第几个接口，0-based index)
     * @param typeIndex 泛型索引(第几个泛型，0-based index)
     * @return 泛型的具体类型
     */
    public static Class<?> getGenericInterface(Object obj, int interfaceIndex, int typeIndex) {
        Type[] types = obj.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[interfaceIndex];
        Type type = parameterizedType.getActualTypeArguments()[typeIndex];
        return checkAndGetType(type, typeIndex);

    }

    /**
     * 获取interface上泛型的具体类型
     * <p>默认第1个接口的第1个泛型</p>
     */
    public static Class<?> getGenericInterface(Object obj) {
        return getGenericInterface(obj,0,0);
    }

    //==================================================================================================================

    /**
     * 获取class上泛型的具体类型
     * @param obj 要获取的类对象
     * @param index 泛型索引(第几个泛型，0-based index)
     * @return 泛型的具体类型
     */
    public static Class<?> getGenericClass(Object obj, int index) {
        Type type = obj.getClass().getGenericSuperclass();
        return checkAndGetType(type,index);
    }

    /**
     * 获取class上泛型的具体类型
     * <p>默认第1个泛型</p>
     */
    public static Class<?> getGenericClass(Object obj) {
        return getGenericClass(obj,0);
    }

    //==================================================================================================================

    /**
     * 使用{@link TypeResolver}解析目标对象的泛型类型，支持解析lambda表达式对象的泛型类型
     * @param genericClass 含有泛型的类型（最终要解析谁）
     * @param object 解析的源头（比如实现\继承了genericClass的最终子类的实例，或是lambda表达式）
     * @return 泛型实际的类型
     */
    public static Class<?>[] resolveRawArguments(Class<?> genericClass, Object object) {
        return TypeResolver.resolveRawArguments(genericClass, object.getClass());
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object, int typeIndex) {
        return resolveRawArguments(genericClass,object)[typeIndex];
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object) {
        return resolveRawArguments(genericClass,object)[0];
    }

    //==================================================================================================================

    /**
     * 检查并获取具体类型
     * @param type 类型对象
     * @param index 类型索引
     * @return 具体类型
     */
    private static Class<?> checkAndGetType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkAndGetType(t, index);
        }
        else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new RuntimeException("Expected a Class: java.lang.reflect.ParameterizedType," + " but <" + type + "> is of type " + className);
        }
    }

}
