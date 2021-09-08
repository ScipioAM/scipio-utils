package com.github.ScipioAM.scipio_utils_doc.listener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
@FunctionalInterface
public interface ExcelCellHandler {

    /**
     * 对1行中每个单元格的处理
     * @param row 行对象
     * @param cell 单元格对象
     * @param rowIndex 行索引(0-based)
     * @param columnIndex 列索引(0-based)
     * @return true代表继续执行，false代表中断对每个单元格的扫描
     */
    boolean handle(Row row, Cell cell, int rowIndex, int columnIndex);

}
