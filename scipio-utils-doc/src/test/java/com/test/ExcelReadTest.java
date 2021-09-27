package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.ExcelBeanReader;
import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelMappingInfo;
import com.github.ScipioAM.scipio_utils_doc.excel.callback.ExcelCellHandler;
import com.github.ScipioAM.scipio_utils_doc.excel.ExcelOperator;
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

}
