package com.github.ScipioAM.scipio_utils_common.validation.annotation;

import java.lang.annotation.*;

/**
 * @author Alan Scipio
 * @date 2021/10/10
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface NotNull {

    /**
     * 检查不通过时抛出的异常信息
     */
    String message() default "";

}
