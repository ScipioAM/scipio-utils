package com.github.ScipioAM.scipio_utils_doc.csv.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/9
 */
public class CSVResult {

    private List<CSVRow> rows;

    private List<String> originalLines;

    public List<CSVRow> getRows() {
        return rows;
    }

    public CSVRow getRow(int rowIndex) {
        if(rows == null) {
            return null;
        }
        return rows.get(rowIndex);
    }

    public String getColumn(int rowIndex, int columnIndex) {
        if(rows == null) {
            return null;
        }
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
        if(originalLines == null) {
            return null;
        }
        return originalLines.get(lineIndex);
    }

    public void setOriginalLines(List<String> originalLines) {
        this.originalLines = originalLines;
    }

}
