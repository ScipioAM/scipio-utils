package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_common.reflect.FieldUtil;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.AutoExcelBeanMapper;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.BeanListener;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelBeanMapper;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Excel-JavaBean处理器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/18
 */
public abstract class ExcelBeanOperator extends ExcelOperator {

    /** 行检查白名单（不在此清单中的都是要跳过的）（为null则视为都不跳过） */
    protected final Set<Integer> rowWhitelist = new HashSet<>();

    /** {@link ExcelBeanReader}或{@link ExcelBeanWriter}对每个JavaBean处理时的监听回调 */
    protected BeanListener<?> beanListener;

    /** 是否强制设置{@link BeanListener}(不检查类型是否一致) */
    protected boolean isForceSetBeanListener = false;

    /**
     * 准备ExcelIndex
     * @param beanClass javaBean类型
     * @param beanList [writer专用]要写入的beanList
     */
    protected void prepareExcelIndex(Class<?> beanClass, List<?> beanList) {
        //已设定的excelIndex优先级高于注解
        if(excelIndex == null) {
            excelIndex = new com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex();
            //获取注解
            ExcelIndex annotation = beanClass.getDeclaredAnnotation(ExcelIndex.class);
            if(annotation == null) {
                return;
            }
            //将注解的值转换为excelIndex对象
            if(annotation.sheetIndex() >= 0) {
                excelIndex.setSheetIndex(annotation.sheetIndex());
            }
            else {
                excelIndex.setSheetName(annotation.sheetName());
            }
            excelIndex.setRowStartIndex(annotation.rowStartIndex())
                    .setRowLength(annotation.rowLength())
                    .setRowStep(annotation.rowStep())
                    .setColumnStartIndex(annotation.columnStartIndex())
                    .setColumnLength(annotation.columnLength())
                    .setColumnStep(annotation.columnStep())
                    .setUseLastNumberOfRows(annotation.useLastNumberOfRows())
                    .setUseLastNumberOfCells(annotation.useLastNumberOfCells())
                    .setUsePhysicalNumberOfRows(annotation.usePhysicalNumberOfRows())
                    .setUsePhysicalNumberOfCells(annotation.usePhysicalNumberOfCells());
        }

        if(beanList != null) { //writer专用的相关默认配置
            //如果未手动设定，则默认要写入的总行数等于beanList的总个数
            if(excelIndex.getRowLength() == null || excelIndex.getRowLength() <= 0) {
                excelIndex.setRowLength(beanList.size());
            }
            //如果未手动设定，则默认从第一行写入
            if(excelIndex.getRowStartIndex() == null || excelIndex.getRowStartIndex() < 0) {
                excelIndex.setRowStartIndex(0);
            }
            //如果未手动设定，则默认从第一列写入
            if(excelIndex.getColumnStartIndex() == null || excelIndex.getColumnStartIndex() < 0) {
                excelIndex.setColumnStartIndex(0);
            }
            //如果未手动设定，则默认在第一个sheet写入
            if(excelIndex.getSheetIndex() == null || excelIndex.getSheetIndex() < 0 || StringUtil.isNull(excelIndex.getSheetName())) {
                excelIndex.setSheetIndex(0);
            }
        }
    }

    /**
     * 操作前的准备
     * @param beanMapper 映射操作者
     * @param createIfNotExists 是否不存在就创建
     * @param <T> javaBean类型
     * @param beanList [writer专用]要写入的beanList
     * @return 准备信息
     */
    protected <T> OpPrepareVo operationPrepare(ExcelBeanMapper<T> beanMapper, boolean createIfNotExists, Class<T> beanClass, List<T> beanList) {
        //准备ExcelIndex
        prepareExcelIndex(beanClass,beanList);
        //参数检查
        if(beanMapper == null) {
            throw new NullPointerException("argument \"ExcelBeanMapper\" is null");
        }
        paramsCheck(beanList == null);
        //获取目标Sheet
        Sheet sheet = getSheet(excelIndex,workbook,createIfNotExists);
        //确定最终的总行数
        Integer rowLength = determineRowLength(excelIndex,sheet);
        //确定行白名单（跳过这些行）
        if(beanMapper instanceof AutoExcelBeanMapper) {
            AutoExcelBeanMapper<T> autoMapper = (AutoExcelBeanMapper<T>) beanMapper;
            checkRowWhitelist(autoMapper.getMappingInfo(),autoMapper.getBeanClass(),rowLength);
        }
        return new OpPrepareVo(sheet,rowLength);
    }

    /**
     * 检查构建行白名单
     * @param infoList 自定义映射信息list
     * @param beanClass 目标JavaBean的类型
     * @param rowLength 要处理的总行数
     * @throws IllegalStateException 注解模式下。目标JavaBean没有任何{@link ExcelMapping}注解
     */
    protected void checkRowWhitelist(List<ExcelMappingInfo> infoList, Class<?> beanClass, int rowLength) throws IllegalStateException {
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
            List<ExcelMapping> mappingAnnotations = FieldUtil.getAnnotationFromFields(beanClass,ExcelMapping.class);
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

    //==================================================================================================================

    /**
     * 内部类：操作前准备的对象数据
     */
    static class OpPrepareVo {
        Sheet sheet;
        Integer rowLength;

        OpPrepareVo(Sheet sheet, Integer rowLength) {
            this.sheet = sheet;
            this.rowLength = rowLength;
        }
    }

}
