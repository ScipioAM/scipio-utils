package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_doc.csv.bean.CSVRow;
import com.github.ScipioAM.scipio_utils_doc.csv.callback.ColumnHandler;
import com.github.ScipioAM.scipio_utils_doc.csv.callback.RowHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV写入者
 * @author Alan Scipio
 * @since 1.0.9
 * @date 2021/11/11
 */
public class CSVWriter extends BaseCSVOperator{

    /**
     * csv写入
     * @param file 要写入的csv文件（不存在就创建）
     * @param append 是追加写入还是覆盖写入
     * @param rowList 要写入的内容（每行的数据）
     * @throws IOException 写入失败等
     */
    public void write(File file, boolean append, List<CSVRow> rowList) throws IOException {
        if(rowList == null || rowList.size() <= 0) {
            throw new IllegalArgumentException("argument \"rowList\" is null or empty");
        }
        //加载文件
        try(BufferedWriter bufferedWriter = loadForWrite(file,append)) {
            //开始写入
            for(int i = 0; i < rowList.size(); i++) {
                CSVRow row = rowList.get(i);
                StringBuilder line;
                String[] columnArr = row.getColumnArr();
                //用line填写
                if(columnArr == null || columnArr.length == 0) {
                    line = new StringBuilder(row.getLine());
                    if(StringUtil.isNull(line.toString())) {
                        System.err.println("CSVRow`s content is empty, rowIndex[" + i + "]");
                        continue;
                    }
                }
                //用columnArr填写
                else {
                    line = new StringBuilder();
                    for(String column : columnArr) {
                        line.append(column).append(DELIMITER);
                    }
                    line.deleteCharAt(line.length() - 1);//去除末尾多余的分隔符
                }
                //行处理回调
                if(rowHandler != null && !rowHandler.handle(line.toString(),i)) {
                    return;
                }
                //每行数据写入
                bufferedWriter.write(line.toString());
            }//end of for
        }//end of try
    }//end of writeAll()

    /**
     * csv写入（覆盖写入）
     * @param file 要写入的csv文件（不存在就创建）
     * @param rowList 要写入的内容（每行的数据）
     * @throws IOException 写入失败等
     */
    public void write(File file, List<CSVRow> rowList) throws IOException {
        write(file,false,rowList);
    }

    /**
     * csv写入
     * @param file 要写入的csv文件（不存在就创建）
     * @param append 是追加写入还是覆盖写入
     * @param rowList 要写入的内容（每行的字符串）
     * @throws IOException 写入失败等
     */
    public void writeStr(File file, boolean append, List<String> rowList) throws IOException {
        if(rowList == null || rowList.size() <= 0) {
            throw new IllegalArgumentException("argument \"rowList\" is null or empty");
        }
        List<CSVRow> csvRowList = new ArrayList<>();
        for(int i = 0; i < rowList.size(); i++) {
            String line = rowList.get(i);
            CSVRow row = new CSVRow(line,i);
            csvRowList.add(row);
        }
        write(file,append,csvRowList);
    }

    /**
     * csv写入（覆盖写入）
     * @param file 要写入的csv文件（不存在就创建）
     * @param rowList 要写入的内容（每行的字符串）
     * @throws IOException 写入失败等
     */
    public void writeStr(File file, List<String> rowList) throws IOException {
        writeStr(file,false,rowList);
    }

    /**
     * csv写入（覆盖写入）
     * @param file 要写入的csv文件（不存在就创建）
     * @param rows 要写入的内容（每行的字符串）
     * @throws IOException 写入失败等
     */
    public void writeStr(File file, String... rows) throws IOException {
        writeStr(file,false, Arrays.asList(rows));
    }

    //==================================================================================================================

    public CSVWriter setDelimiter(String delimiter) {
        super.DELIMITER = delimiter;
        return this;
    }

    public CSVWriter setRowHandler(RowHandler rowHandler) {
        super.rowHandler = rowHandler;
        return this;
    }

    public CSVWriter setColumnHandler(ColumnHandler columnHandler) {
        System.err.println("CSVWriter do not support ColumnHandler! Nothing happened");
        return this;
    }

}
