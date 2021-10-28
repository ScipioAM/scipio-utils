package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelBeanReader;
import com.github.ScipioAM.scipio_utils_doc.excel.ExcelBeanWriter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * {@link ExcelBeanReader}或{@link ExcelBeanWriter}对每个JavaBean处理时的监听回调
 * @param <T> JavaBean的具体类型
 * @author Alan Scipio
 * @since 1.0.4
 * @date 2021/10/28
 */
@FunctionalInterface
public interface BeanListener<T> {

    /**
     * 对每个JavaBean处理时的监听回调
     * @param isReadMode 当前是否为读取；为true代表当前是读取，为false代表当前是写入
     * @param bean JavaBean（读取后，或写入前）
     * @param cell 单元格对象
     * @param rowLength 要处理的总行数
     * @param columnLength 要处理的总列数
     * @return true代表继续执行，false代表中断对每个单元格的扫描
     */
    boolean onHandle(boolean isReadMode, T bean, Cell cell, int rowLength, int columnLength);

}
