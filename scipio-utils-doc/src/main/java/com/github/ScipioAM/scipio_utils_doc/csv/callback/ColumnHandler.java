package com.github.ScipioAM.scipio_utils_doc.csv.callback;

/**
 * 列的回调处理(对Writer无效)
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/9
 */
public interface ColumnHandler {

    /**
     * 每列的回调处理
     * @param columnStr 列字符串
     * @param columnIndex 列下标(0-based)
     * @param line 每行塑化剂
     * @param rowIndex 行下标(0-based)
     * @return true代表继续执行，false代表中断执行
     */
    boolean handle(String columnStr, int columnIndex, String line, int rowIndex);

}
