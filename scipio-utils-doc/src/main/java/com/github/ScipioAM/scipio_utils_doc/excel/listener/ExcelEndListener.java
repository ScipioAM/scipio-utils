package com.github.ScipioAM.scipio_utils_doc.excel.listener;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel操作执行结束时的回调
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public interface ExcelEndListener {

    void lastOperation(Workbook workbook);

    //==================================================================================================================

    /**
     * 默认实现，关闭工作簿对象
     */
    ExcelEndListener SIMPLE_CLOSE = workbook -> {
        try {
            if(workbook != null) {
                workbook.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    };

}
