package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelException;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 垂直进行读取的{@link AutoExcelBeanMapper}
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/23
 */
public class VerticalAutoExcelBeanMapper<T> extends VerticalExcelBeanMapper<T> {

    /** 映射信息的缓存map，key是rowIndex，value是映射信息 */
    private Map<Integer,ExcelMappingInfo> mappingInfoMap;

    public VerticalAutoExcelBeanMapper(Class<T> beanClass) {
        super.beanClass = beanClass;
    }

    public VerticalAutoExcelBeanMapper(List<ExcelMappingInfo> mappingInfo, Class<T> beanClass) {
        super.mappingInfo = mappingInfo;
        super.beanClass = beanClass;
    }

    //==================================================================================================================

    /**
     * 前期准备映射信息
     */
    @Override
    public void prepareMappingInfo() {
        //从注解中获取映射信息(前提是没有用参数指定映射信息)
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            mappingInfo = ExcelMappingUtil.buildFromAnnotations(beanClass,false,true);
        }
        //构建映射缓存map，以方便后面使用
        mappingInfoMap = new HashMap<>();
        for(ExcelMappingInfo info : mappingInfo) {
            mappingInfoMap.put(info.getRowIndex(),info);
        }
    }

    /**
     * 映射：excel -> JavaBean
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数
     * @param columnStartIndex 起始列号
     * @param columnLength 要读取的总列数(加上了起始列号)
     * @param beanList javaBean列表（映射读取的总结果）
     */
    @Override
    public void mappingExcel2Bean(Row row, int rowIndex, int rowLength, int columnStartIndex, int columnLength, List<T> beanList) throws ExcelException {
        if(row == null) { //跳过整行都是空的
            return;
        }
        //遍历一行中所有要扫描的列
        for(int i = columnStartIndex; i < columnLength; i++) {
            try {
                //获取或创建bean
                boolean isNewBean = false;
                T bean;
                int listIndex = i - columnStartIndex;
                if(listIndex >= beanList.size()) {
                    isNewBean = true;
                    bean = beanClass.getDeclaredConstructor().newInstance();
                }
                else {
                    bean = beanList.get(listIndex);
                }
                ExcelMappingInfo info = mappingInfoMap.get(rowIndex);

                String fieldName = info.getFieldName();
                Integer cellIndex = info.getCellIndex();
                if(cellIndex != null && cellIndex >= 0 && i != cellIndex) {
                    continue;//启用列索引(不为空且大于等于0)，且当前列不是指定的列，则跳过
                }
                //获取单元格的值
                Cell cell = row.getCell(i);
                if(cell == null) {
                    System.err.println("Cell is null, rowIndex[" + rowIndex + "], cellIndex[" + i + "]");
                    continue;
                }

                //单元格处理监听器
                if(cellHandler != null && !cellHandler.handle(cell,rowIndex,i,rowLength,columnLength)) {
                    break;
                }

                //注意！此处未检查值全为null的bean
                ExcelMappingUtil.setValueIntoBean(cell,beanClass,bean,fieldName,typeConvert,getFormulaResult,cellIgnoreHandler);
                if(isNewBean) {
                    beanList.add(bean);

                    //对每个bean的监听回调
                    if(beanListener != null) {
                        beanListener.onHandle(true,bean,row,rowLength,columnLength);
                    }
                }
            } catch (Exception e) {
                if(e instanceof ExcelException) {
                    throw (ExcelException) e;
                }
                else {
                    throw new ExcelException(e.getMessage(),e)
                            .setSheetName(row.getSheet().getSheetName())
                            .setRowIndex(rowIndex)
                            .setCellIndex(i);
                }
            }
        }//end of for
    }//end of mappingExcel2Bean

}
