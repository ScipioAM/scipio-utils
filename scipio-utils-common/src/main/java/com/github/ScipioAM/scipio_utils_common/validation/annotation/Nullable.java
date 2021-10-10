package com.github.ScipioAM.scipio_utils_common.validation.annotation;

import java.lang.annotation.*;

/**
 * 为了标记是否可以为null
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {
}
