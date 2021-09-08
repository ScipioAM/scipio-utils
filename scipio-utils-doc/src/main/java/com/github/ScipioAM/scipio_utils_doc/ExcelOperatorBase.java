package com.github.ScipioAM.scipio_utils_doc;

import com.github.ScipioAM.scipio_utils_doc.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/8/27
 */
public abstract class ExcelOperatorBase implements Closeable {

    protected Workbook workbook;
    protected Boolean isOldVersion;

    /**
     * 加载excel文件
     * @param file 目标文件
     * @return 加载出来的工作簿对象
     * @throws IOException 加载失败
     * @throws InvalidFormatException 加载失败
     * @throws NullPointerException file对象为null
     */
    public Workbook load(File file) throws IOException, InvalidFormatException, NullPointerException {
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
        return workbook;
    }

    public Workbook load(String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return load(new File(fileFullPath));
    }

    @Override
    public void close() throws IOException {
        if(workbook != null) {
            workbook.close();
        }
    }

}
