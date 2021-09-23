package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelBeanWriter;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021/9/18
 */
public class ExcelWriteTest {

    @Test
    public void beanWrite() {

        //设定要读取的文件
        File file = new File("D:\\temp\\test2.xlsx");

        //设定映射关系map（或者用注解来确定映射关系）
        List<ExcelMappingInfo> infoList = new ArrayList<>();
        infoList.add(new ExcelMappingInfo(0,"id"));
        infoList.add(new ExcelMappingInfo(1,"name"));
        infoList.add(new ExcelMappingInfo(2,"descCn"));

        //设定要写入数据的beanList
        List<TestBean> beanList = new ArrayList<>();
        beanList.add(new TestBean(101,"杜立特","aa"));
        beanList.add(new TestBean(102,"李梅","bbb"));
        beanList.add(new TestBean(103,"张三",null));
        beanList.add(new TestBean(104,"赵四","easy"));

        try (ExcelBeanWriter writer = new ExcelBeanWriter()) {
            writer.loadOrCreate(file)
//                    .setSheetIndex(0) //设定要写入哪个sheet(不设默认从第一个sheet开始)
//                    .setRowStartIndex(0) //设定起始行号(不设默认从第一行开始写)
//                    .setRowLength(2) //设定行长度(不设默认为beanList.size())
                    .setNeedCreate(true)
                    .write(beanList,infoList,TestBean.class);
            System.out.println(beanList);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
