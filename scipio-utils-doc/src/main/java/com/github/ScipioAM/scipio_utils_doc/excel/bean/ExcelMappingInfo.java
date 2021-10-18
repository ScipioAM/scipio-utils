package com.github.ScipioAM.scipio_utils_doc.excel.bean;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.Nullable;

/**
 * Excel -> JavaBean 映射关系(暂未用到，后续可能考虑使用)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelMappingInfo {

    /**
     * 映射的列索引：
     * <p>该字段对应excel行中的第几列(0-based)</p>
     */
    @NotNull
    private Integer cellIndex;

    /**
     * 映射的行索引：
     * <p>该字段对应excel行中的第几行(0-based)，为-1则代表不使用</p>
     */
    @Nullable
    private Integer rowIndex;

    /**
     * 对应的字段名称
     */
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

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
