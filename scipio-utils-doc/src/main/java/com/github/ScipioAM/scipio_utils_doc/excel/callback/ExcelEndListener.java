package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel操作执行结束时的回调
 *
 * @author Alan Scipio
 * @date 2021/9/10
 * @since 1.0.2-p3
 */
public interface ExcelEndListener {

    void lastOperation(Workbook workbook, ExcelIndex excelIndex);

    //==================================================================================================================

    /**
     * 默认实现，关闭工作簿对象
     */
    ExcelEndListener SIMPLE_CLOSE = (workbook, excelIndex) -> {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

}
