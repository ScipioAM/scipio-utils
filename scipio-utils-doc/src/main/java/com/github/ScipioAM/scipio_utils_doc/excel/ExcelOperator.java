package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.Nullable;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.*;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;

/**
 * excel读取者
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/8/27
 */
public class ExcelOperator extends ExcelOperatorBase {

    /** 指定的Sheet和Sheet里的扫描范围 */
    protected ExcelIndex excelIndex;

    @Override
    public ExcelOperator load(@NotNull File file) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelOperator) super.load(file);
    }

    @Override
    public ExcelOperator load(@NotNull String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelOperator) super.load(fileFullPath);
    }

    /**
     * 扫描指定Sheet里的指定范围
     * @param missingCellPolicy 单元格缺失或为空时的策略，默认是RETURN_NULL_AND_BLANK，具体参看{@link Row.MissingCellPolicy}
     * @param createSheetIfNotExists 获取不到sheet时是否创建
     */
    public void operate(@Nullable Row.MissingCellPolicy missingCellPolicy, boolean createSheetIfNotExists) throws Exception
    {
        try {
            // **************** 参数检查 ****************
            paramsCheck();
            if(missingCellPolicy == null) {
                missingCellPolicy = workbook.getMissingCellPolicy();
            }
            // **************** 获取目标Sheet ****************
            Sheet sheet = getSheet(excelIndex,workbook, createSheetIfNotExists);
            //确定最终的行数(加上了起始行号)
            Integer rowLength = determineRowLength(excelIndex,sheet);

            //开始的监听回调
            if(startListener != null && !startListener.firstOperation(workbook,sheet,excelIndex)) {
                return;
            }

            // **************** 开始扫描行 ****************
            OUTER:
            for(int i = excelIndex.getRowStartIndex(); i < rowLength; i += excelIndex.getRowStep()) {
                Row row = sheet.getRow(i);
                if(rowHandler != null) {
                    if(!rowHandler.handle(row,i,rowLength)) {
                        break;
                    }
                }
                if(cellHandler != null) {
                    //确定每次扫描时最终的列数(加上了起始列号)
                    Integer columnLength = determineColumnLength(excelIndex,row);
                    //开始扫描列
                    for(int j = excelIndex.getColumnStartIndex(); j < columnLength; j += excelIndex.getColumnStep()) {
                        Cell cell = row.getCell(j,missingCellPolicy);
                        if(!cellHandler.handle(cell,i,j,rowLength,columnLength)) {
                            break OUTER;
                        }
                    }
                }//end of if(cellHandler != null)
            }//end of row-scan for
            // **************** 收尾操作 ****************
            finish();
        }catch (Exception e) {
            if(exceptionHandler != null) {
                exceptionHandler.handle(workbook,excelIndex,e);
            }
            else {
                e.printStackTrace();
            }
        }
    }//end of read()

    public void operate() throws Exception {
        operate(null,false);
    }

    //==================================================================================================================

    /**
     * 参数检查
     */
    protected void paramsCheck() throws NullPointerException, IllegalArgumentException {
        if(workbook == null) {
            throw new NullPointerException("workbook is null, maybe not loaded before read");
        }
        if(excelIndex == null) {
            throw new NullPointerException("argument[ExcelIndex] is null");
        }
        excelIndex.checkSelf();//校验合法性，非法就抛异常
        if(!excelIndex.useLastNumberOfRows() && !excelIndex.usePhysicalNumberOfRows()) {
            if(excelIndex.getRowLength() == null) {
                throw new NullPointerException("rowLength is null");
            }
            //校验行扫描范围是否合法
            ExcelUtil.checkRowMax(isOldVersion,excelIndex.getRowStartIndex(),excelIndex.getRowLength());
        }
        if((cellHandler != null) && !excelIndex.usePhysicalNumberOfCells() && !excelIndex.useLastNumberOfCells()) {
            if(excelIndex.getColumnLength() == null) {
                throw new NullPointerException("columnLength is null");
            }
            //校验列扫描范围是否合法
            ExcelUtil.checkColumnMax(isOldVersion,excelIndex.getColumnStartIndex(),excelIndex.getColumnLength());
        }
    }

    /**
     * 根据设定获取Sheet对象
     * @param excelIndex 设定
     * @param workbook 工作簿对象
     * @param createIfNotExists 是否不存在Sheet时，创建它
     * @return Sheet对象
     */
    protected Sheet getSheet(ExcelIndex excelIndex, Workbook workbook, boolean createIfNotExists) {
        Integer sheetIndex = excelIndex.getSheetIndex();
        Sheet sheet = (sheetIndex != null) ? workbook.getSheetAt(sheetIndex) : workbook.getSheet(excelIndex.getSheetName());
        if(sheet == null && createIfNotExists) {
            sheet = workbook.createSheet((sheetIndex != null) ? null : excelIndex.getSheetName());
        }
        return sheet;
    }

    /**
     * 根据设定决定最终要读取的总行数(加上了起始行号)
     * @param excelIndex 设定
     * @param sheet Sheet对象
     * @return 最终要读取的总行数
     */
    protected Integer determineRowLength(ExcelIndex excelIndex, Sheet sheet) {
        Integer rowLength = excelIndex.getRowLength();
        if(rowLength != null && rowLength >= 0) {
            rowLength += excelIndex.getRowStartIndex();
            return rowLength;
        }
        else if(excelIndex.useLastNumberOfRows()) {
            rowLength = (sheet.getLastRowNum() + 1);
        }
        else if(excelIndex.usePhysicalNumberOfRows()) {
            rowLength = sheet.getPhysicalNumberOfRows();
        }
        rowLength += excelIndex.getRowStartIndex();
        return rowLength;
    }

    /**
     * 根据设定觉得最终要读取的每行总列数(加上了起始列号)
     * @param excelIndex 设定
     * @param row row对象
     * @return 最终要读取的每行总列数
     */
    protected Integer determineColumnLength(ExcelIndex excelIndex, Row row) {
        Integer columnLength = excelIndex.getColumnLength();
        if (excelIndex.useLastNumberOfRows()) {
            int lastCellNum = row.getLastCellNum();
            columnLength = (lastCellNum == -1 ? 0 : lastCellNum);
        }
        else if(excelIndex.usePhysicalNumberOfCells()) {
            columnLength = row.getPhysicalNumberOfCells();
        }
        columnLength += excelIndex.getColumnStartIndex();
        return columnLength;
    }

    protected void finish() {
        if(endListener != null) {
            endListener.lastOperation(workbook,excelIndex);
        }
        else {
            ExcelEndListener.SIMPLE_CLOSE.lastOperation(workbook,excelIndex);
        }
    }

    //==================================================================================================================

    public void clearCallbacks() {
        super.cellHandler = null;
        super.rowHandler = null;
        super.endListener = null;
    }

    public void clearAllSettings() {
        this.excelIndex = new ExcelIndex();
        super.workbook = null;
        super.isOldVersion = null;
        clearCallbacks();
    }

    public ExcelOperator setRowHandler(ExcelRowHandler rowHandler) {
        this.rowHandler = rowHandler;
        return this;
    }

    public ExcelOperator setCellHandler(ExcelCellHandler cellHandler) {
        this.cellHandler = cellHandler;
        return this;
    }

    public ExcelOperator setEndListener(ExcelEndListener endListener) {
        super.endListener = endListener;
        return this;
    }

    public ExcelOperator setStartListener(ExcelStartListener startListener) {
        super.startListener = startListener;
        return this;
    }

    public ExcelOperator setExceptionHandler(ExceptionHandler exceptionHandler) {
        super.exceptionHandler = exceptionHandler;
        return this;
    }

    public ExcelOperator setExcelIndex(ExcelIndex excelIndex) {
        this.excelIndex = excelIndex;
        return this;
    }

    public ExcelOperator setSheetIndex(Integer sheetIndex) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setSheetIndex(sheetIndex);
        return this;
    }

    public ExcelOperator setSheetName(String sheetName) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setSheetName(sheetName);
        return this;
    }

    public ExcelOperator setRowStartIndex(Integer rowStartIndex) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setRowStartIndex(rowStartIndex);
        return this;
    }

    public ExcelOperator setRowLength(Integer rowLength) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setRowLength(rowLength);
        return this;
    }

    public ExcelOperator setRowStep(Integer rowStep) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setRowStep(rowStep);
        return this;
    }

    public ExcelOperator setColumnStartIndex(Integer columnStartIndex) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setColumnStartIndex(columnStartIndex);
        return this;
    }

    public ExcelOperator setColumnLength(Integer columnLength) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setColumnLength(columnLength);
        return this;
    }

    public ExcelOperator setColumnStep(Integer columnStep) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setColumnStep(columnStep);
        return this;
    }

    public ExcelOperator setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setUsePhysicalNumberOfRows(usePhysicalNumberOfRows);
        return this;
    }

    public ExcelOperator setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setUsePhysicalNumberOfCells(usePhysicalNumberOfCells);
        return this;
    }

    public ExcelOperator setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setUseLastNumberOfRows(useLastNumberOfRows);
        return this;
    }

    public ExcelOperator setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        if(this.excelIndex == null) {
            this.excelIndex = new ExcelIndex();
        }
        excelIndex.setUseLastNumberOfCells(useLastNumberOfCells);
        return this;
    }

}
