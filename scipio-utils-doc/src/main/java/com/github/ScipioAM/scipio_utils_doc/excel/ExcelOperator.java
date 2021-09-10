package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.annotations.Nullable;
import com.github.ScipioAM.scipio_utils_common.validation.Validator;
import com.github.ScipioAM.scipio_utils_doc.excel.listener.ExcelCellHandler;
import com.github.ScipioAM.scipio_utils_doc.excel.listener.ExcelEndListener;
import com.github.ScipioAM.scipio_utils_doc.excel.listener.ExcelRowHandler;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * excel读取者
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/8/27
 */
public class ExcelOperator extends ExcelOperatorBase {

    /** 指定的Sheet和Sheet里的扫描范围 */
    protected ExcelIndex excelIndex = new ExcelIndex();

    @Override
    public ExcelOperator load(File file) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelOperator) super.load(file);
    }

    @Override
    public ExcelOperator load(String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelOperator) super.load(fileFullPath);
    }

    /**
     * 扫描指定Sheet里的指定范围
     * @param missingCellPolicy 单元格缺失或为空时的策略，默认是RETURN_NULL_AND_BLANK，具体参看{@link Row.MissingCellPolicy}
     */
    public void operate(@Nullable Row.MissingCellPolicy missingCellPolicy)
    {
        // **************** 参数检查 ****************
        if(workbook == null) {
            throw new NullPointerException("workbook is null, maybe not loaded before read");
        }
        if(excelIndex == null) {
            throw new NullPointerException("argument[ExcelIndex] is null");
        }
        Validator.newInstance().validateOnce(excelIndex);//校验合法性
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
        // **************** 获取目标Sheet ****************
        Integer sheetIndex = excelIndex.getSheetIndex();
        Sheet sheet = (sheetIndex != null) ? workbook.getSheetAt(sheetIndex) : workbook.getSheet(excelIndex.getSheetName());
        if(missingCellPolicy == null) {
            missingCellPolicy = workbook.getMissingCellPolicy();
        }
        //确定最终的行数
        Integer rowLength = excelIndex.getRowLength();
        if(excelIndex.useLastNumberOfRows()) {
            rowLength = (sheet.getLastRowNum() + 1);
        }
        else if(excelIndex.usePhysicalNumberOfRows()) {
            rowLength = sheet.getPhysicalNumberOfRows();
        }
        // **************** 开始扫描行 ****************
        OUTER:
        for(int i = excelIndex.getRowStartIndex(); i < rowLength; i += excelIndex.getRowStep()) {
            Row row = sheet.getRow(i);
            if(rowHandler != null) {
                if(!rowHandler.handle(sheet,row,i)) {
                    break;
                }
            }
            if(cellHandler != null) {
                //确定每次扫描时最终的列数
                Integer columnLength = excelIndex.getColumnLength();
                if (excelIndex.useLastNumberOfRows()) {
                    int lastCellNum = row.getLastCellNum();
                    columnLength = (lastCellNum == -1 ? 0 : lastCellNum);
                }
                else if(excelIndex.usePhysicalNumberOfCells()) {
                    columnLength = row.getPhysicalNumberOfCells();
                }
                //开始扫描列
                for(int j = excelIndex.getColumnStartIndex(); j < columnLength; j += excelIndex.getColumnStep()) {
                    Cell cell = row.getCell(j,missingCellPolicy);
                    if(!cellHandler.handle(row,cell,i,j)) {
                        break OUTER;
                    }
                }
            }//end of if(cellHandler != null)
        }//end of row-scan for
        if(endListener != null) {
            endListener.lastOperation(workbook);
        }
        else {
            ExcelEndListener.SIMPLE_CLOSE.lastOperation(workbook);
        }
    }//end of read()

    public void operate() {
        operate(null);
    }

    //==================================================================================================================

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

    public ExcelOperator setExcelIndex(ExcelIndex excelIndex) {
        this.excelIndex = excelIndex;
        return this;
    }

    public ExcelOperator setSheetIndex(Integer sheetIndex) {
        excelIndex.setSheetIndex(sheetIndex);
        return this;
    }

    public ExcelOperator setSheetName(String sheetName) {
        excelIndex.setSheetName(sheetName);
        return this;
    }

    public ExcelOperator setRowStartIndex(Integer rowStartIndex) {
        excelIndex.setRowStartIndex(rowStartIndex);
        return this;
    }

    public ExcelOperator setRowLength(Integer rowLength) {
        excelIndex.setRowLength(rowLength);
        return this;
    }

    public ExcelOperator setRowStep(Integer rowStep) {
        excelIndex.setRowStep(rowStep);
        return this;
    }

    public ExcelOperator setColumnStartIndex(Integer columnStartIndex) {
        excelIndex.setColumnStartIndex(columnStartIndex);
        return this;
    }

    public ExcelOperator setColumnLength(Integer columnLength) {
        excelIndex.setColumnLength(columnLength);
        return this;
    }

    public ExcelOperator setColumnStep(Integer columnStep) {
        excelIndex.setColumnStep(columnStep);
        return this;
    }

    public ExcelOperator setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        excelIndex.setUsePhysicalNumberOfRows(usePhysicalNumberOfRows);
        return this;
    }

    public ExcelOperator setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        excelIndex.setUsePhysicalNumberOfCells(usePhysicalNumberOfCells);
        return this;
    }

    public ExcelOperator setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        excelIndex.setUseLastNumberOfRows(useLastNumberOfRows);
        return this;
    }

    public ExcelOperator setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        excelIndex.setUseLastNumberOfCells(useLastNumberOfCells);
        return this;
    }

}
