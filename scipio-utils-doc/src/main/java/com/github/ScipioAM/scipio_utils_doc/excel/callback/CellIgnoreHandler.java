package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/10/11
 */
@FunctionalInterface
public interface CellIgnoreHandler {

    /**
     * 忽略处理（在convert之前）
     * @param cell 单元格对象
     * @param cellValue 该单元格的值
     * @param valueType 值的类型
     * @param targetType 后续要转换的类型
     * @return 返回true代表要忽略，false代表不能忽略
     */
    boolean ignore(Cell cell, Object cellValue, Class<?> valueType, Class<?> targetType);

}
