package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.*;
import com.github.ScipioAM.scipio_utils_doc.excel.convert.BeanTypeConvert;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * excel -> JavaBean 读取器
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/10
 */
public class ExcelBeanReader extends ExcelBeanOperator{

    /** 自定义excel -> JavaBean类型转换器 */
    private BeanTypeConvert customTypeConvert;

    /**
     * 对于公式单元格，是获取公式计算的值，还是公式本身。
     *      (为true代表获取公式计算的值)
     */
    private boolean getFormulaResult = true;

    /** 垂直读取标志 */
    private boolean verticalRead = false;

    /**
     * 连续多少次空行后，认为读取结束了（0则永远不会这样强行结束）
     * （仅限常规的水平读取）
     */
    private int emptyRowLimit = 3;

    /** 单元格忽略处理器 */
    private CellIgnoreHandler cellIgnoreHandler;

    //流式读取专用
    /**  */
    private Integer currentBeanIndex;
    /**  */
    private AutoExcelBeanMapper<?> autoExcelBeanMapper;
    /**  */
    private Class<?> beanClass;

    @Override
    public ExcelBeanReader load(File file, String password) throws IOException, NullPointerException {
        return (ExcelBeanReader) super.load(file, password);
    }

    @Override
    public ExcelBeanReader load(File file) throws IOException, NullPointerException {
        return (ExcelBeanReader) super.load(file);
    }

    @Override
    public ExcelBeanReader load(String fileFullPath, String password) throws IOException, NullPointerException {
        return (ExcelBeanReader) super.load(fileFullPath, password);
    }

    @Override
    public ExcelBeanReader load(String fileFullPath) throws IOException, NullPointerException {
        return (ExcelBeanReader) super.load(fileFullPath);
    }

    /**
     * 读取excel并映射为JavaBean
     * @param beanMapper 自定义转换器
     * @param <T> JavaBean的类型
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull Class<T> beanClass, @NotNull ExcelBeanMapper<T> beanMapper) throws Exception {
        try {
            //操作前准备(参数检查、确认扫描总行数等)
            //只有垂直读取时才需要检查列长度
            OpPrepareVo prepareVo = operationPrepare(beanMapper,false,beanClass,null,verticalRead);
            Sheet sheet = prepareVo.sheet;
            Integer rowLength = prepareVo.rowLength;
            Integer rowStartIndex = excelIndex.getRowStartIndex();
            //如果是垂直读取，则准备好垂直读取的mapper
            VerticalExcelBeanMapper<T> verticalBeanMapper = null;
            if(verticalRead && (beanMapper instanceof VerticalExcelBeanMapper)) {
                verticalBeanMapper = (VerticalExcelBeanMapper<T>) beanMapper;
                verticalBeanMapper.prepareMappingInfo();
            }

            List<T> beanList = buildBeanList();

            //开始的监听回调
            if(startListener != null && !startListener.firstOperation(workbook,sheet,excelIndex)) {
                return beanList;
            }

            // 开始扫描行
            int beanIndex = 0;
            int nullRowCount = 0;
            for(int i = rowStartIndex; i < rowLength; i += excelIndex.getRowStep()) {
                //不在白名单中的行要跳过
                if(rowWhitelist.size() > 0 && !rowWhitelist.contains(i)) {
                    continue;
                }
                Row row = sheet.getRow(i);

                //行处理监听器
                if(rowHandler != null && !rowHandler.handle(row,i,rowLength)) {
                    break;
                }

                //正常水平读取
                if(verticalBeanMapper == null) {
                    T bean = beanMapper.mappingExcel2Bean(row, i, rowLength,beanIndex);
                    if(bean != null) {
                        if(emptyRowLimit > 0 && emptyRowLimit == nullRowCount) { //达到空行上限，强行结束读取
                            System.out.println("Reach the emptyRowLimit[" + emptyRowLimit + "], finish reading");
                            break;
                        }
                        beanList.add(bean);
                        beanIndex++;
                        nullRowCount++;
                    }
                }
                //垂直读取
                else {
                    //确定每列总行数(加上了起始列号)
                    Integer columnLength = determineColumnLength(excelIndex,row);
                    verticalBeanMapper.mappingExcel2Bean(row,i,rowLength,excelIndex.getColumnStartIndex(),columnLength,beanList);
                }
            }//end of for
            //收尾
            finish();
            return beanList;
        }catch (Exception e) {
            if(exceptionHandler != null) {
                exceptionHandler.handle(workbook,excelIndex,e);
            }
            else {
                throw e;
            }
            return null;
        }
    }

    /**
     * 读取excel并映射为JavaBean - 根据定义的映射信息list
     * @param mappingInfo 映射信息
     * @param <T> JavaBean的类型
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull List<ExcelMappingInfo> mappingInfo, @NotNull Class<T> beanClass) throws Exception {
        if(mappingInfo == null || mappingInfo.size() <= 0) {
            throw new IllegalArgumentException("argument \"mappingInfo\" can not be null or empty");
        }
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        BaseExcelBeanMapper<T> beanAutoMapper = verticalRead ? new VerticalAutoExcelBeanMapper<>(mappingInfo,beanClass) : new AutoExcelBeanMapper<>(mappingInfo,beanClass);
        buildBeanAutoMapper(beanAutoMapper,beanClass);
        return read(beanClass,beanAutoMapper);
    }

    /**
     * 读取excel并映射为JavaBean - 根据注解映射
     * @param <T> JavaBean的类型，要映射的字段上需要添加{@link ExcelMapping}注解
     * @return 映射后的JavaBean list
     */
    public <T> List<T> read(@NotNull Class<T> beanClass) throws Exception {
        if(beanClass == null ) {
            throw new NullPointerException("argument \"beanClass\" is null");
        }
        BaseExcelBeanMapper<T> beanAutoMapper = verticalRead ? new VerticalAutoExcelBeanMapper<>(beanClass) : new AutoExcelBeanMapper<>(beanClass);
        buildBeanAutoMapper(beanAutoMapper,beanClass);
        return read(beanClass,beanAutoMapper);
    }

