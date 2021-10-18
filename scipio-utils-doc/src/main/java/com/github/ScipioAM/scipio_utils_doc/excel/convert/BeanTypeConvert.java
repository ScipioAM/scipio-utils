package com.github.ScipioAM.scipio_utils_doc.excel.convert;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 类型转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/17
 */
@FunctionalInterface
public interface BeanTypeConvert {

    /**
     * 类型转换
     * @param cell 转换的单元格对象
     * @param originalValue 原始值
     * @param originalType 原始值的类型
     * @param targetType 预期类型
     * @return 转换后的值
     */
    Object convert(Cell cell, Object originalValue, Class<?> originalType, Class<?> targetType) throws IllegalStateException;

}
