package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_doc.excel.listener.*;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelBeanReader extends ExcelOperator{

    @Override
    public ExcelBeanReader load(@NotNull File file) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanReader) super.load(file);
    }

    @Override
    public ExcelBeanReader load(@NotNull String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanReader) super.load(fileFullPath);
    }

    /**
     * 读取excel并映射为JavaBean
     * @param beanMapper 自定义转换器
     * @param <T> JavaBean的类型
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull ExcelBeanMapper<T> beanMapper) {
        //参数检查
        if(beanMapper == null) {
            throw new NullPointerException("argument \"ExcelBeanMapper\" is null");
        }
        paramsCheck();
        //获取目标Sheet
        Sheet sheet = getSheet(excelIndex,workbook);
        //确定最终的行数
        Integer rowLength = determineRowLength(excelIndex,sheet);
        // 开始扫描行
        List<T> beanList = buildBeanList();
        for(int i = excelIndex.getRowStartIndex(); i < rowLength; i += excelIndex.getRowStep()) {
            Row row = sheet.getRow(i);
            T bean = beanMapper.mapping(row,i);
            if(bean != null) {
                beanList.add(bean);
            }
        }
        //收尾
        finish();
        return beanList;
    }

    /**
     * 读取excel并映射为JavaBean
     * @param mappingInfo 映射信息
     *                     <ul>
     *                          <li>Key: excel每一行中，要映射列的下标(0-based)</li>
     *                          <li>Value: JavaBean字段名称(大小写敏感)</li>
     *                     </ul>
     * @param <T> JavaBean的类型
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(Map<Integer,String> mappingInfo) {
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            throw new IllegalArgumentException("argument \"mappingInfo\" is null or empty");
        }
        ExcelBeanAutoMapper<T> beanAutoMapper = new ExcelBeanAutoMapper<>(mappingInfo);
        return read(beanAutoMapper);
    }

    /**
     * 构建空的beanList
     *      <p>子类重写此方法以定义自己的List类型</p>
     * @param <T> bean类型
     * @return 空的beanList实例
     */
    public <T> List<T> buildBeanList() {
        return new ArrayList<>();
    }

    //==================================================================================================================

    @Override
    public ExcelBeanReader setRowHandler(ExcelRowHandler rowHandler) {
        return (ExcelBeanReader) super.setRowHandler(rowHandler);
    }

    @Override
    public ExcelBeanReader setCellHandler(ExcelCellHandler cellHandler) {
        return (ExcelBeanReader) super.setCellHandler(cellHandler);
    }

    @Override
    public ExcelBeanReader setEndListener(ExcelEndListener endListener) {
        return (ExcelBeanReader) super.setEndListener(endListener);
    }

    @Override
    public ExcelBeanReader setExcelIndex(ExcelIndex excelIndex) {
        return (ExcelBeanReader) super.setExcelIndex(excelIndex);
    }

    @Override
    public ExcelBeanReader setSheetIndex(Integer sheetIndex) {
        return (ExcelBeanReader) super.setSheetIndex(sheetIndex);
    }

    @Override
    public ExcelBeanReader setSheetName(String sheetName) {
        return (ExcelBeanReader) super.setSheetName(sheetName);
    }

    @Override
    public ExcelBeanReader setRowStartIndex(Integer rowStartIndex) {
        return (ExcelBeanReader) super.setRowStartIndex(rowStartIndex);
    }

    @Override
    public ExcelBeanReader setRowLength(Integer rowLength) {
        return (ExcelBeanReader) super.setRowLength(rowLength);
    }

    @Override
    public ExcelBeanReader setRowStep(Integer rowStep) {
        return (ExcelBeanReader) super.setRowStep(rowStep);
    }

    @Override
    public ExcelBeanReader setColumnStartIndex(Integer columnStartIndex) {
        return (ExcelBeanReader) super.setColumnStartIndex(columnStartIndex);
    }

    @Override
    public ExcelBeanReader setColumnLength(Integer columnLength) {
        return (ExcelBeanReader) super.setColumnLength(columnLength);
    }

    @Override
    public ExcelBeanReader setColumnStep(Integer columnStep) {
        return (ExcelBeanReader) super.setColumnStep(columnStep);
    }

    @Override
    public ExcelBeanReader setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        return (ExcelBeanReader) super.setUsePhysicalNumberOfRows(usePhysicalNumberOfRows);
    }

    @Override
    public ExcelBeanReader setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        return (ExcelBeanReader) super.setUsePhysicalNumberOfCells(usePhysicalNumberOfCells);
    }

    @Override
    public ExcelBeanReader setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        return (ExcelBeanReader) super.setUseLastNumberOfRows(useLastNumberOfRows);
    }

    @Override
    public ExcelBeanReader setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        return (ExcelBeanReader) super.setUseLastNumberOfCells(useLastNumberOfCells);
    }
}
