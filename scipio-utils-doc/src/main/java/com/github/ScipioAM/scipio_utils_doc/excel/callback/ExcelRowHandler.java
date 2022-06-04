package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author Alan Scipio
 * @date 2021/9/8
 * @since 1.0.2-p3
 */
@FunctionalInterface
public interface ExcelRowHandler {

    /**
     * 对1个Sheet中每行的处理
     *
     * @param row       行对象
     * @param rowIndex  行索引(0-based)
     * @param rowLength 要扫描的总行数
     * @return true代表继续执行，false代表中断对每行的扫描
     */
    boolean handle(Row row, int rowIndex, int rowLength);

}
