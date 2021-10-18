package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;
import com.github.ScipioAM.scipio_utils_common.validation.annotation.Nullable;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelCellHandler;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelEndListener;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelRowHandler;
import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/8/27
 */
public abstract class ExcelOperatorBase implements Closeable {

    protected Workbook workbook;
    protected Boolean isOldVersion;

    /** 行处理器(每行) */
    @Nullable
    protected ExcelRowHandler rowHandler;

    /** 列处理器(每个单元格) */
    @Nullable
    protected ExcelCellHandler cellHandler;

    /** 操作结束时的回调 */
    @Nullable
    protected ExcelEndListener endListener;

    /**
     * 加载excel文件
     * @param file 目标文件
     * @return 加载出来的工作簿对象
     * @throws IOException 加载失败
     * @throws InvalidFormatException 加载失败
     * @throws NullPointerException file对象为null
     */
    public ExcelOperatorBase load(@NotNull File file) throws IOException, InvalidFormatException, NullPointerException {
        if(file == null) {
            throw new NullPointerException("argument \"file\" is null");
        }
        else if(file.isDirectory()) {
            throw new IllegalArgumentException("argument \"file\" must be a excel file");
        }
        else if(!file.exists()) {
            throw new FileNotFoundException("file[" + file.getAbsolutePath() + "] does not exists");
        }
        Workbook workbook;
        isOldVersion = ExcelUtil.isOldVersion(file);
        if(isOldVersion) {
            FileInputStream fis = new FileInputStream(file);
            workbook = new HSSFWorkbook(fis);
        }
        else {
            workbook = new XSSFWorkbook(file);
        }
        this.workbook = workbook;
        return this;
    }

    public ExcelOperatorBase load(@NotNull String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return load(new File(fileFullPath));
    }

    @Override
    public void close() throws IOException {
        if(workbook != null) {
            workbook.close();
        }
    }

    //==================================================================================================================

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

}
