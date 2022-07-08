package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 序列化的{@link Supplier}
 *
 * @param <T> 入参
 * @since 2022/7/8
 */
public interface SeSupplier<T> extends Serializable, Supplier<T>, SeFunctional {
}
