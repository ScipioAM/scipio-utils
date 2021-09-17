package com.github.ScipioAM.scipio_utils_doc.excel.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel扫描时的相关配置
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
public class ExcelIndex {

    /** Sheet索引(0-based) - 优先于sheetName使用 */
    private Integer sheetIndex;

    /** Sheet名 - 落后于sheetIndex使用 */
    private String sheetName;

    /** 开始行索引(0-based) */
    @NotNull(message = "rowStartIndex can not be null")
    @Min(value = 0, message = "rowStartIndex can not less then 0")
    private Integer rowStartIndex = 0;

    /** 要扫描的行数 */
    @Min(value = 0, message = "rowLength can not less then 0")
    private Integer rowLength;

    /** 扫描行数时的步长 */
    @NotNull(message = "rowStep can not be null")
    @Min(value = 0, message = "rowStep can not less then 0")
    private Integer rowStep = 1;

    /** 开始列索引(0-based) */
    @NotNull(message = "columnStartIndex can not be null")
    @Min(value = 0, message = "columnStartIndex can not less then 0")
    private Integer columnStartIndex = 0;

    /** 要扫描的列数 */
    @Min(value = 0, message = "columnLength can not greater then 16384")
    private Integer columnLength;

    /** 扫描列数时的步长 */
    @NotNull(message = "columnStep can not be null")
    @Min(value = 0, message = "columnStep can not less then 0")
    private Integer columnStep = 1;

    /** 使用{@link Sheet#getLastRowNum()}来自行判断rowLength，优先于usePhysicalNumberOfRows */
    private boolean useLastNumberOfRows = false;

    /** 使用{@link Row#getLastCellNum()}来自行判断columnLength，优先于usePhysicalNumberOfCells */
    private boolean useLastNumberOfCells = false;

    /** 使用{@link Sheet#getPhysicalNumberOfRows()}来自行判断rowLength */
    private boolean usePhysicalNumberOfRows = false;

    /** 使用{@link Row#getPhysicalNumberOfCells()}来自行判断columnLength */
    private boolean usePhysicalNumberOfCells = false;

    public ExcelIndex() {}

    public ExcelIndex(Integer sheetIndex, Integer rowStartIndex, Integer rowLength, Integer rowStep, Integer columnStartIndex, Integer columnLength, Integer columnStep) {
        this.sheetIndex = sheetIndex;
        this.rowStartIndex = rowStartIndex;
        this.rowLength = rowLength;
        this.rowStep = rowStep;
        this.columnStartIndex = columnStartIndex;
        this.columnLength = columnLength;
        this.columnStep = columnStep;
    }

    public ExcelIndex(String sheetName, Integer rowStartIndex, Integer rowLength, Integer rowStep, Integer columnStartIndex, Integer columnLength, Integer columnStep) {
        this.sheetName = sheetName;
        this.rowStartIndex = rowStartIndex;
        this.rowLength = rowLength;
        this.rowStep = rowStep;
        this.columnStartIndex = columnStartIndex;
        this.columnLength = columnLength;
        this.columnStep = columnStep;
    }

    public ExcelIndex(Integer sheetIndex, Integer rowStartIndex, Integer rowLength, Integer columnStartIndex, Integer columnLength) {
        this.sheetIndex = sheetIndex;
        this.rowStartIndex = rowStartIndex;
        this.rowLength = rowLength;
        this.columnStartIndex = columnStartIndex;
        this.columnLength = columnLength;
    }

    public ExcelIndex(String sheetName, Integer rowStartIndex, Integer rowLength, Integer columnStartIndex, Integer columnLength) {
        this.sheetName = sheetName;
        this.rowStartIndex = rowStartIndex;
        this.rowLength = rowLength;
        this.columnStartIndex = columnStartIndex;
        this.columnLength = columnLength;
    }

    public ExcelIndex(Integer sheetIndex, Integer rowLength, Integer columnLength) {
        this.sheetIndex = sheetIndex;
        this.rowLength = rowLength;
        this.columnLength = columnLength;
    }

    public ExcelIndex(String sheetName, Integer rowLength, Integer columnLength) {
        this.sheetName = sheetName;
        this.rowLength = rowLength;
        this.columnLength = columnLength;
    }

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public ExcelIndex setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelIndex setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public Integer getRowStartIndex() {
        return rowStartIndex;
    }

    public ExcelIndex setRowStartIndex(Integer rowStartIndex) {
        this.rowStartIndex = rowStartIndex;
        return this;
    }

    public Integer getRowLength() {
        return rowLength;
    }

    public ExcelIndex setRowLength(Integer rowLength) {
        this.rowLength = rowLength;
        return this;
    }

    public Integer getRowStep() {
        return rowStep;
    }

    public ExcelIndex setRowStep(Integer rowStep) {
        this.rowStep = rowStep;
        return this;
    }

    public Integer getColumnStartIndex() {
        return columnStartIndex;
    }

    public ExcelIndex setColumnStartIndex(Integer columnStartIndex) {
        this.columnStartIndex = columnStartIndex;
        return this;
    }

    public Integer getColumnLength() {
        return columnLength;
    }

    public ExcelIndex setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
        return this;
    }

    public Integer getColumnStep() {
        return columnStep;
    }

    public ExcelIndex setColumnStep(Integer columnStep) {
        this.columnStep = columnStep;
        return this;
    }

    public boolean usePhysicalNumberOfRows() {
        return usePhysicalNumberOfRows;
    }

    public ExcelIndex setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        this.usePhysicalNumberOfRows = usePhysicalNumberOfRows;
        return this;
    }

    public boolean usePhysicalNumberOfCells() {
        return usePhysicalNumberOfCells;
    }

    public ExcelIndex setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        this.usePhysicalNumberOfCells = usePhysicalNumberOfCells;
        return this;
    }

    public boolean useLastNumberOfRows() {
        return useLastNumberOfRows;
    }

    public ExcelIndex setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        this.useLastNumberOfRows = useLastNumberOfRows;
        return this;
    }

    public boolean useLastNumberOfCells() {
        return useLastNumberOfCells;
    }

    public ExcelIndex setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        this.useLastNumberOfCells = useLastNumberOfCells;
        return this;
    }
}
