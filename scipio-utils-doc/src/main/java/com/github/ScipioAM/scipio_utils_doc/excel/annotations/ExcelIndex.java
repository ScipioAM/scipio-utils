package com.github.ScipioAM.scipio_utils_doc.excel.annotations;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 注解版的{@link com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex}
 *      <p>注：优先级低于显式传参的{@link com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex}</p>
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/27
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface ExcelIndex {

    /** Sheet索引(0-based)，为负数则代表null - 优先于sheetName使用 */
    int sheetIndex() default -1;

    /** Sheet名 - 落后于sheetIndex使用 */
    String sheetName() default "";

    /** 开始行索引(0-based) */
    int rowStartIndex() default 0;

    /** 要扫描的行数 */
    int rowLength() default 0;

    /** 扫描行数时的步长 */
    int rowStep() default 1;

    /** 开始列索引(0-based) */
    int columnStartIndex() default 0;

    /** 要扫描的列数 */
    int columnLength() default 0;

    /** 扫描列数时的步长 */
    int columnStep() default 1;

    /** 使用{@link Sheet#getLastRowNum()}来自行判断rowLength，优先于usePhysicalNumberOfRows */
    boolean useLastNumberOfRows() default false;

    /** 使用{@link Row#getLastCellNum()}来自行判断columnLength，优先于usePhysicalNumberOfCells */
    boolean useLastNumberOfCells() default false;

    /** 使用{@link Sheet#getPhysicalNumberOfRows()}来自行判断rowLength */
    boolean usePhysicalNumberOfRows() default false;

    /** 使用{@link Row#getPhysicalNumberOfCells()}来自行判断columnLength */
    boolean usePhysicalNumberOfCells() default false;

}
