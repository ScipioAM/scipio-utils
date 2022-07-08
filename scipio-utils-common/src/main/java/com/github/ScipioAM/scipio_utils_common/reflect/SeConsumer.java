package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 序列化的{@link Consumer}
 *
 * @param <T> 入参
 * @since 2022/7/8
 */
public interface SeConsumer<T> extends Serializable, Consumer<T>, SeFunctional {

    /**
     * 如果实现是一个getter或setter方法，尝试根据方法名获取其属性名
     *
     * @return 属性名
     */
    default String getPropertyByMethod() {
        return ReflectUtil.beanMethodToProperty(getImplMethodName());
    }

}
