package com.github.ScipioAM.scipio_utils_common;

import java.util.Collection;

/**
 * 断言工具
 *
 * @author Alan Scipio
 * @date 2021/9/6
 * @since 1.0.2-p3
 */
public class AssertUtil {

    public static void notNull(String arg, String errMsg) {
        if (StringUtil.isNull(arg)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void notNull(String arg) {
        if (StringUtil.isNull(arg)) {
            throw new IllegalArgumentException("argument string is empty");
        }
    }

    public static void shouldNull(String arg, String errMsg) {
        if (StringUtil.isNotNull(arg)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void shouldNull(String arg) {
        if (StringUtil.isNotNull(arg)) {
            throw new IllegalArgumentException("argument string is not empty");
        }
    }

    public static void notNull(Object arg, String errMsg) {
        if (arg == null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void notNull(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("argument object is null");
        }
    }

    public static void shouldNull(Object arg, String errMsg) {
        if (arg != null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void shouldNull(Object arg) {
        if (arg != null) {
            throw new IllegalArgumentException("argument object is not null");
        }
    }

    public static void collectionNotEmpty(Collection<?> arg, String errMsg) {
        if (arg == null || arg.size() <= 0) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public static void collectionNotEmpty(Collection<?> arg) {
        if (arg == null) {
            throw new IllegalArgumentException("argument collection is null");
        } else if (arg.size() <= 0) {
            throw new IllegalArgumentException("argument collection is empty");
        }
    }

    /**
     * 检查类型是否合法
     *
     * @param checkInstance 要检查的实例
     * @param expectType    预期类型
     */
    public static void checkType(Object checkInstance, Class<?> expectType) {
        if (checkInstance.getClass() != expectType) {
            throw new IllegalStateException("Expect type [" + expectType + "], bus actual type is [" + checkInstance.getClass() + "], for check instance[" + checkInstance + "]");
        }
    }

}
