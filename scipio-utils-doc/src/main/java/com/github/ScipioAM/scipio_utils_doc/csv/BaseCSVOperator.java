package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_doc.csv.callback.ColumnHandler;
import com.github.ScipioAM.scipio_utils_doc.csv.callback.RowHandler;

import java.io.*;

/**
 * @author Alan Scipio
 * @since 1.0.8
 * @date 2021/11/8
 */
public abstract class BaseCSVOperator {

    /** 要处理的csv文件对象 */
    protected File csvFile;

    /** 默认分隔符：英文逗号 */
    public static final String DEFAULT_DELIMITER = ",";

    /** 分隔符，可修改 */
    protected String DELIMITER = DEFAULT_DELIMITER;

    /** 行处理器 */
    protected RowHandler rowHandler;

    /** 列处理器 */
    protected ColumnHandler columnHandler;

    /**
     * 加载csv文件（为了读取）
     * @param file 文件对象
     * @return 文件读取者
     * @throws FileNotFoundException 文件不存在
     */
    public BufferedReader loadForRead(File file) throws FileNotFoundException {
        //文件参数检查
        checkFile(file,true);
        this.csvFile = file;
        //加载文件
        FileInputStream fis = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(fis));
    }

    /**
     * 加载csv文件（为了读取）
     * @param fullFilePath 文件全路径（路径+文件全名）
     * @return 文件读取者
     * @throws FileNotFoundException 文件不存在
     */
    public BufferedReader loadForRead(String fullFilePath) throws FileNotFoundException {
        return loadForRead(new File(fullFilePath));
    }

    /**
     * 加载csv文件（为了写入，不存在就会创建文件）
     * @param file 文件对象
     * @param append 是否追加，true代表在末尾追加，false代表覆盖重写
     * @return 文件写入者
     * @throws IOException 文件写入者加载时的IO异常
     */
    public BufferedWriter loadForWrite(File file, boolean append) throws IOException {
        //文件参数检查
        checkFile(file,false);
        this.csvFile = file;
        //加载文件
        return new BufferedWriter(new FileWriter(file,append));
    }

    /**
     * 加载csv文件（为了写入，不存在就会创建文件）
     * @param fullFilePath 文件全路径（路径+文件全名）
     * @return 文件写入者
     * @throws FileNotFoundException 文件不存在
     */
    public BufferedWriter loadForWrite(String fullFilePath, boolean append) throws IOException {
        return loadForWrite(new File(fullFilePath),append);
    }

    //==================================================================================================================

    private void checkFile(File file, boolean checkExists) throws NullPointerException, FileNotFoundException {
        if(file == null) {
            throw new NullPointerException("File object is null while load csv file");
        }
        else {
            String[] nameArr = file.getName().split("\\.");
            if(!nameArr[nameArr.length-1].equals("csv")) {
                System.err.println("WARN: file[" + file.getAbsolutePath() + "] might not be a csv file!");
            }
        }

        if(checkExists) {
            if(file.exists()) {
                throw new FileNotFoundException("file[" + file.getAbsolutePath() + "] not exists!");
            }
        }
    }

    //==================================================================================================================

    public String getDelimiter() {
        return DELIMITER;
    }

    public RowHandler getRowHandler() {
        return rowHandler;
    }

    public ColumnHandler getColumnHandler() {
        return columnHandler;
    }

    public File getCsvFile() {
        return csvFile;
    }
}