    //==================================================================================================================

    /**
     * 流式读取bean
     *  <p>注意：读取完后要记得调用{@link #close()}或{@link #saveAndClose()}关闭</p>
     * @param <T> JavaBean的类型
     * @return 当前读取到的bean
     */
    @SuppressWarnings("unchecked")
    public <T> T readNext() {
        //TODO 类型是否一致的问题，检查还是不检查？是否有其他奇怪情况？
        Row row = readNextRow();
        if(autoExcelBeanMapper == null) { //第一次读取，初始化mapper
            isNeedReset = false;
            currentBeanIndex = 0;
            autoExcelBeanMapper = new AutoExcelBeanMapper<>(beanClass);
            buildBeanAutoMapper(autoExcelBeanMapper,beanClass);
        }
        T bean = (T) autoExcelBeanMapper.mappingExcel2Bean(row, currentRowIndex, lastRowIndex, (currentBeanIndex++));
        if(bean == null) {
            resetForStreamRead(true);
        }
        return bean;
    }

    @Override
    protected void prepareForStreamRead() {
        //准备ExcelIndex
        prepareExcelIndex(beanClass,null);
        super.prepareForStreamRead();

    }
    @Override
    protected void resetForStreamRead(boolean isNeedReset) {
        if(isNeedReset) {
            firstCheck = true;
            createSheetIfNotExists = true;
            currentSheet = null;
            currentRowIndex = null;
            lastRowIndex = null;
            autoExcelBeanMapper = null;
            beanClass = null;
        }
    }

    public ExcelBeanReader setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    //==================================================================================================================

    private void buildBeanAutoMapper(BaseExcelBeanMapper<?> beanAutoMapper, Class<?> beanClass) {
        if(customTypeConvert != null) {
            beanAutoMapper.setTypeConvert(customTypeConvert);
        }
        beanAutoMapper.setGetFormulaResult(getFormulaResult);
        beanAutoMapper.setCellIgnoreHandler(cellIgnoreHandler);
        beanAutoMapper.setCellHandler(cellHandler);
        if(super.isForceSetBeanListener) {
            beanAutoMapper.forceSetBeanListener(beanClass,beanListener);
        }
        else {
            beanAutoMapper.checkAndSetBeanListener(beanClass,beanListener);
        }
    }

    /**
     * 构建空的beanList
     *      <p>子类重写此方法以定义自己的List类型</p>
     * @param <T> bean类型
     * @return 空的beanList实例
     */
    protected <T> List<T> buildBeanList() {
        return new ArrayList<>();
    }

    //==================================================================================================================

    public ExcelBeanReader setCustomTypeConvert(BeanTypeConvert customTypeConvert) {
        this.customTypeConvert = customTypeConvert;
        return this;
    }

    public boolean isGetFormulaResult() {
        return getFormulaResult;
    }

