package com.github.ScipioAM.scipio_utils_doc.csv;

import java.lang.annotation.*;

/**
 * CSV一列
 * @author Alan Scipio
 * @since 1.0.9 2021/12/30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CSVCell {

    /**
     * CSV文件的标题
     */
    String title();

    /**
     * 在CSV同一行中的先后序号（0-based）
     */
    int sort();

}
