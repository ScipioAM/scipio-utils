package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 序列化的{@link Function}
 *
 * @param <P> 入参
 * @param <R> 返回值
 * @since 2022/7/8
 */
@FunctionalInterface
public interface SeFunction<P, R> extends Serializable, Function<P, R>, SeFunctional {

    /**
     * 如果实现是一个getter或setter方法，尝试根据方法名获取其属性名
     *
     * @return 属性名
     */
    default String getPropertyByMethod() {
        return ReflectUtil.beanMethodToProperty(getImplMethodName());
    }

}
