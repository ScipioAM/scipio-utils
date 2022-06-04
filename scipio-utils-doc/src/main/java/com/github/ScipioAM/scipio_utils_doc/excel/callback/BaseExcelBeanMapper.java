package com.github.ScipioAM.scipio_utils_doc.excel.callback;

import com.github.ScipioAM.scipio_utils_common.reflect.TypeHelper;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.SimpleBeanTypeConvert;

import java.util.List;

/**
 * @author Alan Scipio
 * @since 1.0.4
 * @date 2021/10/28
 */
public abstract class BaseExcelBeanMapper<T> implements ExcelBeanMapper<T> {

    /** 自定义映射信息(非null时优先级高于{@link com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping}) */
    protected List<ExcelMappingInfo> mappingInfo;

    /** Java Bean的类型 */
    protected Class<T> beanClass;

    /** 对于公式单元格，是获取公式计算的值，还是公式本身。 (为true代表获取公式计算的值) */
    protected boolean getFormulaResult;

    /** 单元格监听器 */
    protected ExcelCellHandler cellHandler;

    /** 单元格忽略处理器 */
    protected CellIgnoreHandler cellIgnoreHandler;

    /** 类型转换器 */
    protected BeanTypeConvert typeConvert = new SimpleBeanTypeConvert();

    /** 对每个JavaBean处理时的监听回调 */
    protected BeanListener<T> beanListener;

    /** 连续多少次空单元格后，认为该行读取结束了（0则永远不会这样强行结束） */
    protected int emptyColumnLimit;

    /**
     * 检查泛型类型并设置{@link BeanListener}
     * @param targetClass  预期类型
     * @param beanListener 监听器对象
     * @throws IllegalArgumentException 监听器的泛型类型与预期类型不一致
     */
    @SuppressWarnings("unchecked")
    public void checkAndSetBeanListener(Class<?> targetClass, BeanListener<?> beanListener) throws IllegalArgumentException {
        if(beanListener != null) { //beanListener为null则略过
            Class<?> listenerGenericType = TypeHelper.resolveRawArgument(BeanListener.class,beanListener);
            if(targetClass == listenerGenericType) { //检查BeanListener类型与预期类型是否一致
                setBeanListener((BeanListener<T>) beanListener);
            }
            else {
                throw new IllegalArgumentException("Illegal type of BeanListener! expect type:[" + beanClass + "], actual type:[" + listenerGenericType + "]");
            }
            System.out.println(beanListener);
        }
    }

    /**
     * 强制设置{@link BeanListener}
     * @param targetClass  预期类型
     * @param beanListener 监听器对象
     */
    @SuppressWarnings("unchecked")
    public void forceSetBeanListener(Class<?> targetClass, BeanListener<?> beanListener) {
        setBeanListener((BeanListener<T>) beanListener);
    }

    public void setBeanListener(BeanListener<T> beanListener) {
        this.beanListener = beanListener;
    }

    public List<ExcelMappingInfo> getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(List<ExcelMappingInfo> mappingInfo) {
        this.mappingInfo = mappingInfo;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isGetFormulaResult() {
        return getFormulaResult;
    }

    public void setGetFormulaResult(boolean getFormulaResult) {
        this.getFormulaResult = getFormulaResult;
    }

    public void setTypeConvert(BeanTypeConvert typeConvert) {
        this.typeConvert = typeConvert;
    }

    public void setCellIgnoreHandler(CellIgnoreHandler cellIgnoreHandler) {
        this.cellIgnoreHandler = cellIgnoreHandler;
    }

    public void setCellHandler(ExcelCellHandler cellHandler) {
        this.cellHandler = cellHandler;
    }

    public void setEmptyColumnLimit(int emptyColumnLimit) {
        this.emptyColumnLimit = emptyColumnLimit;
    }
}
