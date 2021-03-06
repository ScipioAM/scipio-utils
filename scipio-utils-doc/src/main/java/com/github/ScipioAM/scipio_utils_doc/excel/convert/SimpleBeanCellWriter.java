package com.github.ScipioAM.scipio_utils_doc.excel.convert;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alan Scipio
 * @date 2021/9/23
 * @since 1.0.2-p3
 */
public class SimpleBeanCellWriter implements BeanCellWriter {

    @Override
    public void writeIntoCell(Cell cell, Object value, Class<?> valueType, NullWritingPolicy policy) throws ExcelException {
        if (value == null) { //值为null则等于不写入
            if (policy != NullWritingPolicy.IGNORE) {
                cell.setBlank();
            }
            return;
        }
        if (valueType == Integer.class) {
            Integer trueValue = (Integer) value;
            cell.setCellValue(trueValue.doubleValue());
        } else if (valueType == Long.class) {
            Long trueValue = (Long) value;
            cell.setCellValue(trueValue.doubleValue());
        } else if (valueType == Double.class) {
            cell.setCellValue((Double) value);
        } else if (valueType == Float.class) {
            Float trueValue = (Float) value;
            cell.setCellValue(trueValue.doubleValue());
        } else if (valueType == Boolean.class) {
            cell.setCellValue((Boolean) value);
        } else if (valueType == LocalDate.class) {
            cell.setCellValue((LocalDate) value);
        } else if (valueType == LocalDateTime.class) {
            cell.setCellValue((LocalDateTime) value);
        } else if (valueType == String.class) {
            cell.setCellValue((String) value);
        } else if (valueType == BigDecimal.class) {
            BigDecimal trueValue = (BigDecimal) value;
            cell.setCellValue(trueValue.doubleValue());
        } else if (valueType == Character.class) {
            Character trueValue = (Character) value;
            cell.setCellValue(trueValue.toString());
        } else if (valueType == Date.class) {
            cell.setCellValue((Date) value);
        } else if (valueType == Calendar.class) {
            cell.setCellValue((Calendar) value);
        } else if (valueType == RichTextString.class) {
            cell.setCellValue((RichTextString) value);
        } else {
            throw new ExcelException("Unknown type, while writing data into excel!")
                    .setSheetName(cell.getSheet().getSheetName())
                    .setRowIndex(cell.getRowIndex())
                    .setCellIndex(cell.getColumnIndex());
        }
    }

}
