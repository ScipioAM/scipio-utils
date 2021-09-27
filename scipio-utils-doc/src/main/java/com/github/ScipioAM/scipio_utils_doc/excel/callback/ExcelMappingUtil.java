package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel映射相关工具类
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/9/27
 */
class ExcelMappingUtil {

    /**
     * 根据指定class中的注解情况，构建映射信息list
     * @param beanClass 指定的class
     * @param checkCellIndex 是否检查cellIndex的值（检查值是否大于等于0）
     * @param checkRowIndex 是否检查rowIndex的值（检查值是否大于等于0）
     * @return 映射信息list，如果没有{@link ExcelMapping}注解，则为空list
     */
    static List<ExcelMappingInfo> buildFromAnnotations(Class<?> beanClass, boolean checkCellIndex, boolean checkRowIndex) {
        List<ExcelMappingInfo> infoList = new ArrayList<>();
        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields) {
            if(!field.isAnnotationPresent(ExcelMapping.class)) { //跳过没有被注解的字段
                continue;
            }
            //获取注解
            ExcelMapping mappingAnnotation = field.getDeclaredAnnotation(ExcelMapping.class);
            //获取注解值
            int cellIndex = mappingAnnotation.cellIndex();
            if(checkCellIndex && cellIndex < 0) {
                throw new IllegalStateException("cellIndex can not less then 0");
            }
            int rowIndex = mappingAnnotation.rowIndex();
            if(rowIndex < 0) {
                throw new IllegalStateException("rowIndex can not less then 0, when using vertical read mode");
            }
            ExcelMappingInfo infoBean = new ExcelMappingInfo(cellIndex,rowIndex,field.getName());
            infoList.add(infoBean);
        }
        return infoList;
    }

    /**
     * 将单元格的值set进javaBean的指定字段（调用set方法）
     * @param cell 单元格对象
     * @param beanClass javaBean的类型
     * @param bean javaBean实例对象
     * @param fieldName 指定字段名
     * @param typeConvert 类型转换器
     * @param getFormulaResult 是否计算公式
     * @param <T> javaBean的类型
     * @throws NoSuchFieldException 没有指定的字段
     * @throws NoSuchMethodException 没有字段对应的set方法
     * @throws InvocationTargetException 反射执行set方法失败
     * @throws IllegalAccessException 反射执行set方法失败（没有访问权限）
     */
    static <T> void setValueIntoBean(Cell cell, Class<T> beanClass, T bean, String fieldName, BeanTypeConvert typeConvert, boolean getFormulaResult) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object cellValue = ExcelUtil.getCellValue(cell,getFormulaResult);
        if(cellValue != null) {
            //获取字段类型
            Field field = beanClass.getDeclaredField(fieldName);
            Class<?> fieldClass = field.getType();
            //类型检查和转换
            Object finalCellValue = typeConvert.convert(cellValue,cellValue.getClass(),fieldClass);
            //获取set方法
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method setMethod = beanClass.getDeclaredMethod(setMethodName,fieldClass);
            //执行set方法
            setMethod.invoke(bean, finalCellValue);
        }
    }

}