    public ExcelBeanReader setGetFormulaResult(Boolean getFormulaResult) {
        this.getFormulaResult = getFormulaResult;
        return this;
    }

    public boolean isVerticalRead() {
        return verticalRead;
    }

    public ExcelBeanReader setVerticalRead(boolean verticalRead) {
        this.verticalRead = verticalRead;
        return this;
    }

    public int getEmptyRowLimit() {
        return emptyRowLimit;
    }

    public ExcelBeanReader setEmptyRowLimit(int emptyRowLimit) {
        this.emptyRowLimit = emptyRowLimit;
        return this;
    }

    public ExcelBeanReader setCellIgnoreHandler(CellIgnoreHandler cellIgnoreHandler) {
        this.cellIgnoreHandler = cellIgnoreHandler;
        return this;
    }

    public ExcelBeanReader setBeanListener(BeanListener<?> beanListener) {
        super.beanListener = beanListener;
        return this;
    }

    public boolean isForceSetBeanListener() {
        return super.isForceSetBeanListener;
    }

    public ExcelBeanReader setForceSetBeanListener(boolean forceSetBeanListener) {
        super.isForceSetBeanListener = forceSetBeanListener;
        return this;
    }

    @Override
    public ExcelBeanReader setRowHandler(ExcelRowHandler rowHandler) {
        return (ExcelBeanReader) super.setRowHandler(rowHandler);
    }

    @Override
    public ExcelBeanReader setCellHandler(ExcelCellHandler cellHandler) {
        return (ExcelBeanReader) super.setCellHandler(cellHandler);
    }

    @Override
    public ExcelBeanReader setEndListener(ExcelEndListener endListener) {
        return (ExcelBeanReader) super.setEndListener(endListener);
    }

    @Override
    public ExcelBeanReader setStartListener(ExcelStartListener startListener) {
        return (ExcelBeanReader) super.setStartListener(startListener);
    }

    @Override
    public ExcelBeanReader setExceptionHandler(ExceptionHandler exceptionHandler) {
        return (ExcelBeanReader) super.setExceptionHandler(exceptionHandler);
    }

    @Override
    public ExcelBeanReader setExcelIndex(ExcelIndex excelIndex) {
        return (ExcelBeanReader) super.setExcelIndex(excelIndex);
    }

    @Override
    public ExcelBeanReader setSheetIndex(Integer sheetIndex) {
        return (ExcelBeanReader) super.setSheetIndex(sheetIndex);
    }

    @Override
    public ExcelBeanReader setSheetName(String sheetName) {
        return (ExcelBeanReader) super.setSheetName(sheetName);
    }

    @Override
    public ExcelBeanReader setRowStartIndex(Integer rowStartIndex) {
        return (ExcelBeanReader) super.setRowStartIndex(rowStartIndex);
    }

    @Override
    public ExcelBeanReader setRowLength(Integer rowLength) {
        return (ExcelBeanReader) super.setRowLength(rowLength);
    }

    @Override
    public ExcelBeanReader setRowStep(Integer rowStep) {
        return (ExcelBeanReader) super.setRowStep(rowStep);
    }

    @Override
    public ExcelBeanReader setColumnStartIndex(Integer columnStartIndex) {
        return (ExcelBeanReader) super.setColumnStartIndex(columnStartIndex);
    }

    @Override
    public ExcelBeanReader setColumnLength(Integer columnLength) {
        return (ExcelBeanReader) super.setColumnLength(columnLength);
    }

    @Override
    public ExcelBeanReader setColumnStep(Integer columnStep) {
        return (ExcelBeanReader) super.setColumnStep(columnStep);
    }

    @Override
    public ExcelBeanReader setUsePhysicalNumberOfRows(boolean usePhysicalNumberOfRows) {
        return (ExcelBeanReader) super.setUsePhysicalNumberOfRows(usePhysicalNumberOfRows);
    }

    @Override
    public ExcelBeanReader setUsePhysicalNumberOfCells(boolean usePhysicalNumberOfCells) {
        return (ExcelBeanReader) super.setUsePhysicalNumberOfCells(usePhysicalNumberOfCells);
    }

    @Override
    public ExcelBeanReader setUseLastNumberOfRows(boolean useLastNumberOfRows) {
        return (ExcelBeanReader) super.setUseLastNumberOfRows(useLastNumberOfRows);
    }

    @Override
    public ExcelBeanReader setUseLastNumberOfCells(boolean useLastNumberOfCells) {
        return (ExcelBeanReader) super.setUseLastNumberOfCells(useLastNumberOfCells);
    }
}
