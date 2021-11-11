package com.github.ScipioAM.scipio_utils_doc.csv.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/9
 */
public class CSVRow {

    /** 切割时用的分隔符，一般默认是英文逗号 */
    private String delimiter;

    /** 行原始内容，写入时优先级最低 */
    private String line;

    /** 行内容（切割后），写入时优先级高于line */
    private List<String> columnArr;

    /** 行序号(0-based) */
    private int rowIndex;

    /** 切割出的行内容总数 */
    private int length;

    public CSVRow() {}

    public CSVRow(String delimiter, String line, String[] columnArr, int rowIndex) {
        this.delimiter = delimiter;
        this.line = line;
        this.rowIndex = rowIndex;
        setColumnArr(Arrays.asList(columnArr));
    }

    public CSVRow(String line, int rowIndex) {
        this.line = line;
        this.rowIndex = rowIndex;
    }

    @Override
    public String toString() {
        return "CSVRow{" +
                "delimiter='" + delimiter + '\'' +
                ", line='" + line + '\'' +
                ", columnArr=" + columnArr +
                ", rowIndex=" + rowIndex +
                ", length=" + length +
                '}';
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

    public List<String> getColumnArr() {
        return columnArr;
    }

    public String getColumn(int index) {
        return columnArr.get(index);
    }

    public void setColumnArr(List<String> columnArr) {
        this.columnArr = columnArr;
        if(columnArr != null) {
            this.length = columnArr.size();
        }
    }

    public void addColumn(String column) {
        if(columnArr == null) {
            columnArr = new ArrayList<>();
        }
        columnArr.add(column);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
