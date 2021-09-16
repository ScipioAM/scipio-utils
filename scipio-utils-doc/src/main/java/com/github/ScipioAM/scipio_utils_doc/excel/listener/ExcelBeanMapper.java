package com.github.ScipioAM.scipio_utils_doc.excel.listener;

import org.apache.poi.ss.usermodel.Row;

/**
 * Excel - JavaBean 转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
@FunctionalInterface
public interface ExcelBeanMapper<T> {

    /**
     * 指定Excel里数据与JavaBean的对应关系
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @return JavaBean实例
     */
    T mapping(Row row, int rowIndex) throws Exception;

}
