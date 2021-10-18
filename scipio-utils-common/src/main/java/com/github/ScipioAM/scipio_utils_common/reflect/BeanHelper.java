package com.github.ScipioAM.scipio_utils_common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JavaBean相关基础工具类
 * @author Alan Scipio
 * @since 1.0.2
 * @date 2021/10/13
 */
public class BeanHelper {

    public static void copyDifferenceSet(Object target, Object source) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Field[] tgtFields = target.getClass().getDeclaredFields();
        Field[] srcFields = source.getClass().getDeclaredFields();
        System.out.println("tgtFields.len: " + tgtFields.length);
        System.out.println("srcFields.len: " + srcFields.length);
        int innerLoop = 0;
        for(Field tgtField : tgtFields) {
            for(Field srcField : srcFields) {
                System.out.println("inner loop: " + (++innerLoop));
                if(!tgtField.getName().equals(srcField.getName()) || tgtField.getType() != srcField.getType()) {
                    continue;
                }
                Object value = getValueByMethod(srcField,source.getClass(),source);
                if(value == null) {
                    break;
                }
                Object originalValue = getValueByMethod(tgtField,target.getClass(),target);
                if(originalValue == null) {
                    break;
                }
                setValueByMethod(tgtField,target.getClass(),target,value);
                break;
            }
        }//end of outside for
    }// end of copyDifferenceSet

    private static Object getValueByMethod(Field field, Class<?> beanClass, Object beanInstance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fieldName = field.getName();
        Class<?> fieldClass = field.getType();
        //获取get方法
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method getMethod = beanClass.getDeclaredMethod(getMethodName);
        return getMethod.invoke(beanInstance);
    }

    private static void setValueByMethod(Field field, Class<?> beanClass, Object beanInstance, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fieldName = field.getName();
        Class<?> fieldClass = field.getType();
        //获取set方法
        String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method setMethod = beanClass.getDeclaredMethod(setMethodName,fieldClass);
        setMethod.invoke(beanInstance, value);
    }

}
