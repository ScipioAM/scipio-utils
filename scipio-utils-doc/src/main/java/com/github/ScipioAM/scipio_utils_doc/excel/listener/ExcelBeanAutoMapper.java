package com.github.ScipioAM.scipio_utils_doc.excel.listener;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Excel - JavaBean 自动转换器(只需要指定映射关系即可)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/16
 */
public class ExcelBeanAutoMapper<T> implements ExcelBeanMapper<T>{

    /**
     * 映射信息(优先级高于{@link ExcelMapping})
     * <ul>
     *      <li>Key: excel每一行中，要映射列的下标(0-based)</li>
     *      <li>Value: JavaBean字段名称(大小写敏感)</li>
     * </ul>
     */
    private Map<Integer,String> mappingInfo;

    /**
     * Java Bean的类型
     */
    private Class<T> beanClass;

    /**
     * 对于公式单元格，是获取公式计算的值，还是公式本身。
     *      (为true代表获取公式计算的值)
     */
    private boolean getFormulaResult = true;

    public ExcelBeanAutoMapper() {}

    public ExcelBeanAutoMapper(Map<Integer, String> mappingInfo, Class<T> beanClass) {
        this.mappingInfo = mappingInfo;
        this.beanClass = beanClass;
    }

    public Map<Integer, String> getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(Map<Integer, String> mappingInfo) {
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

    //==================================================================================================================

    @Override
    public T mapping(Row row, int rowIndex) throws Exception {
        //实例化一个javaBean
        T bean = beanClass.getDeclaredConstructor().newInstance();
        //依据map来映射
        if(mappingInfo != null) {
            //开始循环获取每行中的值，并set入这个bean里
            for(Map.Entry<Integer,String> info : mappingInfo.entrySet()) {
                Integer cellIndex = info.getKey();
                String fieldName = info.getValue();
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
                    //获取set方法
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = beanClass.getDeclaredMethod(setMethodName,fieldClass);
                    //执行set方法
                    setMethod.invoke(bean, cellValue);
                }
            }
        }
        //依据注解来映射
        else {
            Field[] fields = beanClass.getDeclaredFields();
            for(Field field : fields) {
                if(!field.isAnnotationPresent(ExcelMapping.class)) { //跳过没有被注解的字段
                    continue;
                }
                //获取注解
                ExcelMapping mappingAnnotation = field.getDeclaredAnnotation(ExcelMapping.class);
                //根据注解的值获取index，并依次获取单元格
                int cellIndex = mappingAnnotation.cellIndex();
                Cell cell = row.getCell(cellIndex);
                if(cell == null) {
                    System.err.println("Cell is null, rowIndex[" + rowIndex + "], cellIndex[" + cellIndex + "]");
                    continue;
                }
                //获取单元格的值
                Object cellValue = ExcelUtil.getCellValue(cell,getFormulaResult);
                if(cellValue != null) {
                    //获取set方法
                    String fieldName = field.getName();
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = beanClass.getDeclaredMethod(setMethodName,field.getType());
                    //执行set方法
                    setMethod.invoke(bean, cellValue);
                }
            }
        }
        return bean;
    }

    //==================================================================================================================

    /**
     * 检查单元格值，如果是浮点型则转换为整型
     * @param originalValue 原始读取的单元格值
     * @param fieldClass 字段类型
     * @return 最终的单元格值，如果原始值不是浮点型则原样输出
     */
    private Object changeFloat2Int(Object originalValue, Class<?> fieldClass) {
        if(originalValue instanceof Double) {
            if(fieldClass == Integer.class) {

            }
            else if(fieldClass == Long.class) {

            }
            return null; //TODO 待完成，考虑单元格值的转换，追加一个TypeConverter
        }
        else {
            return originalValue;
        }
    }

}
