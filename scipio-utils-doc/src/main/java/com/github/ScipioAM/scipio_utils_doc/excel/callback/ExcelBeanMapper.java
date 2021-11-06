package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelException;
import org.apache.poi.ss.usermodel.Row;

/**
 * Excel - JavaBean 转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public interface ExcelBeanMapper<T> {

    /**
     * 映射：excel -> JavaBean
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数(加上了起始行号)
     * @param beanIndex JavaBean在list中的下标
     * @return 通过映射，新生成的一个JavaBean实例
     */
    default T mappingExcel2Bean(Row row, int rowIndex, int rowLength, int beanIndex) throws ExcelException {
        return null;
    }

    /**
     * 映射：JavaBean -> excel
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数(加上了起始列号)
     * @param bean JavaBean实例(将它的数据写入excel)
     * @param beanIndex JavaBean在list中的下标
     */
    default void mappingBean2Excel(Row row, int rowIndex, int rowLength, T bean, int beanIndex) throws ExcelException {
    }

}
