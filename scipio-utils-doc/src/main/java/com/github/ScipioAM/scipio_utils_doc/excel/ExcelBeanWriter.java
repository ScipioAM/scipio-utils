package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotEmpty;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.*;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanCellWriter;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * JavaBean -> excel 写入器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelBeanWriter extends ExcelBeanOperator{

    /** excel文件及sheet不存在时，是否要自动创建 */
    private boolean needCreate = false;

    private File excelFile;

    /** 自定义单元格写入实现 */
    private BeanCellWriter customCellWriter;

    @Override
    public ExcelBeanWriter load(File file) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanWriter) super.load(file);
    }

    @Override
    public ExcelBeanWriter load(String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanWriter) super.load(fileFullPath);
    }

    /**
     * 加载excel文件，不存在就创建
     */
    public ExcelBeanWriter loadOrCreate(File file) throws IOException, NullPointerException {
        if(file == null) {
            throw new NullPointerException("argument \"file\" is null");
        }
        else if(file.isDirectory()) {
            throw new IllegalArgumentException("argument \"file\" must be a excel file");
        }
        else if(!file.exists()) {
            needCreate = true;
        }
        Workbook workbook;
        super.isOldVersion = ExcelUtil.isOldVersion(file);
        if(super.isOldVersion) {
            workbook = needCreate ? new HSSFWorkbook() : new HSSFWorkbook(new FileInputStream(file));
        }
        else {
            workbook = needCreate ? new XSSFWorkbook() : new XSSFWorkbook(new FileInputStream(file));
        }
        super.workbook = workbook;
        this.excelFile = file;
        return this;
    }

    /**
     * 加载，不存在就创建
     */
    public ExcelBeanWriter loadOrCreate(String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return this.load(new File(fileFullPath));
    }

    @Override
    protected Sheet getSheet(ExcelIndex excelIndex, Workbook workbook, boolean createIfNotExists) {
        int sheetCount = workbook.getNumberOfSheets();
        if(sheetCount > 0) {
            return super.getSheet(excelIndex, workbook, createIfNotExists);
        }
        else {
            return workbook.createSheet();
        }
    }

    @Override
    public void close() {
        //write完后会自行关闭，重复关闭会导致异常，故这里故意不再执行关闭逻辑
    }

    //==================================================================================================================

    /**
     * 将JavaBean写入excel
     * @param beanMapper 自定义转换器
     * @param beanList 要写入的数据
     * @param <T> JavaBean类型
     */
    public <T> void write(@NotNull ExcelBeanMapper<T> beanMapper, @NotEmpty List<T> beanList, @NotNull Class<T> beanClass) throws Exception {
        //如果未手动设定，则默认要写入的总行数等于beanList的总个数
        if(excelIndex.getRowLength() == null || excelIndex.getRowLength() < 0) {
            excelIndex.setRowLength(beanList.size());
        }
        //如果未手动设定，则默认从第一行写入
        if(excelIndex.getRowStartIndex() == null || excelIndex.getRowStartIndex() < 0) {
            excelIndex.setRowStartIndex(0);
        }
        //如果未手动设定，则默认在第一个sheet写入
        if(excelIndex.getSheetIndex() == null || excelIndex.getSheetIndex() < 0 || StringUtil.isNull(excelIndex.getSheetName())) {
            excelIndex.setSheetIndex(0);
        }
        //操作前准备(参数检查、确认扫描总行数等)
        OpPrepareVo prepareVo = operationPrepare(beanMapper,true,beanClass);
        Sheet sheet = prepareVo.sheet;
        Integer rowLength = prepareVo.rowLength;
        // 开始扫描行
        int j = 0;
        for(int i = excelIndex.getRowStartIndex(); i < rowLength; i += excelIndex.getRowStep()) {
            //不在白名单中的行要跳过
            if(rowWhitelist.size() > 0 && !rowWhitelist.contains(i)) {
                continue;
            }
            Row row = sheet.getRow(i);
            //行处理监听器
            if(rowHandler != null && !rowHandler.handle(row,i,rowLength)) {
                break;
            }

            if(row == null) {
                row = sheet.createRow(i);
            }
            T bean = beanList.get(j);
            beanMapper.mappingBean2Excel(row,i,rowLength,bean);
            j++;
        }
        //将workbook对象的数据真正写入文件里去
        try (FileOutputStream out = new FileOutputStream(excelFile)) {
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end of write()

    /**
     * 将JavaBean写入excel - 根据定义的映射信息list
     * @param beanList 要写入的数据
     * @param mappingInfo 映射信息
     * @param beanClass JavaBean的类型
     * @param <T> JavaBean的类型
     */
    public <T> void write(@NotEmpty List<T> beanList, @NotNull List<ExcelMappingInfo> mappingInfo, @NotNull Class<T> beanClass) throws Exception {
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            throw new IllegalArgumentException("argument \"mappingInfo\" can not be null or empty");
        }
        if(beanList == null  || beanList.size() <= 0) {
            throw new IllegalArgumentException("argument \"beanList\" can not be null or empty, for write into excel");
        }
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        AutoExcelBeanMapper<T> autoMapper = new AutoExcelBeanMapper<>(mappingInfo,beanClass);
        if(customCellWriter != null) {
            autoMapper.setCellWriter(customCellWriter);
        }
        write(autoMapper,beanList,beanClass);
    }

    /**
     * 将JavaBean写入excel - 要映射的字段上需要添加{@link ExcelMapping}注解
     * @param beanList 要写入的数据
     * @param beanClass JavaBean的类型
     * @param <T> JavaBean的类型
     */
    public <T> void write(@NotEmpty List<T> beanList, @NotNull Class<T> beanClass) throws Exception {
        if(beanList == null  || beanList.size() <= 0) {
            throw new IllegalArgumentException("argument \"beanList\" can not be null or empty, for write into excel");
        }
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        AutoExcelBeanMapper<T> autoMapper = new AutoExcelBeanMapper<>(null,beanClass);
        if(customCellWriter != null) {
            autoMapper.setCellWriter(customCellWriter);
        }
        write(autoMapper,beanList,beanClass);
    }

    //==================================================================================================================

    public File getExcelFile() {
        return excelFile;
    }

    public ExcelBeanWriter setCustomCellWriter(BeanCellWriter customCellWriter) {
        this.customCellWriter = customCellWriter;
        return this;
    }

    public boolean isNeedCreate() {
        return needCreate;
    }

    public ExcelBeanWriter setNeedCreate(boolean needCreate) {
        this.needCreate = needCreate;
        return this;
    }

    @Override
    public ExcelBeanWriter setRowHandler(ExcelRowHandler rowHandler) {
        return (ExcelBeanWriter) super.setRowHandler(rowHandler);
    }

    @Override
    public ExcelBeanWriter setCellHandler(ExcelCellHandler cellHandler) {
        return (ExcelBeanWriter) super.setCellHandler(cellHandler);
    }

    @Override
    public ExcelBeanWriter setEndListener(ExcelEndListener endListener) {
        return (ExcelBeanWriter) super.setEndListener(endListener);
    }

    @Override
    public ExcelBeanWriter setExcelIndex(ExcelIndex excelIndex) {
        return (ExcelBeanWriter) super.setExcelIndex(excelIndex);
    }

    @Override
    public ExcelBeanWriter setSheetIndex(Integer sheetIndex) {
        return (ExcelBeanWriter) super.setSheetIndex(sheetIndex);
    }

    @Override
    public ExcelBeanWriter setSheetName(String sheetName) {
        return (ExcelBeanWriter) super.setSheetName(sheetName);
    }

    @Override
    public ExcelBeanWriter setRowStartIndex(Integer rowStartIndex) {
        return (ExcelBeanWriter) super.setRowStartIndex(rowStartIndex);
    }

    @Override
    public ExcelBeanWriter setRowLength(Integer rowLength) {
        return (ExcelBeanWriter) super.setRowLength(rowLength);
    }

    @Override
    public ExcelBeanWriter setRowStep(Integer rowStep) {
        return (ExcelBeanWriter) super.setRowStep(rowStep);
    }

    @Override
    public ExcelBeanWriter setColumnStartIndex(Integer columnStartIndex) {
        return (ExcelBeanWriter) super.setColumnStartIndex(columnStartIndex);
    }

    @Override
    public ExcelBeanWriter setColumnLength(Integer columnLength) {
        return (ExcelBeanWriter) super.setColumnLength(columnLength);
    }

    @Override
    public ExcelBeanWriter setColumnStep(Integer columnStep) {
        return (ExcelBeanWriter) super.setColumnStep(columnStep);
    }

    @Override
    public ExcelBeanWriter setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        return (ExcelBeanWriter) super.setUsePhysicalNumberOfRows(usePhysicalNumberOfRows);
    }

    @Override
    public ExcelBeanWriter setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        return (ExcelBeanWriter) super.setUsePhysicalNumberOfCells(usePhysicalNumberOfCells);
    }

    @Override
    public ExcelBeanWriter setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        return (ExcelBeanWriter) super.setUseLastNumberOfRows(useLastNumberOfRows);
    }

    @Override
    public ExcelBeanWriter setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        return (ExcelBeanWriter) super.setUseLastNumberOfCells(useLastNumberOfCells);
    }
}
