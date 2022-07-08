package com.github.ScipioAM.scipio_utils_common.reflect;

import net.jodah.typetools.TypeResolver;

import java.lang.reflect.*;
import java.util.Locale;

/**
 * @author Alan Scipio
 * @since 2022/6/13
 */
public class ReflectUtil extends FieldUtil {

    private ReflectUtil() {
    }

    /**
     * 执行getter方法
     *
     * @param field    目标字段
     * @param instance 实例对象
     */
    public static Object doGetter(Field field, Object instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getterName = getterMethodName(field.getName());
        Method method = instance.getClass().getMethod(getterName);
        return method.invoke(instance);
    }

    /**
     * 执行setter方法
     *
     * @param field    目标字段
     * @param instance 实例对象
     * @param value    要set的值
     */
    public static void doSetter(Field field, Object instance, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        Class<?> valueType = value.getClass();
        if (fieldType != valueType) {
            throw new IllegalArgumentException("required type:[" + fieldType.getName() + "], provided type:[" + valueType.getName() + "]");
        }
        String setterName = setterMethodName(field.getName());
        Method method = instance.getClass().getMethod(setterName);
        method.invoke(value);
    }

    /**
     * 构建getter方法的名称
     *
     * @param fieldName 字段名
     */
    public static String getterMethodName(String fieldName) {
        return methodName("get", fieldName);
    }

    /**
     * 构建setter方法的名称
     *
     * @param fieldName 字段名
     */
    public static String setterMethodName(String fieldName) {
        return methodName("set", fieldName);
    }

    /**
     * 拼凑getter\setter方法的名称
     *
     * @param prefix    前缀
     * @param fieldName 字段名
     */
    private static String methodName(String prefix, String fieldName) {
        char[] arr = fieldName.toCharArray();
        StringBuilder methodName = new StringBuilder()
                .append(prefix)
                .append(Character.toUpperCase(arr[0]));
        for (int i = 1; i < arr.length; i++) {
            methodName.append(arr[i]);
        }
        return methodName.toString();
    }

    /**
     * 根据方法名（getter或setter方法），获取属性名
     *
     * @param methodName 方法名
     * @return 推测的属性名
     * @author Clinton Begin (org.apache.ibatis.reflection.property.PropertyNamer)
     */
    public static String beanMethodToProperty(String methodName) {
        if (methodName.startsWith("is")) {
            methodName = methodName.substring(2);
        } else if (methodName.startsWith("get") || methodName.startsWith("set")) {
            methodName = methodName.substring(3);
        } else {
            throw new IllegalStateException("Error parsing property name '" + methodName + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (methodName.length() == 1 || (methodName.length() > 1 && !Character.isUpperCase(methodName.charAt(1)))) {
            methodName = methodName.substring(0, 1).toLowerCase(Locale.ENGLISH) + methodName.substring(1);
        }

        return methodName;
    }

    //==================================================================================================================

    /**
     * 获取interface上泛型的具体类型
     *
     * @param obj            要获取的类对象
     * @param interfaceIndex 接口索引(第几个接口，0-based index)
     * @param typeIndex      泛型索引(第几个泛型，0-based index)
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
        return getGenericInterface(obj, 0, 0);
    }

    /**
     * 获取class上泛型的具体类型
     *
     * @param obj   要获取的类对象
     * @param index 泛型索引(第几个泛型，0-based index)
     * @return 泛型的具体类型
     */
    public static Class<?> getGenericClass(Object obj, int index) {
        Type type = obj.getClass().getGenericSuperclass();
        return checkAndGetType(type, index);
    }

    /**
     * 获取class上泛型的具体类型
     * <p>默认第1个泛型</p>
     */
    public static Class<?> getGenericClass(Object obj) {
        return getGenericClass(obj, 0);
    }

    /**
     * 使用{@link TypeResolver}解析目标对象的泛型类型，支持解析lambda表达式对象的泛型类型
     *
     * @param genericClass 含有泛型的类型（最终要解析谁）
     * @param object       解析的源头（比如实现\继承了genericClass的最终子类的实例，或是lambda表达式）
     * @return 泛型实际的类型
     */
    public static Class<?>[] resolveRawArguments(Class<?> genericClass, Object object) {
        return TypeResolver.resolveRawArguments(genericClass, object.getClass());
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object, int typeIndex) {
        return resolveRawArguments(genericClass, object)[typeIndex];
    }

    public static Class<?> resolveRawArgument(Class<?> genericClass, Object object) {
        return resolveRawArguments(genericClass, object)[0];
    }

    /**
     * 检查并获取具体类型
     *
     * @param type  类型对象
     * @param index 类型索引
     * @return 具体类型
     */
    private static Class<?> checkAndGetType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkAndGetType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new RuntimeException("Expected a Class: java.lang.reflect.ParameterizedType," + " but <" + type + "> is of type " + className);
        }
    }

}
