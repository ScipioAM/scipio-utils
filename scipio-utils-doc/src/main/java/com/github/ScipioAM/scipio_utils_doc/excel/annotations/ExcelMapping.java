package com.github.ScipioAM.scipio_utils_doc.excel.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 定义 Excel - JavaBean 映射关系的注解
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/16
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface ExcelMapping {

    /**
     * 映射的列索引：
     * <p>该字段对应excel行中的第几列(0-based)，为-1则代表不使用</p>
     */
    int cellIndex() default -1;

    /**
     * 映射的行索引：
     * <p>该字段对应excel行中的第几行(0-based)，为-1则代表不使用</p>
     */
    int rowIndex() default -1;

}
