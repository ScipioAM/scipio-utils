package com.github.ScipioAM.scipio_utils_doc.excel.convert;

/**
 * 类型转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/17
 */
@FunctionalInterface
public interface BeanTypeConvert {

    /**
     * 类型转换
     * @param originalValue 原始值
     * @param originalType 原始值的类型
     * @param targetType 预期类型
     * @return 转换后的值
     */
    Object convert(Object originalValue, Class<?> originalType, Class<?> targetType) throws IllegalStateException;

}
