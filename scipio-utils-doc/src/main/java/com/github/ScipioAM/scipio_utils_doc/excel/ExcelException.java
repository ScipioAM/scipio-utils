package com.github.ScipioAM.scipio_utils_doc.excel;

/**
 * Excel处理时的异常
 * @author Alan Scipio
 * @date 2021/11/1
 */
public class ExcelException extends RuntimeException{

    private String sheetName;

    private Integer rowIndex;

    private Integer cellIndex;

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelException setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public ExcelException setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public ExcelException setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
        return this;
    }
}
