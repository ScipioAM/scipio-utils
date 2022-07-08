package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 序列化的{@link Predicate}
 *
 * @param <T> 入参
 * @since 2022/7/8
 */
public interface SePredict<T> extends Serializable, Predicate<T>, SeFunctional {
}
