package com.github.ScipioAM.scipio_utils_common.reflect;

import net.jodah.typetools.TypeResolver;

/**
 * Class类型相关工具类
 * @author Alan Min
 * @since 2021/7/10
 */
@Deprecated
public class TypeHelper {

    /**
     * 获取interface上泛型的具体类型
     * @param obj 要获取的类对象
     * @param interfaceIndex 接口索引(第几个接口，0-based index)
     * @param typeIndex 泛型索引(第几个泛型，0-based index)
     * @return 泛型的具体类型
     */
    public static Class<?> getGenericInterface(Object obj, int interfaceIndex, int typeIndex) {
        return ReflectUtil.getGenericInterface(obj, interfaceIndex, typeIndex);
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
        return ReflectUtil.getGenericClass(obj, index);
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
        return ReflectUtil.resolveRawArguments(genericClass, object);
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object, int typeIndex) {
        return ReflectUtil.resolveRawArgument(genericClass, object, typeIndex);
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object) {
        return ReflectUtil.resolveRawArgument(genericClass, object);
    }

}
