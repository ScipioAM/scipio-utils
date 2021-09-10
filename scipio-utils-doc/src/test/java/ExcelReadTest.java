import com.github.ScipioAM.scipio_utils_doc.excel.listener.ExcelCellHandler;
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
        ExcelCellHandler cellHandler = (row, cell, rowIndex, columnIndex) -> {
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

}
