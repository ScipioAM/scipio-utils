package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.reflect.FieldUtil;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.AutoExcelBeanMapper;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelBeanMapper;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @date 2021/9/18
 */
public class ExcelBeanOperator extends ExcelOperator {

    /**
     * 行检查白名单（不在此清单中的都是要跳过的）（为null则视为都不跳过）
     */
    protected final Set<Integer> rowWhitelist = new HashSet<>();

    protected <T> OpPrepareVo operationPrepare(ExcelBeanMapper<T> beanMapper, boolean createIfNotExists) {
        //参数检查
        if(beanMapper == null) {
            throw new NullPointerException("argument \"ExcelBeanMapper\" is null");
        }
        paramsCheck();
        //获取目标Sheet
        Sheet sheet = getSheet(excelIndex,workbook,createIfNotExists);
        //确定最终的总行数
        Integer rowLength = determineRowLength(excelIndex,sheet);
        //确定行白名单（跳过这些行）
        if(beanMapper instanceof AutoExcelBeanMapper) {
            AutoExcelBeanMapper<T> autoMapper = (AutoExcelBeanMapper<T>) beanMapper;
            checkRowWhitelist(autoMapper.getMappingInfo(),autoMapper.getBeanClass(),rowLength);
        }
        return new OpPrepareVo(sheet,rowLength);
    }

    /**
     * 检查构建行白名单
     * @param infoList 自定义映射信息list
     * @param beanClass 目标JavaBean的类型
     * @param rowLength 要处理的总行数
     * @throws IllegalStateException 注解模式下。目标JavaBean没有任何{@link ExcelMapping}注解
     */
    protected void checkRowWhitelist(List<ExcelMappingInfo> infoList, Class<?> beanClass, int rowLength) throws IllegalStateException {
        Set<Integer> rowIndexSet = new HashSet<>();
        int checkCount = 0;
        if(infoList != null) {
            for(ExcelMappingInfo info : infoList) {
                Integer rowIndex = info.getRowIndex();
                if(rowIndex != null && rowIndex >= 0) {
                    rowIndexSet.add(rowIndex);
                    checkCount++;
                }
            }
        }
        else {
            List<ExcelMapping> mappingAnnotations = FieldUtil.getAnnotationFromFields(beanClass,ExcelMapping.class);
            if(mappingAnnotations.size() <= 0) {
                throw new IllegalStateException("target class[" + beanClass.getCanonicalName() + "] has no annotation[@ExcelMapping] found!");
            }
            for(ExcelMapping info : mappingAnnotations) {
                if(info.rowIndex() >= 0) {
                    rowIndexSet.add(info.rowIndex());
                    checkCount++;
                }
            }
        }
        if(checkCount == rowLength) {
            rowWhitelist.addAll(rowIndexSet);
        }
        else {
            rowWhitelist.clear();
        }
    }

    /**
     * 内部类：操作前准备的对象数据
     */
    static class OpPrepareVo {
        Sheet sheet;
        Integer rowLength;

        OpPrepareVo(Sheet sheet, Integer rowLength) {
            this.sheet = sheet;
            this.rowLength = rowLength;
        }
    }

}
