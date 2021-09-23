package com.github.ScipioAM.scipio_utils_doc.excel.convert;

import java.math.BigDecimal;

/**
 * 简易实现的类型转换器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/17
 */
public class SimpleBeanTypeConvert implements BeanTypeConvert{

    @Override
    public Object convert(Object originalValue, Class<?> originalType, Class<?> targetType) throws IllegalStateException {
        if(originalType == targetType) { //类型一致，直接放行
            return originalValue;
        }

        //预期可转换的
        if(originalType == Double.class) {
            Double originalDV = (Double) originalValue;
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
        }
        else if(originalType == String.class && targetType == Boolean.class) {
            String originalStr = (String) originalValue;
            if(originalStr.equalsIgnoreCase("true")) {
                return true;
            }
            else if(originalStr.equalsIgnoreCase("false")) {
                return false;
            }
        }

        //不是预期可转换的，且类型又不一致，则抛出异常
        throw new IllegalStateException("Type mismatch while read value from excel to javaBean, originalType[" + originalType + "], targetType[" + targetType + "]");
    }

}
