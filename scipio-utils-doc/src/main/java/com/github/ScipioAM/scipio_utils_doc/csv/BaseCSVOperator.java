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

    /** 默认分隔符：英文逗号 */
    public static final String DEFAULT_DELIMITER = ",";

    /** 分隔符，可修改 */
    protected String DELIMITER = DEFAULT_DELIMITER;

    /** 行处理器 */
    protected RowHandler rowHandler;

    /** 列处理器 */
    protected ColumnHandler columnHandler;

    /**
     * 加载csv文件
     * @param file 文件对象
     * @return 文件读取者
     * @throws FileNotFoundException 文件不存在
     */
    public BufferedReader loadFile(File file) throws FileNotFoundException {
        //文件参数检查
        if(file == null) {
            throw new NullPointerException("File object is null while load csv file");
        }
        else {
            String[] nameArr = file.getName().split("\\.");
            if(!nameArr[nameArr.length-1].equals("csv")) {
                System.err.println("WARN: file[" + file.getAbsolutePath() + "] might not be a csv file!");
            }
        }
        //加载文件
        FileInputStream fis = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(fis));
    }

    public BufferedReader loadFile(String fileFullPath) throws FileNotFoundException {
        return loadFile(new File(fileFullPath));
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

}
