package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.SimpleBeanTypeConvert;
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
public class VerticalAutoExcelBeanMapper<T> implements VerticalExcelBeanMapper<T> {
    /**
     * 自定义映射信息(非null时优先级高于{@link ExcelMapping})
     */
    private List<ExcelMappingInfo> mappingInfo;

    /**
     * 映射信息的缓存map，key是rowIndex，value是映射信息
     */
    private Map<Integer,ExcelMappingInfo> mappingInfoMap;

    /**
     * Java Bean的类型
     */
    private final Class<T> beanClass;

    /**
     * 对于公式单元格，是获取公式计算的值，还是公式本身。
     *      (为true代表获取公式计算的值)
     */
    private boolean getFormulaResult;

    /**
     * 类型转换器
     */
    private BeanTypeConvert typeConvert = new SimpleBeanTypeConvert();

    public VerticalAutoExcelBeanMapper(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public VerticalAutoExcelBeanMapper(List<ExcelMappingInfo> mappingInfo, Class<T> beanClass) {
        this.mappingInfo = mappingInfo;
        this.beanClass = beanClass;
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
     * @param columnStartIndex 起始列号
     * @param columnLength 要读取的总列数(加上了起始列号)
     * @param beanList javaBean列表（映射读取的总结果）
     */
    @Override
    public void mappingExcel2Bean(Row row, int rowIndex, int columnStartIndex, int columnLength, List<T> beanList) throws Exception {
        if(row == null) { //跳过整行都是空的
            return;
        }
        //遍历一行中所有要扫描的列
        for(int i = columnStartIndex; i < columnLength; i++) {
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
            ExcelMappingUtil.setValueIntoBean(cell,beanClass,bean,fieldName,typeConvert,getFormulaResult);
            if(isNewBean) {
                beanList.add(bean);
            }
        }//end of for
    }//end of mappingExcel2Bean

    //==================================================================================================================

    public void setGetFormulaResult(boolean getFormulaResult) {
        this.getFormulaResult = getFormulaResult;
    }

    public void setTypeConvert(BeanTypeConvert typeConvert) {
        this.typeConvert = typeConvert;
    }
}
