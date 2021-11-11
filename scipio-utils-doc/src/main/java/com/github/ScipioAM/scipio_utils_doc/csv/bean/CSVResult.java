package com.github.ScipioAM.scipio_utils_doc.csv.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/9
 */
public class CSVResult {

    /** 读取的行对象 */
    private List<CSVRow> rows;

    /** 读取的行原始内容 */
    private List<String> originalLines;

    @Override
    public String toString() {
        return "CSVResult{" +
                "rows=" + rows +
                ", originalLines=" + originalLines +
                '}';
    }

    public List<CSVRow> getRows() {
        return rows;
    }

    public CSVRow getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public String getColumn(int rowIndex, int columnIndex) {
        CSVRow row = rows.get(rowIndex);
        return row.getColumn(columnIndex);
    }

    public void setRows(List<CSVRow> rows) {
        this.rows = rows;
    }

    public void addRow(CSVRow row) {
        if(rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(row);
    }

    public List<String> getOriginalLines() {
        return originalLines;
    }

    public String getOriginalLine(int lineIndex) {
        return originalLines.get(lineIndex);
    }

    public void setOriginalLines(List<String> originalLines) {
        this.originalLines = originalLines;
    }

    public void addOriginalLine(String line) {
        if(originalLines == null) {
            originalLines = new ArrayList<>();
        }
        originalLines.add(line);
    }

}
