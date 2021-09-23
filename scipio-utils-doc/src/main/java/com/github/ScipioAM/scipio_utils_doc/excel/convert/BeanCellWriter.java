package com.github.ScipioAM.scipio_utils_doc.excel.convert;

import org.apache.poi.ss.usermodel.Cell;

/**
 * JavaBean写入Excel单元格
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/23
 */
@FunctionalInterface
public interface BeanCellWriter {

    /**
     * 将值写入单元格
     * @param cell 单元格对象
     * @param value 要写入的值
     * @param valueType 要写入值的类型
     * @throws IllegalStateException 未知的写入值类型
     */
    void writeIntoCell(Cell cell, Object value, Class<?> valueType, NullWritingPolicy policy) throws IllegalStateException;

    /**
     * 将值写入单元格 - 当value为null时跳过
     */
    default void writeIntoCell(Cell cell, Object value, Class<?> valueType) throws IllegalStateException {
        writeIntoCell(cell, value, valueType, NullWritingPolicy.IGNORE);
    }

    /**
     * 当value为null时的值如何写入
     */
    enum NullWritingPolicy {
        //忽略不写入（不会覆盖单元格原有的值）
        IGNORE,
        //写入为空白单元格（覆盖单元格原有的值）
        WRITE_BLANK
    }

}
