package com.github.ScipioAM.scipio_utils_doc.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @date 2021/9/10
 */
public class ExcelBeanReader extends ExcelOperator{

    @Override
    public ExcelBeanReader load(File file) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanReader) super.load(file);
    }

    @Override
    public ExcelBeanReader load(String fileFullPath) throws IOException, InvalidFormatException, NullPointerException {
        return (ExcelBeanReader) super.load(fileFullPath);
    }

    //TODO 待完成，考虑【链式调用的便利性】与【编码便利性】的兼容（不用再继承一遍那些set方法）
//    public List<?> read() {
//
//    }

}
