package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_doc.csv.bean.CSVResult;
import com.github.ScipioAM.scipio_utils_doc.csv.bean.CSVRow;
import com.github.ScipioAM.scipio_utils_doc.csv.callback.ColumnHandler;
import com.github.ScipioAM.scipio_utils_doc.csv.callback.RowHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV读取者
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/8
 */
public class CSVReader extends BaseCSVOperator {

    private BufferedReader bufferedReader;
    private Integer globalRowIndex;

    /**
     * 一次性读取整个文件
     * @param file csv文件对象
     * @return 读取结果
     * @throws IOException 文件不存在；读取行时发生IO异常；关闭流时发生异常
     */
    public CSVResult readAll(File file) throws IOException {
        try (BufferedReader reader = (bufferedReader == null ? loadForRead(file) : bufferedReader)) {
            //加载文件
            //准备工作
            int rowIndex = 0;
            String line;
            List<String> lines = new ArrayList<>();
            List<CSVRow> rows = new ArrayList<>();
            CSVResult result = new CSVResult();
            //读取csv内容
            while ((line = reader.readLine()) != null) {
                CSVRow row = handleLine(line, rowIndex);
                if (row == null) {
                    break;
                }
                lines.add(line);
                rows.add(row);
                rowIndex++;
            }//end of while
            result.setOriginalLines(lines);
            result.setRows(rows);
            return result;
        }
    }//end of readAll()

    /**
     * 行读取
     * @param line 每行的字符串
     * @param rowIndex 行下标(0-based)
     * @return 读取结果
     */
    private CSVRow handleLine(String line, int rowIndex) {
        //行处理回调
        if(rowHandler != null && !rowHandler.handle(line,rowIndex)) {
            return null;
        }

        String[] colArr = line.split(DELIMITER);
        CSVRow row = new CSVRow(DELIMITER,line,colArr,rowIndex);

        //列处理
        if(columnHandler != null) {
            for(int i = 0; i < colArr.length; i++) {
                String columnStr = colArr[i];
                if(!columnHandler.handle(columnStr,i,line,rowIndex)) {
                    return null;
                }
            }
        }
        return row;
    }

    /**
     * 读取csv为list
     * @param file csv文件对象
     * @return 每行的内容
     * @throws IOException 文件不存在；读取行时发生IO异常；关闭流时发生异常
     */
    public List<String> readAsList(File file) throws IOException {
        CSVResult result = readAll(file);
        return result.getOriginalLines();
    }

    //==================================================================================================================

    /**
     * 加载文件
     * @param file csv文件对象
     * @return 链式调用
     * @throws FileNotFoundException 文件不存在
     */
    public CSVReader parse(File file) throws FileNotFoundException {
        bufferedReader = loadForRead(file);
        globalRowIndex = 0;
        return this;
    }

    /**
     * 读取下一行
     * @return 下一行的内容
     * @throws IOException 读取行时发生IO异常；关闭流时发生异常
     */
    public CSVRow readNext() throws IOException {
        //检查
        if(bufferedReader == null) {
            throw new NullPointerException("bufferedReader is null, maybe not parse file first ?");
        }
        else if(!bufferedReader.ready()) {
            throw new IllegalStateException("bufferedReader is not ready, maybe reparse the file ?");
        }
        //读取行
        String line = bufferedReader.readLine();
        if(line == null) { //如果读取到末尾
            close();//自动关闭输入流
            return null;
        }
        return handleLine(line,globalRowIndex++);//否则处理该行的数据
    }

    /**
     * 读取下一行（原始字符串内容）
     * @return 下一行的原始字符串内容
     * @throws IOException 读取行时发生IO异常；关闭流时发生异常
     */
    public String readNextLine() throws IOException {
        CSVRow row = readNext();
        return row == null ? null : row.getLine();
    }

    /**
     * 关闭处理
     * @throws IOException reader关闭失败
     */
    private void close() throws IOException {
        globalRowIndex = null;
        bufferedReader.close();
    }

    //==================================================================================================================

    public CSVReader setDelimiter(String delimiter) {
        super.DELIMITER = delimiter;
        return this;
    }

    public CSVReader setRowHandler(RowHandler rowHandler) {
        super.rowHandler = rowHandler;
        return this;
    }

    public CSVReader setColumnHandler(ColumnHandler columnHandler) {
        super.columnHandler = columnHandler;
        return this;
    }
}
