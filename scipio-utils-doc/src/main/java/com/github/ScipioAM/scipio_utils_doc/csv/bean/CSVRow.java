package com.github.ScipioAM.scipio_utils_doc.csv.bean;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/9
 */
public class CSVRow {

    private String delimiter;

    private String line;

    private String[] columnArr;

    private int rowIndex;

    public CSVRow() {}

    public CSVRow(String delimiter, String line, String[] columnArr, int rowIndex) {
        this.delimiter = delimiter;
        this.line = line;
        this.columnArr = columnArr;
        this.rowIndex = rowIndex;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String[] getColumnArr() {
        return columnArr;
    }

    public String getColumn(int index) {
        if(columnArr == null) {
            return null;
        }
        return columnArr[index];
    }

    public void setColumnArr(String[] columnArr) {
        this.columnArr = columnArr;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
