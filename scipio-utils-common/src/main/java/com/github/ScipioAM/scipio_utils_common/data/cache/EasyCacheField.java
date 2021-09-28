package com.github.ScipioAM.scipio_utils_common.data.cache;

import java.lang.annotation.*;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyCacheField {

    int order() default 0;

}
