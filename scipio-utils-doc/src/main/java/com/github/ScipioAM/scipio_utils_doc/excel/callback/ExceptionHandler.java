package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel处理执行时的异常处理
 * @author Alan Scipio
 * @since 1.0.4
 * @date 2021/10/28
 */
@FunctionalInterface
public interface ExceptionHandler {

    void handle(Workbook workbook, ExcelIndex excelIndex, Exception e) throws Exception;

}
