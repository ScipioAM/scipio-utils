package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_common.reflect.FieldUtil;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanCellWriter;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.SimpleBeanCellWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Excel - JavaBean 自动转换器(只需要指定映射关系即可)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/16
 */
public class AutoExcelBeanMapper<T> extends BaseExcelBeanMapper<T>{
    
    /** JavaBean写入excel的具体实现者 */
    private BeanCellWriter cellWriter = new SimpleBeanCellWriter();

    public AutoExcelBeanMapper(Class<T> beanClass) {
        super.beanClass = beanClass;
    }

    public AutoExcelBeanMapper(List<ExcelMappingInfo> mappingInfo, Class<T> beanClass) {
        super.mappingInfo = mappingInfo;
        super.beanClass = beanClass;
    }

    //==================================================================================================================

    /**
     * 映射：excel -> JavaBean
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数
     * @return 通过映射，新生成的一个JavaBean实例
     */
    @Override
    public T mappingExcel2Bean(Row row, int rowIndex, int rowLength) throws Exception {
        if(row == null) { //跳过整行都是空的
            return null;
        }
        //从注解中获取映射信息(前提是没有用参数指定映射信息)
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            mappingInfo = ExcelMappingUtil.buildFromAnnotations(beanClass,true,false);
        }
        //实例化一个javaBean
        T bean = beanClass.getDeclaredConstructor().newInstance();
        //开始循环获取每行中的值，并set入这个bean里
        int fieldCount = 0;//要映射的字段总数
        int nullCount = 0;//空值字段总数
        for(ExcelMappingInfo info : mappingInfo) {
            Integer cellIndex = info.getCellIndex();
            String fieldName = info.getFieldName();
            Integer mappingRowIndex = info.getRowIndex();
            if(mappingRowIndex != null && mappingRowIndex >=0 && mappingRowIndex != rowIndex) {
                continue;//启用行索引(不为空且大于等于0)，且当前行不是指定的行，则跳过
            }
            //获取单元格的值
            Cell cell = row.getCell(cellIndex);
            if(cell == null) {
                nullCount++;
                System.err.println("Cell is null, rowIndex[" + rowIndex + "], cellIndex[" + cellIndex + "]");
                continue;
            }
            
            //单元格处理监听器
            if(cellHandler != null && !cellHandler.handle(cell,rowIndex,cellIndex,rowLength,mappingInfo.size())) {
                break;
            }
            
            fieldCount++;
            ExcelMappingUtil.setValueIntoBean(cell,beanClass,bean,fieldName,typeConvert,getFormulaResult,cellIgnoreHandler);

            //对每个bean的监听回调
            if(beanListener != null && !beanListener.onHandle(true,bean,cell,rowLength,mappingInfo.size())) {
                break;
            }
        }// end of for
        //如果所有映射字段都是空值，则不构成bean实例
        if(nullCount == fieldCount) {
            bean = null;
        }
        return bean;
    }

    /**
     * 映射：JavaBean -> excel
     * @param row 行对象
     * @param rowIndex 行索引(0-based)
     * @param rowLength 要读取的总行数
     * @param bean JavaBean实例(将它的数据写入excel)
     */
    @Override
    public void mappingBean2Excel(Row row, int rowIndex, int rowLength, T bean) throws Exception {
        //依据自定义list来映射
        if(mappingInfo != null) {
            //开始循环的从bean写入到excel
            for(ExcelMappingInfo info : mappingInfo) {
                Integer cellIndex = info.getCellIndex();
                Integer mappingRowIndex = info.getRowIndex();
                if(mappingRowIndex != null && mappingRowIndex >=0 && mappingRowIndex != rowIndex) {
                    continue;//启用行索引(不为空且大于等于0)，且当前行不是指定的行，则跳过
                }
                //获取单元格对象
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    cell = row.createCell(cellIndex);
                }

                //单元格处理监听器
                if(cellHandler != null && !cellHandler.handle(cell,rowIndex,cellIndex,rowLength,mappingInfo.size())) {
                    break;
                }

                //对每个bean的监听回调
                if(beanListener != null && !beanListener.onHandle(false,bean,cell,rowLength,mappingInfo.size())) {
                    break;
                }
                
                //获取字段值
                String fieldName = info.getFieldName();
                Field field = beanClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> fieldClass = field.getType();
                Object fieldValue = field.get(bean);
                //写入单元格中
                cellWriter.writeIntoCell(cell, fieldValue, fieldClass);
            }
        }
        //依据注解来映射
        else {
            List<Field> fields = FieldUtil.getFieldsByAnnotationPresent(beanClass,ExcelMapping.class);
            if(fields.size() <= 0) {
                System.err.println("There has no field annotated by [@ExcelMapping]");
                return;
            }
            for(Field field : fields) {
                //获取注解
                ExcelMapping mappingAnnotation = field.getDeclaredAnnotation(ExcelMapping.class);
                //根据注解的值获取rowIndex
                if(mappingAnnotation.rowIndex() >= 0 && rowIndex != mappingAnnotation.rowIndex()) {
                    continue;//启用行索引(大于等于0)，且当前行不是指定的行，则跳过
                }
                //获取单元格对象
                int cellIndex = mappingAnnotation.cellIndex();
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    cell = row.createCell(cellIndex);
                }

                //单元格处理监听器
                if(cellHandler != null && !cellHandler.handle(cell,rowIndex,cellIndex,rowLength,fields.size())) {
                    break;
                }

                //对每个bean的监听回调
                if(beanListener != null && !beanListener.onHandle(false,bean,cell,rowLength,fields.size())) {
                    break;
                }
                
                //获取字段值
                field.setAccessible(true);
                Class<?> fieldClass = field.getType();
                Object fieldValue = field.get(bean);
                //写入单元格中
                cellWriter.writeIntoCell(cell, fieldValue, fieldClass);
            }
        }
    }

    //==================================================================================================================

    public void setCellWriter(BeanCellWriter cellWriter) {
        this.cellWriter = cellWriter;
    }

}
