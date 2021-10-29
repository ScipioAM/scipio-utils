package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 开始处理之前的监听回调
 * @author Alan Scipio
 * @since 1.0.5
 * @date 2021/10/29
 */
@FunctionalInterface
public interface ExcelStartListener {

    /**
     * 开始处理之前的监听回调
     * @param workbook 工作簿对象
     * @param sheet 工作表对象
     * @param excelIndex 调用者定义的处理范围
     * @return true代表继续执行，false代表终止后续所有处理
     */
    boolean firstOperation(Workbook workbook, Sheet sheet, ExcelIndex excelIndex);

}
