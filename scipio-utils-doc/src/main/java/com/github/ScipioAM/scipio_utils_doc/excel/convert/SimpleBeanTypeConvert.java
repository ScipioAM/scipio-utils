package com.github.ScipioAM.scipio_utils_doc.excel.convert;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_common.time.DateTimeUtil;
import com.github.ScipioAM.scipio_utils_common.time.DateUtil;
import com.github.ScipioAM.scipio_utils_doc.excel.ExcelException;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 简易实现的类型转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/17
 */
public class SimpleBeanTypeConvert implements BeanTypeConvert{

    /**
     * 类型转换
     * @param cell 转换的单元格对象
     * @param originalValue 原始值
     * @param originalType 原始值的类型
     * @param targetType 预期类型
     * @return 转换后的值
     */
    @Override
    public Object convert(Cell cell, Object originalValue, Class<?> originalType, Class<?> targetType) throws ExcelException {
        if(originalType == targetType) { //类型一致，直接放行
            return originalValue;
        }

        //预期可转换的
        //原始值是数字
        if(originalType == Double.class) {
            Double originalDV = (Double) originalValue;
            return convertNumeric(originalDV,targetType);
        }
        //原始值是字符串
        else if(originalType == String.class) {
            String originalStr = (String) originalValue;
            return convertString(originalStr,targetType,cell);
        }
        //原始值是日期
        else if(originalType == LocalDateTime.class) {
            LocalDateTime dateTime = (LocalDateTime) originalValue;
            return convertDateTime(dateTime,targetType);
        }

        //不是预期可转换的，且类型又不一致，则抛出异常
        ExcelException e = new ExcelException("Type mismatch while read value from excel to javaBean, originalType["
                + originalType + "], targetType["
                + targetType + "], rowIndex["
                + cell.getRowIndex() + "], columnIndex["
                + cell.getColumnIndex() + "]");
        e.setSheetName(cell.getSheet().getSheetName())
                .setRowIndex(cell.getRowIndex())
                .setCellIndex(cell.getColumnIndex());
        throw e;
    }

    /**
     * 原始值是数字的转换
     */
    private Object convertNumeric(Double originalDV, Class<?> targetType) {
        if(targetType == Integer.class) {
            return originalDV.intValue();
        }
        else if(targetType == Long.class) {
            return originalDV.longValue();
        }
        else if(targetType == Float.class) {
            return originalDV.floatValue();
        }
        else if(targetType == BigDecimal.class) {
            return new BigDecimal(originalDV);
        }
        else if(targetType == Boolean.class) {
            if(originalDV == 0.0) {
                return Boolean.FALSE;
            }
            else if(originalDV == 1.0) {
                return Boolean.TRUE;
            }
        }
        return null;
    }

    /**
     * 原始值是字符串的转换
     */
    private Object convertString(String originalStr, Class<?> targetType, Cell cell) throws ExcelException {
        if(targetType == Boolean.class) {
            if(originalStr.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            }
            else if(originalStr.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            }
        }
        else if(targetType == BigDecimal.class) {
            if(!StringUtil.isNumeric(originalStr)) {
                return null;
//                throw new ExcelException("Type convert error while read value from excel to javaBean, cellValue is not a numeric string: ["
//                        + originalStr + "], rowIndex["
//                        + cell.getRowIndex() + "], columnIndex["
//                        + cell.getColumnIndex() + "]")
//                        .setSheetName(cell.getSheet().getSheetName())
//                        .setRowIndex(cell.getRowIndex())
//                        .setCellIndex(cell.getColumnIndex());
            }
            return new BigDecimal(originalStr);
        }
        else if(targetType == Integer.class) {
            if(!StringUtil.isIntNumeric(originalStr)) {
                throw new ExcelException("Type convert error while read value from excel to javaBean, cellValue is not a integer string: ["
                        + originalStr + "], rowIndex["
                        + cell.getRowIndex() + "], columnIndex["
                        + cell.getColumnIndex() + "]")
                        .setSheetName(cell.getSheet().getSheetName())
                        .setRowIndex(cell.getRowIndex())
                        .setCellIndex(cell.getColumnIndex());
            }
            return Integer.valueOf(originalStr);
        }
        else if(targetType == Long.class) {
            if(!StringUtil.isIntNumeric(originalStr)) {
                throw new ExcelException("Type convert error while read value from excel to javaBean, cellValue is not a integer string: ["
                        + originalStr + "], rowIndex["
                        + cell.getRowIndex() + "], columnIndex["
                        + cell.getColumnIndex() + "]")
                        .setSheetName(cell.getSheet().getSheetName())
                        .setRowIndex(cell.getRowIndex())
                        .setCellIndex(cell.getColumnIndex());
            }
            return Long.valueOf(originalStr);
        }
        else if(targetType == Float.class) {
            if(!StringUtil.isNumeric(originalStr)) {
                throw new ExcelException("Type convert error while read value from excel to javaBean, cellValue is not a numeric string: ["
                        + originalStr + "], rowIndex["
                        + cell.getRowIndex() + "], columnIndex["
                        + cell.getColumnIndex() + "]")
                        .setSheetName(cell.getSheet().getSheetName())
                        .setRowIndex(cell.getRowIndex())
                        .setCellIndex(cell.getColumnIndex());
            }
            return Float.valueOf(originalStr);
        }
        else if(targetType == Double.class) {
            if(!StringUtil.isNumeric(originalStr)) {
                throw new ExcelException("Type convert error while read value from excel to javaBean, cellValue is not a numeric string: ["
                        + originalStr + "], rowIndex["
                        + cell.getRowIndex() + "], columnIndex["
                        + cell.getColumnIndex() + "]")
                        .setSheetName(cell.getSheet().getSheetName())
                        .setRowIndex(cell.getRowIndex())
                        .setCellIndex(cell.getColumnIndex());
            }
            return Double.valueOf(originalStr);
        }
        else if(targetType == LocalDate.class) {
            String pattern = DateUtil.smartPatternGet(originalStr);
            return DateUtil.str2Date(originalStr, pattern);
        }
        else if(targetType == LocalDateTime.class) {
            return DateTimeUtil.str2Time(originalStr);
        }
        return null;
    }

    /**
     * 原始值是日期的转换
     */
    private Object convertDateTime(LocalDateTime dateTime, Class<?> targetType) {
        if(targetType == String.class) {
            return DateTimeUtil.time2Str(dateTime);
        }
        else if(targetType == Long.class){
            return DateTimeUtil.getTimestampFromObj(dateTime);
        }
        else if(targetType == BigDecimal.class) {
            long timestamp = DateTimeUtil.getTimestampFromObj(dateTime);
            BigInteger bigInteger = BigInteger.valueOf(timestamp);
            return new BigDecimal(bigInteger);
        }
        return null;
    }

}
