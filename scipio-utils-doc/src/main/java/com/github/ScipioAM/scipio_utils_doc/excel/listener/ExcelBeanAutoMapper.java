package com.github.ScipioAM.scipio_utils_doc.excel.listener;

import org.apache.poi.ss.usermodel.Row;

import java.util.Map;

/**
 * Excel - JavaBean 自动转换器(只需要指定映射关系即可)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/16
 */
public class ExcelBeanAutoMapper<T> implements ExcelBeanMapper<T>{

    /**
     * 映射信息
     * <ul>
     *      <li>Key: excel每一行中，要映射列的下标(0-based)</li>
     *      <li>Value: JavaBean字段名称(大小写敏感)</li>
     * </ul>
     */
    private Map<Integer,String> mappingInfo;

    public ExcelBeanAutoMapper() {}

    public ExcelBeanAutoMapper(Map<Integer, String> mappingInfo) {
        this.mappingInfo = mappingInfo;
    }

    public Map<Integer, String> getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(Map<Integer, String> mappingInfo) {
        this.mappingInfo = mappingInfo;
    }

    //==================================================================================================================

    @Override
    public T mapping(Row row, int rowIndex) {
        //TODO 待完成
        return null;
    }

    //==================================================================================================================

}
