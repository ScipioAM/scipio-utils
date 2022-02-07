package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对象比较器
 *
 * @author mindingqi
 * @since 2022/2/7
 */
public class ObjectComparator {

    /**
     * 比较class的字段
     * @return 比较结果
     */
    public CompareResult compareFields(Class<?> clazz1, Class<?> clazz2) {
        CompareResult result = new CompareResult();
        result.setObjType1(clazz1);
        result.setObjType2(clazz2);
        try {
            //获取字段
            Field[] fields1 = clazz1.getDeclaredFields();
            Field[] fields2 = clazz2.getDeclaredFields();
            //准备比对map
            Map<String, Field> fields2Map = new LinkedHashMap<>();
            for (Field field2 : fields2) {
                fields2Map.put(field2.getName(), field2);
            }
            //比对字段1所有，和字段2里能对应的
            for(Field field1 : fields1) {
                StringBuilder compareInfo = new StringBuilder();
                Field field2 = fields2Map.get(field1.getName());
                if(field2 == null) {
                    //字段1没有对应字段2的
                    compareInfo.append("(EXTRA FIELD)   ");
                    compareInfo.append("Field1 [").append(field1.getName()).append("] type1[").append(field1.getType()).append("]");
                    result.addDiffField1(field1, compareInfo.toString());
                } else {
                    //字段1有对应字段2的
                    if (field1.getType() != field2.getType()) {
                        //字段1字段2的类型不一样
                        compareInfo.append("(TYPE NOT EQUAL)   ");
                        compareInfo.append("Field1 [").append(field1.getName()).append("] type1[").append(field1.getType()).append("],   ")
                                .append("Field2 [").append(field2.getName()).append("] type2[").append(field2.getType()).append("]");
                        result.addDiffField(field1, field2, compareInfo.toString());
                    } else {
                        //字段1字段2的类型也完全一样
                        compareInfo.append("(EQUAL)   ");
                        compareInfo.append("Field1 [").append(field1.getName()).append("] type1[").append(field1.getType()).append("],   ")
                                .append("Field2 [").append(field2.getName()).append("] type2[").append(field2.getType()).append("]");
                        result.addEqualField(field1, field2, compareInfo.toString());
                    }
                    //去除有对应的字段，这样最后map里只剩没有能对应的字段2
                    fields2Map.remove(field1.getName());
                }
            }
            //比对字段2里没有对应的
            for(Map.Entry<String, Field> entry : fields2Map.entrySet()) {
                Field field2 = entry.getValue();
                String compareInfo = "(EXTRA FIELD)   " +
                        "Field2 [" + field2.getName() + "] type2[" + field2.getType() + "]";
                result.addDiffField2(field2, compareInfo);
            }
        } catch (Exception e) {
            result.setError(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 比较外部两个class文件的字段
     * @return 比较结果
     */
    public CompareResult compareFieldsFromClassFile(File classFile1, File classFile2) {
        System.err.println("method[compareFieldsFromClassFile] is still developing...");
        return null;
    }

    /**
     * 比较外部两个java源码文件的字段
     * @return 比较结果
     */
    public CompareResult compareFieldsFromJavaFile(File javaFile1, File javaFile2) {
        System.err.println("method[compareFieldsFromJavaFile] is still developing...");
        return null;
    }

}
