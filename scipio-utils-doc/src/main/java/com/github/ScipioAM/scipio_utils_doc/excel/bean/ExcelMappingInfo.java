package com.github.ScipioAM.scipio_utils_doc.excel.bean;

import com.github.ScipioAM.scipio_utils_common.annotations.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * Excel -> JavaBean 映射关系(暂未用到，后续可能考虑使用)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
@Deprecated
public class ExcelMappingInfo {

    @NotNull
    private Integer cellIndex;

    @Nullable
    private Integer rowIndex;

    @NotNull
    private String fieldName;

    public ExcelMappingInfo() {}

    public ExcelMappingInfo(int cellIndex, String fieldName) {
        this.cellIndex = cellIndex;
        this.fieldName = fieldName;
    }

    public ExcelMappingInfo(int cellIndex, int rowIndex, String fieldName) {
        this.cellIndex = cellIndex;
        this.rowIndex = rowIndex;
        this.fieldName = fieldName;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
