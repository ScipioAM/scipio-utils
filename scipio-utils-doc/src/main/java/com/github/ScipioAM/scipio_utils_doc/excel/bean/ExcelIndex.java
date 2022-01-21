package com.github.ScipioAM.scipio_utils_doc.excel.bean;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.Min;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
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
    private Integer rowStartIndex;

    /** 要扫描的行数 */
    @Min(value = 0, message = "rowLength can not less then 0")
    private Integer rowLength;

    /** 扫描行数时的步长 */
    @NotNull(message = "rowStep can not be null")
    @Min(value = 1, message = "rowStep can not less then 1")
    private Integer rowStep;

    /** 开始列索引(0-based) */
    @NotNull(message = "columnStartIndex can not be null")
    @Min(value = 0, message = "columnStartIndex can not less then 0")
    private Integer columnStartIndex;

    /** 要扫描的列数 */
    @Min(value = 0, message = "columnLength can not greater then 16384")
    private Integer columnLength;

    /** 扫描列数时的步长 */
    @NotNull(message = "columnStep can not be null")
    @Min(value = 1, message = "columnStep can not less then 1")
    private Integer columnStep;

    /** 使用{@link Sheet#getLastRowNum()}来自行判断rowLength，优先于usePhysicalNumberOfRows */
    private Boolean useLastNumberOfRows;

    /** 使用{@link Row#getLastCellNum()}来自行判断columnLength，优先于usePhysicalNumberOfCells */
    private Boolean useLastNumberOfCells;

    /** 使用{@link Sheet#getPhysicalNumberOfRows()}来自行判断rowLength */
    private Boolean usePhysicalNumberOfRows;

    /** 使用{@link Row#getPhysicalNumberOfCells()}来自行判断columnLength */
    private Boolean usePhysicalNumberOfCells;

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

    /**
     * 自我合法性校验
     * @param isReader 是否为读取模式
     * @param needCheckColLength 是否需要检查列长度
     */
    public void checkSelf(boolean isReader, boolean needCheckColLength) throws NullPointerException, IllegalArgumentException {
        if(rowStartIndex == null) {
            throw new NullPointerException("rowStartIndex can not be null");
        }
        else if(rowStartIndex < 0) {
            throw new IllegalArgumentException("rowStartIndex can not less then 0");
        }

        if(!useLastNumberOfRows && !usePhysicalNumberOfRows) {
            if(rowLength == null) {
                throw new NullPointerException("rowLength can not be null");
            }
            else if(rowLength < 0) {
                throw new IllegalArgumentException("rowLength can not less then 0");
            }
        }

        if(rowStep == null) {
            throw new NullPointerException("rowStep can not be null");
        }
        else if(rowStep < 1) {
            throw new IllegalArgumentException("rowStep can not less then 1");
        }

        if(columnStartIndex == null) {
            throw new NullPointerException("columnStartIndex can not be null");
        }
        else if(columnStartIndex < 0) {
            throw new IllegalArgumentException("columnStartIndex can not less then 0");
        }

        if(!useLastNumberOfCells && !usePhysicalNumberOfCells && isReader && needCheckColLength) {
            if(columnLength == null) {
                throw new NullPointerException("columnLength can not be null");
            }
            else if(columnLength < 0) {
                throw new IllegalArgumentException("columnLength can not less then 0");
            }
        }

        if(isReader) {
            if(columnStep == null) {
                throw new NullPointerException("rowStep can not be null");
            }
            else if(columnStep < 1) {
                throw new IllegalArgumentException("columnStep can not less then 1");
            }
        }
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

    public Boolean usePhysicalNumberOfRows() {
        return usePhysicalNumberOfRows;
    }

    public ExcelIndex setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        this.usePhysicalNumberOfRows = usePhysicalNumberOfRows;
        return this;
    }

    public Boolean usePhysicalNumberOfCells() {
        return usePhysicalNumberOfCells;
    }

    public ExcelIndex setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        this.usePhysicalNumberOfCells = usePhysicalNumberOfCells;
        return this;
    }

    public Boolean useLastNumberOfRows() {
        return useLastNumberOfRows;
    }

    public ExcelIndex setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        this.useLastNumberOfRows = useLastNumberOfRows;
        return this;
    }

    public Boolean useLastNumberOfCells() {
        return useLastNumberOfCells;
    }

    public ExcelIndex setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        this.useLastNumberOfCells = useLastNumberOfCells;
        return this;
    }
}
