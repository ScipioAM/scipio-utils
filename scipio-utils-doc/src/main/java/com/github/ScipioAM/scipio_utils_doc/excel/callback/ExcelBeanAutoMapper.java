package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.SimpleBeanTypeConvert;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Excel - JavaBean 自动转换器(只需要指定映射关系即可)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/16
 */
public class ExcelBeanAutoMapper<T> implements ExcelBeanMapper<T>{

    /**
     * 自定义映射信息(优先级高于{@link ExcelMapping})
     */
    private List<ExcelMappingInfo> mappingInfo;

    /**
     * Java Bean的类型
     */
    private Class<T> beanClass;

    /**
     * 对于公式单元格，是获取公式计算的值，还是公式本身。
     *      (为true代表获取公式计算的值)
     */
    private boolean getFormulaResult = true;

    /**
     * 类型转换器
     */
    private final BeanTypeConvert typeConvert = new SimpleBeanTypeConvert();

    public ExcelBeanAutoMapper() {}

    public ExcelBeanAutoMapper(List<ExcelMappingInfo> mappingInfo, Class<T> beanClass) {
        this.mappingInfo = mappingInfo;
        this.beanClass = beanClass;
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
        //实例化一个javaBean
        T bean = beanClass.getDeclaredConstructor().newInstance();
        //依据map来映射
        if(mappingInfo != null) {
            //开始循环获取每行中的值，并set入这个bean里
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
                    System.err.println("Cell is null, rowIndex[" + rowIndex + "], cellIndex[" + cellIndex + "]");
                    continue;
                }
                Object cellValue = ExcelUtil.getCellValue(cell,getFormulaResult);
                if(cellValue != null) {
                    //获取字段类型
                    Field field = beanClass.getDeclaredField(fieldName);
                    Class<?> fieldClass = field.getType();
                    //类型检查和转换
                    Object finalCellValue = typeConvert(cellValue,fieldClass);
                    //获取set方法
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = beanClass.getDeclaredMethod(setMethodName,fieldClass);
                    //执行set方法
                    setMethod.invoke(bean, finalCellValue);
                }
            }// end of for
        } //end of outside if
        //依据注解来映射
        else {
            Field[] fields = beanClass.getDeclaredFields();
            int fieldCount = 0;//要映射的字段总数
            int nullCount = 0;//空值字段总数
            for(Field field : fields) {
                if(!field.isAnnotationPresent(ExcelMapping.class)) { //跳过没有被注解的字段
                    continue;
                }
                fieldCount++;
                //获取注解
                ExcelMapping mappingAnnotation = field.getDeclaredAnnotation(ExcelMapping.class);
                //根据注解的值获取rowIndex
                int mappingRowIndex = mappingAnnotation.rowIndex();
                if(mappingRowIndex >= 0 && rowIndex != mappingRowIndex) {
                    continue;//启用行索引(大于等于0)，且当前行不是指定的行，则跳过
                }
                //根据注解的值获取cellIndex，并依次获取单元格
                int cellIndex = mappingAnnotation.cellIndex();
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    nullCount++;
                    System.err.println("Cell is null, rowIndex[" + rowIndex + "], cellIndex[" + cellIndex + "]");
                    continue;
                }
                //获取单元格的值
                Object cellValue = ExcelUtil.getCellValue(cell,getFormulaResult);
                if(cellValue != null) {
                    //类型检查和转换
                    Object finalCellValue = typeConvert(cellValue,field.getType());
                    //获取set方法
                    String fieldName = field.getName();
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = beanClass.getDeclaredMethod(setMethodName,field.getType());
                    //执行set方法
                    setMethod.invoke(bean, finalCellValue);
                }
            }
            //如果所有映射字段都是空值，则不构成bean实例
            if(nullCount == fieldCount) {
                bean = null;
            }
        }
        return bean;
    }

    //==================================================================================================================

    /**
     * 类型转换
     * @param originalValue 单元格原始值
     * @param fieldClass 字段类型（预期要转的类型）
     * @return 转换后的单元格值
     * @throws IllegalStateException JavaBean类型有问题，转不了
     */
    private Object typeConvert(Object originalValue, Class<?> fieldClass) throws IllegalStateException {
        return typeConvert.convert(originalValue,originalValue.getClass(),fieldClass);
    }

    //==================================================================================================================

    public List<ExcelMappingInfo> getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(List<ExcelMappingInfo> mappingInfo) {
        this.mappingInfo = mappingInfo;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isGetFormulaResult() {
        return getFormulaResult;
    }

    public void setGetFormulaResult(boolean getFormulaResult) {
        this.getFormulaResult = getFormulaResult;
    }

    public BeanTypeConvert getTypeConvert() {
        return typeConvert;
    }
}
