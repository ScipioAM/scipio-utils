package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_common.reflect.TypeHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 调用OpenCSV库的读取器
 *
 * @author Alan Scipio
 * @since 2022/2/24
 */
public class OpenCsvBeanReader<T> {

    private final Class<T> beanClass;

    public OpenCsvBeanReader(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @SuppressWarnings("unchecked")
    public OpenCsvBeanReader() {
        beanClass = (Class<T>) TypeHelper.getGenericClass(this);
    }

    /**
     * 根据@CsvBindByPosition去读取
     *
     * @param file      要读取的csv文件
     * @param charset   csv文件的编码
     * @param skipLines 要跳过几行
     * @return 映射结果
     */
    public List<T> readByPosition(File file, Charset charset, int skipLines) throws FileNotFoundException {
        //载入文件
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset);
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(skipLines) //跳过的行数
                .build();
        //按位置映射的策略
        ColumnPositionMappingStrategy<T> ms = new ColumnPositionMappingStrategy<>();
        ms.setType(beanClass);
        //构建读取器
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                .withType(beanClass)
                .withMappingStrategy(ms)
                .build();
        //读取
        return csvToBean.parse();
    }

    public List<T> readByPosition(File file, Charset charset) throws FileNotFoundException {
        return readByPosition(file, charset, 0);
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }
}
