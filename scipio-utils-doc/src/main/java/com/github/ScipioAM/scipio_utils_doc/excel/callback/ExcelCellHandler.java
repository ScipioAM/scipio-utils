package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
@FunctionalInterface
public interface ExcelCellHandler {

    /**
     * 对1行中每个单元格的处理
     * @param cell 单元格对象
     * @param rowIndex 行索引(0-based)
     * @param columnIndex 列索引(0-based)
     * @param rowLength 要扫描的总行数
     * @param columnLength 要扫描的总列数
     * @return true代表继续执行，false代表中断对每个单元格的扫描
     */
    boolean handle(Cell cell, int rowIndex, int columnIndex, int rowLength, int columnLength);

}
