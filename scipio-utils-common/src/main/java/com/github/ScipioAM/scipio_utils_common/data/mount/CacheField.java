package com.github.ScipioAM.scipio_utils_common.data.mount;

import java.lang.annotation.*;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CacheField {

    /**
     * 多个缓存字段时，需要指定id
     */
    int id() default MountedCache.DEFAULT_ID;

}
