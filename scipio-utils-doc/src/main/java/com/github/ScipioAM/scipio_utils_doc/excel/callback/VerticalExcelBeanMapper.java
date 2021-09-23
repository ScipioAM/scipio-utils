package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import org.apache.poi.ss.usermodel.Row;

/**
 * TODO 垂直进行读取的{@link ExcelBeanMapper}
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/23
 */
public class VerticalExcelBeanMapper<T> {

    public T mappingExcel2Bean(Row row, int rowIndex, int rowLength, T bean) throws Exception {
        return null;
    }

    public void mappingBean2Excel(Row row, int rowIndex, int rowLength, T bean) throws Exception {

    }

}
