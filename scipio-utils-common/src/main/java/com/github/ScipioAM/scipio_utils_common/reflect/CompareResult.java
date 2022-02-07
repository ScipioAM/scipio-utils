package com.github.ScipioAM.scipio_utils_common.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ObjectComparator}的比较结果
 * @author mindingqi
 * @since 2022/2/7
 */
public class CompareResult {

    /** 对象1的类型 */
    private Class<?> objType1;

    /** 对象2的类型 */
    private Class<?> objType2;

    /** 比较出错时的错误信息 */
    private String error;

    /** 相等字段(字段名、类型均相等，大小写敏感) */
    private List<FieldComparisonInfo> equalList;

    /** 不相等字段(字段名或类型不相等，大小写敏感) */
    private List<FieldComparisonInfo> diffList;

    public void addEqualField(Field field1, Field field2, String comparisonInfo) {
        if(equalList == null) {
            equalList = new ArrayList<>();
        }
        equalList.add(new FieldComparisonInfo(field1, field2, comparisonInfo));
    }

    public void addDiffField(Field field1, Field field2, String comparisonInfo) {
        if(diffList == null) {
            diffList = new ArrayList<>();
        }
        diffList.add(new FieldComparisonInfo(field1, field2, comparisonInfo));
    }

    public void addDiffField1(Field field1, String comparisonInfo) {
        if(diffList == null) {
            diffList = new ArrayList<>();
        }
        diffList.add(new FieldComparisonInfo(field1, true, comparisonInfo));
    }

    public void addDiffField2(Field field2, String comparisonInfo) {
        if(diffList == null) {
            diffList = new ArrayList<>();
        }
        diffList.add(new FieldComparisonInfo(field2, false, comparisonInfo));
    }

    public void printReport(boolean printEqualedFields) {
        if(error == null || "".equals(error)) {
            System.out.println("\n**********************************************");
            System.out.println("*           OBJECT COMPARISON REPORT         *");
            System.out.println("**********************************************\n");
            System.out.println("Object1`s type: " + objType1.getName());
            System.out.println("Object2`s type: " + objType2.getName() + "\n");
            int i = 1;
            //打印差异的信息
            if(diffList != null) {
                for(FieldComparisonInfo info : diffList) {
                    System.out.println("[" + (i++) + "] " + info.getComparisonInfo());
                }
            }
            //打印相同的信息
            if(printEqualedFields && equalList != null) {
                for(FieldComparisonInfo info : equalList) {
                    System.out.println("[" + (i++) + "] " + info.getComparisonInfo());
                }
            }
            System.out.println("\n");
        } else {
            System.out.println("\n**********************************************");
            System.out.println("*           OBJECT COMPARISON ERROR          *");
            System.out.println("**********************************************\n");
            System.out.println("Object1`s type: " + objType1.getName());
            System.out.println("Object2`s type: " + objType2.getName() + "\n");
            System.out.println(error);
            System.out.println("\n");
        }
    }

    public void printReport() {
        printReport(false);
    }

    public Class<?> getObjType1() {
        return objType1;
    }

    public void setObjType1(Class<?> objType1) {
        this.objType1 = objType1;
    }

    public Class<?> getObjType2() {
        return objType2;
    }

    public void setObjType2(Class<?> objType2) {
        this.objType2 = objType2;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<FieldComparisonInfo> getEqualList() {
        return equalList;
    }

    public void setEqualList(List<FieldComparisonInfo> equalList) {
        this.equalList = equalList;
    }

    public List<FieldComparisonInfo> getDiffList() {
        return diffList;
    }

    public void setDiffList(List<FieldComparisonInfo> diffList) {
        this.diffList = diffList;
    }

    //================================================================================================================================

    public static class FieldComparisonInfo {
        private String field1Name;
        private String field2Name;
        private Class<?> field1Type;
        private Class<?> field2Type;
        private String comparisonInfo;

        public FieldComparisonInfo(Field field1, Field field2, String comparisonInfo) {
            this.field1Name = field1.getName();
            this.field2Name = field2.getName();
            this.field1Type = field1.getType();
            this.field2Type = field2.getType();
            this.comparisonInfo = comparisonInfo;
        }

        public FieldComparisonInfo(Field field, boolean isField1, String comparisonInfo) {
            if(isField1) {
                this.field1Name = field.getName();
                this.field1Type = field.getType();
            } else {
                this.field2Name = field.getName();
                this.field2Type = field.getType();
            }
            this.comparisonInfo = comparisonInfo;
        }

        /**
         * 字段1和字段2是否完全相等（字段名和类型均相等，大小写敏感）
         * @return true代表完全相等
         */
        public boolean isFieldsEqual() {
            return (field1Name.equals(field2Name) && field1Type == field2Type);
        }

        public String getField1Name() {
            return field1Name;
        }

        public void setField1Name(String field1Name) {
            this.field1Name = field1Name;
        }

        public String getField2Name() {
            return field2Name;
        }

        public void setField2Name(String field2Name) {
            this.field2Name = field2Name;
        }

        public Class<?> getField1Type() {
            return field1Type;
        }

        public void setField1Type(Class<?> field1Type) {
            this.field1Type = field1Type;
        }

        public Class<?> getField2Type() {
            return field2Type;
        }

        public void setField2Type(Class<?> field2Type) {
            this.field2Type = field2Type;
        }

        public String getComparisonInfo() {
            return comparisonInfo;
        }

        public void setComparisonInfo(String comparisonInfo) {
            this.comparisonInfo = comparisonInfo;
        }
    }

}
