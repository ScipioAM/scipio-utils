package com.test;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_doc.excel.ExcelBeanReader;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.BeanListener;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelCellHandler;
import com.github.ScipioAM.scipio_utils_doc.excel.ExcelOperator;
import com.test.bean.MPrice;
import com.test.bean.StyleBean;
import com.test.bean.TestBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan Scipio
 * @date 2021/8/27
 */
public class ExcelReadTest {

    @Test
    public void test0() {
        File file = new File("D:\\temp\\test.xlsx");
        List<Cell> list = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("getLastRowNum:"+sheet.getLastRowNum());
            System.out.println("getPhysicalNumberOfRows:"+sheet.getPhysicalNumberOfRows());

            Row row = sheet.getRow(0);
            System.out.println("getLastCellNum:"+row.getLastCellNum());
            System.out.println("getPhysicalNumberOfCells:"+row.getPhysicalNumberOfCells());

            Cell cell = row.getCell(0);
            list.add(cell);

            System.out.println("\n"+list+"\n");

            workbook.close();
            System.out.println("workbook closed\n");

            System.out.println(list);

            Cell cell1 = list.get(0);
            System.out.println(cell1.getCellType());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        File file = new File("D:\\temp\\test.xlsx");
        ExcelCellHandler cellHandler = (cell, rowIndex, columnIndex, rowLength, columnLength) -> {
            System.out.println("[row:" + rowIndex + ",column:" + columnIndex + "] " + cell);
            return true;
        };
        try (ExcelOperator operator = new ExcelOperator()) {
            operator.load(file);
            operator.setSheetIndex(0)
                    .setUseLastNumberOfRows(true)
                    .setUseLastNumberOfCells(true)
                    .setCellHandler(cellHandler)
                    .operate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBeanRead() {
        //设定要读取的文件
        File file = new File("D:\\temp\\test.xlsx");

        //设定映射关系map（或者用注解来确定映射关系）
        List<ExcelMappingInfo> infoList = new ArrayList<>();
        infoList.add(new ExcelMappingInfo(0,0,"id"));
        infoList.add(new ExcelMappingInfo(1,0,"name"));
        infoList.add(new ExcelMappingInfo(2,0,"descCn"));

        try (ExcelBeanReader reader = new ExcelBeanReader()) {
            reader.load(file);
            List<TestBean> beanList = reader.setSheetIndex(0) //指定读取哪个Sheet
                    .setUseLastNumberOfRows(true) //自动获取所有物理存在的行
                    .read(TestBean.class);//读取(映射)
            System.out.println(beanList);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        File file = new File("D:\\temp\\test7.xls");
        try {
            ExcelBeanReader reader = new ExcelBeanReader();
            reader.load(file);
            List<StyleBean> beanList = reader.setSheetIndex(5) //指定读取哪个Sheet
                    .setRowStartIndex(12)
                    .setRowLength(6)
                    .setColumnStartIndex(2)
                    .setColumnLength(1)
                    .read(StyleBean.class);//读取(映射)

            System.out.println("\n" + beanList + "\n");

            for(StyleBean bean : beanList) {
                System.out.println(bean);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        File file = new File("D:\\temp\\dtms-price-test-2021.8.xlsx");
        try {
            //************** 准备解析工具对象 **************
            ExcelBeanReader excelBeanReader = new ExcelBeanReader();
            excelBeanReader.load(file) //加载excel文件
                    .setRowStartIndex(2) //开始行号(0-based)
                    .setRowStep(1) //行间距
                    .setUseLastNumberOfRows(true) //自动识别总行数
                    .setColumnStartIndex(2) //开始列号(0-based)
                    .setColumnStep(1) //列间距
                    .setColumnLength(12) //列长度
                    .setCellIgnoreHandler((cell, cellValue, valueType, targetType) -> {
                        if(cell.getColumnIndex() == 8 && valueType == String.class) { //FOB(美元)列
                            String value = (String) cellValue;
                            return (value.equals("-") || value.equals(""));
                        }
                        return false;
                    });//如何忽略的规则

            //************** 解析补给品 **************
            excelBeanReader.setSheetIndex(0);
            //每个bean的共通字段
            excelBeanReader.setBeanListener((BeanListener<MPrice>) (isReadMode, bean, cell, rowLength, columnLength, beanIndex) -> {
                clearBlankChars(bean);//清除空白字符;
                bean.setRemark0(bean.getCustCode());//将此时还是交易费缩写的custCode也存一份到remark0字段，以方便后面存储过程的转换
                bean.setSaleType(1);//补给品
                bean.setSaleOrderNo("*");
            });
            List<MPrice> supplyList = excelBeanReader.read(MPrice.class);//读取解析

            //总数据list
            List<MPrice> finalDataList = new ArrayList<>(supplyList);

            //************** 解析号口（量产品） **************
            excelBeanReader.setSheetIndex(1);
            //每个bean的共通字段
            excelBeanReader.setBeanListener((BeanListener<MPrice>) (isReadMode, bean, cell, rowLength, columnLength, beanIndex) -> {
                clearBlankChars(bean);//清除空白字符
                bean.setRemark0(bean.getCustCode());//将此时还是交易费缩写的custCode也存一份到remark0字段，以方便后面存储过程的转换
                bean.setSaleType(2);//量产品
                bean.setSaleOrderNo("*");
            });
            List<MPrice> mpList = excelBeanReader.read(MPrice.class);//读取解析
            finalDataList.addAll(mpList);


            for(MPrice price : finalDataList) {
                System.out.println(price);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除空白字符
     */
    private void clearBlankChars(MPrice bean) {
        if(StringUtil.isNotNull(bean.getPartsNo())) {
            String newPartsNo = bean.getPartsNo().replace(" ","");
            bean.setPartsNo(newPartsNo);
        }
        if(StringUtil.isNotNull(bean.getCustCode())) {
            String newCustCode = bean.getCustCode().replace(" ","");
            bean.setCustCode(newCustCode);
        }
    }

}
