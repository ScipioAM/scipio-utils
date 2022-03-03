package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.util.List;

/**
 * CSV文件工具
 * @author liuzihao
 */
public class CsvUtil {

    /**
     * 解析CSV格式的文件
     *
     * @param filePath     文件路径
     * @param objectClass  解析出的对象的类
     * @param columns      列
     * @param separator    分隔符
     * @param readStartRow 起始行
     * @param charsetName  编码格式
     * @return 解析出的对象 列表
     */
    public static <T> List<T> analyzeCsvFile(String filePath, Class<T> objectClass, String columns, char separator, long readStartRow, String charsetName) {
        List<T> list = null;
        FileInputStream fileInputStream = null;
        BOMInputStream bomInputStream;
        Reader reader = null;
        try {
            String[] columnMapping = columns.split(String.valueOf(separator), -1);
            ColumnPositionMappingStrategy<T> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            columnPositionMappingStrategy.setType(objectClass);
            fileInputStream = new FileInputStream(filePath);
            if (StringUtil.isNotNull(charsetName)) {
                charsetName = "utf-8"; //编码格式 默认utf-8
            }
            //检查文件是否含有BOM头
            bomInputStream = new BOMInputStream(fileInputStream, false,
                    ByteOrderMark.UTF_8,
                    ByteOrderMark.UTF_16BE,
                    ByteOrderMark.UTF_16LE,
                    ByteOrderMark.UTF_32BE,
                    ByteOrderMark.UTF_32LE);
            if (bomInputStream.hasBOM()) {
                charsetName = bomInputStream.getBOMCharsetName();
            }
            reader = new InputStreamReader(bomInputStream, charsetName);
            //reader = new InputStreamReader(fileInputStream);
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withMappingStrategy(columnPositionMappingStrategy);
            csvToBeanBuilder.withSeparator(separator);
            int skipLines = (int) readStartRow;
            csvToBeanBuilder.withSkipLines(skipLines);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            list = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 解析Txt格式的文件，且分隔符为竖线
     *
     * @param filePath     文件路径
     * @param objectClass  解析出的对象的类
     * @param columns      列
     * @param separator    分隔符
     * @param readStartRow 起始行
     * @return 解析出的对象 列表
     */
    public static <T> List<T> analyzeTxtFile(String filePath, Class<T> objectClass, String columns, char separator, long readStartRow) throws IOException {
        List<T> list;
        FileInputStream fileInputStream = null;
        Reader reader = null;
        try {
            String[] columnMapping = columns.split(String.valueOf(separator), -1);
            ColumnPositionMappingStrategy<T> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            columnPositionMappingStrategy.setType(objectClass);
            fileInputStream = new FileInputStream(filePath);

            // 分隔符为竖线时，将文件IO流中竖线替换成逗号
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = buffReader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            String str = buffer.toString().replace(",", ";");
            str = str.replace("|", ",");
            // 是否存在双引号，存在转换为空
            if (str.contains("\"")) {
                str = str.replace("\"", "");
            }
            // 将String转换成Reader
            reader = new StringReader(str);

            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withMappingStrategy(columnPositionMappingStrategy);

            // 分隔符为竖线时，将分隔符竖线替换为逗号
            csvToBeanBuilder.withSeparator(',');
            int skipLines = (int) readStartRow;
            csvToBeanBuilder.withSkipLines(skipLines);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            list = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 解析CSV格式的文件
     *
     * @param filePath     文件路径
     * @param objectClass  解析出的对象的类
     * @param columns      列
     * @param separator    分隔符
     * @param readStartRow 起始行
     * @param charsetName  编码格式
     * @return 解析出的对象 列表
     */
    public static <T> List<T> analyzeCsvFileByQuotationMarks(String filePath, Class<T> objectClass, String columns, char separator, long readStartRow, String charsetName) {
        List<T> list = null;
        FileInputStream fileInputStream = null;
        Reader reader = null;
        try {
            String[] columnMapping = columns.split(String.valueOf(separator), -1);
            ColumnPositionMappingStrategy<T> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            columnPositionMappingStrategy.setType(objectClass);
            fileInputStream = new FileInputStream(filePath);
            // 分隔符为竖线时，将文件IO流中竖线替换成逗号
            @SuppressWarnings("resource")
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = buffReader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            String str = buffer.toString();

            // 将","替换成$
            if (str.contains("\",\"")) {
                str = str.replace("\",\"", "$");
            }
            // 将双引号内的逗号替换成空
            if (str.contains(",")) {
                str = str.replace(",", " ");
            }
            // 将英制单位后英文双引号替换成空
            if (str.contains("\"")) {
                str = str.replace("\"", "");
            }
            // 再将$符号替换成逗号
            if (str.contains("$")) {
                str = str.replace("$", ",");
            }

            // 将String转换成Reader
            reader = new StringReader(str);

            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withMappingStrategy(columnPositionMappingStrategy);
            csvToBeanBuilder.withSeparator(separator);
            int skipLines = (int) readStartRow;
            csvToBeanBuilder.withSkipLines(skipLines);
            CsvToBean<T> csvToBean = csvToBeanBuilder.build();
            list = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
