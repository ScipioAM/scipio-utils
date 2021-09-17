package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.*;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * excel -> JavaBean 读取器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelBeanReader extends ExcelOperator{

    /**
     * 行检查白名单（不在此清单中的都是要跳过的）（为null则视为都不跳过）
     */
    private final Set<Integer> rowWhitelist = new HashSet<>();

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
        //参数检查
        if(beanMapper == null) {
            throw new NullPointerException("argument \"ExcelBeanMapper\" is null");
        }
        paramsCheck();
        //获取目标Sheet
        Sheet sheet = getSheet(excelIndex,workbook);
        //确定最终的总行数
        Integer rowLength = determineRowLength(excelIndex,sheet);
        //确定行白名单（跳过这些行）
        if(beanMapper instanceof ExcelBeanAutoMapper) {
            ExcelBeanAutoMapper<T> autoMapper = (ExcelBeanAutoMapper<T>) beanMapper;
            checkRowBlacklist(autoMapper.getMappingInfo(),autoMapper.getBeanClass(),rowLength);
        }
        // 开始扫描行
        List<T> beanList = buildBeanList();
        for(int i = excelIndex.getRowStartIndex(); i < rowLength; i += excelIndex.getRowStep()) {
            //在黑名单中的行要跳过
            if(rowWhitelist.size() > 0 && !rowWhitelist.contains(i)) {
                continue;
            }
            Row row = sheet.getRow(i);
            T bean = beanMapper.mappingExcel2Bean(row, i, rowLength);
            if(bean != null) {
                beanList.add(bean);
            }
        }
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
            throw new IllegalArgumentException("argument \"mappingInfo\" is null or empty");
        }
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        ExcelBeanAutoMapper<T> beanAutoMapper = new ExcelBeanAutoMapper<>(mappingInfo,beanClass);
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
        ExcelBeanAutoMapper<T> beanAutoMapper = new ExcelBeanAutoMapper<>(null,beanClass);
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

    /**
     * 检查构建行白名单
     * @param infoList 自定义映射信息list
     * @param beanClass 目标JavaBean的类型
     * @param rowLength 要处理的总行数
     * @throws IllegalStateException 注解模式下。目标JavaBean没有任何{@link ExcelMapping}注解
     */
    private void checkRowBlacklist(List<ExcelMappingInfo> infoList, Class<?> beanClass, int rowLength) throws IllegalStateException {
        Set<Integer> rowIndexSet = new HashSet<>();
        int checkCount = 0;
        if(infoList != null) {
            for(ExcelMappingInfo info : infoList) {
                Integer rowIndex = info.getRowIndex();
                if(rowIndex != null && rowIndex >= 0) {
                    rowIndexSet.add(rowIndex);
                    checkCount++;
                }
            }
        }
        else {
            List<ExcelMapping> mappingAnnotations = getMappingAnnotations(beanClass);
            if(mappingAnnotations.size() <= 0) {
                throw new IllegalStateException("target class[" + beanClass.getCanonicalName() + "] has no annotation[@ExcelMapping] found!");
            }
            for(ExcelMapping info : mappingAnnotations) {
                if(info.rowIndex() >= 0) {
                    rowIndexSet.add(info.rowIndex());
                    checkCount++;
                }
            }
        }
        if(checkCount == rowLength) {
            rowWhitelist.addAll(rowIndexSet);
        }
        else {
            rowWhitelist.clear();
        }
    }

    /**
     * 获取类里所有字段上的注解
     * @param beanClass javaBean类型
     * @return 类里所有字段上的注解，没有注解就为空list
     */
    private List<ExcelMapping> getMappingAnnotations(Class<?> beanClass) {
        List<ExcelMapping> mappingAnnotations = new ArrayList<>();
        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(ExcelMapping.class)) { //跳过没有被注解的字段
                ExcelMapping mappingAnnotation = field.getDeclaredAnnotation(ExcelMapping.class);
                mappingAnnotations.add(mappingAnnotation);
            }
        }
        return mappingAnnotations;
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
