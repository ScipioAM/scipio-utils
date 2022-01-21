package com.github.ScipioAM.scipio_utils_doc.csv.bean;

import com.github.ScipioAM.scipio_utils_common.time.DateTimeUtil;
import com.github.ScipioAM.scipio_utils_common.time.DateUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * CSV写入时的元信息
 * @author Alan Scipio
 * @since 1.0.9 2021/12/30
 */
public class CSVMeta {

    private int sort;

    private String title;

    private String content;

    private Field field;

    public CSVMeta() {}

    public CSVMeta(int sort, String title, Field field) {
        this.sort = sort;
        this.title = title;
        this.field = field;
    }

    /**
     * 获取字段的值
     */
    public String getFieldValue(Object beanInstance) throws IllegalAccessException {
        if(field == null) {
            return null;
        }
        field.setAccessible(true);
        Object value = field.get(beanInstance);
        return convert2Str(value, field.getType());
    }

    /**
     * 值转换
     * @param value 原始值
     * @param valueClass 原始值的类型
     * @return 转换后的字符串值
     */
    private String convert2Str(Object value, Class<?> valueClass) {
        if(value == null) {
            return "";
        }
        else if(valueClass == String.class) {
            return (String) value;
        }
        else if(valueClass == LocalDate.class) {
            return DateUtil.date2Str((LocalDate) value);
        }
        else if(valueClass == LocalDateTime.class) {
            return DateTimeUtil.time2Str((LocalDateTime) value);
        }
        else if(valueClass == Date.class) {
            return DateUtil.date2String((Date) value);
        }
        else {
            return value.toString();
        }
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
