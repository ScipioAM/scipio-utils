package com.github.ScipioAM.scipio_utils_common.validation.annotation;

import java.lang.annotation.*;

/**
 * @author Alan Scipio
 * @date 2021/10/10
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Max {

    /**
     * 不能大于该值（可以等于）
     */
    long value();

    String message() default "";

}
