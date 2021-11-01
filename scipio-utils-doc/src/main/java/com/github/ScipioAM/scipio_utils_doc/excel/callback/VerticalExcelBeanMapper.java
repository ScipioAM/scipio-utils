package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelException;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * 垂直进行读取的{@link ExcelBeanMapper}
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/27
 */
public abstract class VerticalExcelBeanMapper<T> extends BaseExcelBeanMapper<T> {

    /**
     * 前期准备映射信息
     */
    public abstract void prepareMappingInfo();

    /**
     * 映射：excel -> JavaBean
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数
     * @param columnStartIndex 起始列号
     * @param columnLength 要读取的总列数(加上了起始列号)
     * @param beanList javaBean列表（映射读取的总结果）
     */
    public void mappingExcel2Bean(Row row, int rowIndex, int rowLength, int columnStartIndex, int columnLength, List<T> beanList) throws ExcelException {
    }

}
