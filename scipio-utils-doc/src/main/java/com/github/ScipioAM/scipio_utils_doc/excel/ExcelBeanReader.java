package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.*;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * excel -> JavaBean 读取器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelBeanReader extends ExcelBeanOperator{

    /** 自定义excel -> JavaBean类型转换器 */
    private BeanTypeConvert customTypeConvert;

    /**
     * 对于公式单元格，是获取公式计算的值，还是公式本身。
     *      (为true代表获取公式计算的值)
     */
    private boolean getFormulaResult = true;

    /** 垂直读取标志 */
    private boolean verticalRead = false;

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
    public <T> List<T> read(@NotNull ExcelBeanMapper<T> beanMapper) throws Exception {
        //操作前准备(参数检查、确认扫描总行数等)
        OpPrepareVo prepareVo = operationPrepare(beanMapper,false);
        Sheet sheet = prepareVo.sheet;
        Integer rowLength = prepareVo.rowLength;
        Integer rowStartIndex = excelIndex.getRowStartIndex();
        int finalRowLength = rowStartIndex + rowLength;
        // 开始扫描行
        List<T> beanList = buildBeanList();
        for(int i = rowStartIndex; i < finalRowLength; i += excelIndex.getRowStep()) {
            //不在白名单中的行要跳过
            if(rowWhitelist.size() > 0 && !rowWhitelist.contains(i)) {
                continue;
            }
            Row row = sheet.getRow(i);
            //垂直读取
            if(verticalRead) {
                //TODO 垂直读取的实现待完成，循环每个cell，写入bean的对应位置（bean为null就创建）
                System.err.println("Sorry, vertical read is nt finished yet!");
            }
            else { //正常水平读取
                T bean = beanMapper.mappingExcel2Bean(row, i, rowLength);
                if(bean != null) {
                    beanList.add(bean);
                }
            }
        }//end of for
        //收尾
        finish();
        return beanList;
    }

    /**
     * 读取excel并映射为JavaBean - 根据定义的映射信息list
     * @param mappingInfo 映射信息
     * @param <T> JavaBean的类型
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull List<ExcelMappingInfo> mappingInfo, @NotNull Class<T> beanClass) throws Exception {
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            throw new IllegalArgumentException("argument \"mappingInfo\" can not be null or empty");
        }
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        AutoExcelBeanMapper<T> beanAutoMapper = new AutoExcelBeanMapper<>(mappingInfo,beanClass);
        if(customTypeConvert != null) {
            beanAutoMapper.setTypeConvert(customTypeConvert);
        }
        beanAutoMapper.setGetFormulaResult(getFormulaResult);
        return read(beanAutoMapper);
    }

    /**
     * 读取excel并映射为JavaBean - 根据注解映射
     * @param <T> JavaBean的类型，要映射的字段上需要添加{@link ExcelMapping}注解
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull Class<T> beanClass) throws Exception {
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        AutoExcelBeanMapper<T> beanAutoMapper = new AutoExcelBeanMapper<>(beanClass);
        if(customTypeConvert != null) {
            beanAutoMapper.setTypeConvert(customTypeConvert);
        }
        beanAutoMapper.setGetFormulaResult(getFormulaResult);
        return read(beanAutoMapper);
    }

    /**
     * 构建空的beanList
     *      <p>子类重写此方法以定义自己的List类型</p>
     * @param <T> bean类型
     * @return 空的beanList实例
     */
    protected <T> List<T> buildBeanList() {
        return new ArrayList<>();
    }

    //==================================================================================================================

    public ExcelBeanReader setCustomTypeConvert(BeanTypeConvert customTypeConvert) {
        this.customTypeConvert = customTypeConvert;
        return this;
    }

    public boolean isGetFormulaResult() {
        return getFormulaResult;
    }

    public ExcelBeanReader setGetFormulaResult(Boolean getFormulaResult) {
        this.getFormulaResult = getFormulaResult;
        return this;
    }

    public boolean isVerticalRead() {
        return verticalRead;
    }

    public ExcelBeanReader setVerticalRead(boolean verticalRead) {
        this.verticalRead = verticalRead;
        return this;
    }

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
