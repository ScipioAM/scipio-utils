package com.github.ScipioAM.scipio_utils_doc.excel.listener;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
@FunctionalInterface
public interface ExcelRowHandler {

    /**
     * 对1个Sheet中每行的处理
     * @param sheet Sheet对象
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @return true代表继续执行，false代表中断对每行的扫描
     */
    boolean handle(Sheet sheet, Row row, int rowIndex);

}
