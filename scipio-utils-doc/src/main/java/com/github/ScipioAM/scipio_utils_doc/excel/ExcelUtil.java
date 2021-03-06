package com.github.ScipioAM.scipio_utils_doc.excel;

import com.github.ScipioAM.scipio_utils_doc.excel.bean.ExcelImage;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Excel静态工具类
 *
 * @author Alan Scipio
 * @date 2021/9/8
 * @since 1.0.2-p3
 */
public class ExcelUtil {

    /**
     * 旧版最大行数
     */
    public static final int ROW_MAX_OLD = 65536;
    /**
     * 旧版最大列数
     */
    public static final int COLUMN_MAX_OLD = 256;
    /**
     * 新版最大行数
     */
    public static final int ROW_MAX_NEW = 1048576;
    /**
     * 新版最大列数
     */
    public static final int COLUMN_MAX_NEW = 16384;

    /**
     * 判断是否为旧版Excel(97-07版)
     *
     * @param fileName 要检查的文件名
     * @return true代表是旧版
     */
    public static boolean isOldVersion(String fileName) {
        if (!fileName.contains(".")) {
            throw new IllegalArgumentException("fileName not contains a extension, : " + fileName);
        }
        String[] arr = fileName.split("\\.");
        String extension = arr[arr.length - 1];
        return ("xls".equals(extension));
    }

    /**
     * 判断是否为旧版Excel(97-07版)
     *
     * @param file 要检查的文件
     * @return true代表是旧版
     */
    public static boolean isOldVersion(File file) {
        String fileName = file.getName();
        return isOldVersion(fileName);
    }

    /**
     * copy带式样的行
     *
     * @param sheet         工作表
     * @param styleRowIndex 式样行的行号（从0开始）
     * @param newRowIndex   要新建行的行号（从0开始），如果与式样行行号一样，则直接返回式样行
     * @param startColumn   起始列号（从0开始）
     * @return 被copy并新创建的行
     */
    public static Row copyRowWithStyle(Sheet sheet, int styleRowIndex, int newRowIndex, int startColumn) {
        //获取式样行
        Row styleRow = sheet.getRow(styleRowIndex);
        //如果获取不到，就在第一行创建（一个空白式样行）
        if (styleRow == null) {
            Row firstRow = sheet.getRow(0);
            styleRow = sheet.createRow(styleRowIndex);
            for (int i = startColumn; i < firstRow.getLastCellNum(); i++) {
                styleRow.createCell(i);
            }
        }

        if (styleRowIndex == newRowIndex) {
            return styleRow;
        }

        CellStyle rowStyle = styleRow.getRowStyle();
        short styleRowHeight = styleRow.getHeight();

        int maxColCnt = styleRow.getLastCellNum();
        Row newRow = sheet.createRow(newRowIndex);
        newRow.setRowStyle(rowStyle);
        newRow.setHeight(styleRowHeight);
        for (int i = startColumn; i < maxColCnt; i++) {
            Cell newCell = newRow.createCell(i);
            Cell styleCell = styleRow.getCell(i);
            if (styleCell != null) {
                newCell.setCellStyle(styleCell.getCellStyle());
            }
        }
        return newRow;
    }

    /**
     * 检查行扫描范围是否合法
     *
     * @param isOldVersion  是否为旧版excel
     * @param rowStartIndex 开始扫描的index
     * @param rowLength     扫描长度
     * @throws IllegalArgumentException 范围非法则抛出此异常
     */
    public static void checkRowMax(boolean isOldVersion, int rowStartIndex, int rowLength) throws IllegalArgumentException {
        if (isOldVersion) {
            if ((rowStartIndex + rowLength) > ROW_MAX_OLD) {
                throw new IllegalArgumentException("row scan range(rowLength + rowStartIndex) can not greater then " + ROW_MAX_OLD);
            }
        } else {
            if ((rowStartIndex + rowLength) > ROW_MAX_NEW) {
                throw new IllegalArgumentException("row scan range(rowLength + rowStartIndex) can not greater then " + ROW_MAX_NEW);
            }
        }
    }

    /**
     * 检查行扫描范围是否合法
     *
     * @param isOldVersion     是否为旧版excel
     * @param columnStartIndex 开始扫描的index
     * @param columnLength     扫描长度
     * @throws IllegalArgumentException 范围非法则抛出此异常
     */
    public static void checkColumnMax(boolean isOldVersion, int columnStartIndex, int columnLength) throws IllegalArgumentException {
        if (isOldVersion) {
            if ((columnStartIndex + columnLength) > COLUMN_MAX_OLD) {
                throw new IllegalArgumentException("column scan range(columnLength + columnStartIndex) can not greater then " + COLUMN_MAX_OLD);
            }
        } else {
            if ((columnStartIndex + columnLength) > COLUMN_MAX_NEW) {
                throw new IllegalArgumentException("column scan range(columnLength + columnStartIndex) can not greater then " + COLUMN_MAX_NEW);
            }
        }
    }

    /**
     * 简单获取单元格的值
     *
     * @param cell             单元格对象
     * @param getFormulaResult 对于公式单元格，是获取公式计算的值，还是公式本身。
     *                         (为true代表获取公式计算的值)
     * @return 单元格的值
     */
    public static Object getCellValue(Cell cell, boolean getFormulaResult) {
        Object value = null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING: // 字符串类型的单元格
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: // boolean类型的单元格
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC: // 数字(包括分数)、日期类型的单元格
                //日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getLocalDateTimeCellValue();
                }
                //数字
                else {
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: // 公式类型的单元格
                //获取公式计算的值
                if (getFormulaResult) {
                    try {
                        value = cell.getNumericCellValue();
                    } catch (IllegalStateException e) {
                        value = cell.getStringCellValue();
                    }
                }
                //获取公式本身
                else {
                    value = cell.getCellFormula();
                }
                break;
            case BLANK: // 空值类型的单元格
                System.out.println("Cell type is blank, sheetName[" + cell.getSheet().getSheetName() + "], rowIndex[" + cell.getRowIndex() + "], columnIndex[" + cell.getColumnIndex() + "]");
                break;
            case ERROR: // 故障类型的单元格
                System.err.println("Cell type is error! sheetName[" + cell.getSheet().getSheetName() + "], rowIndex[" + cell.getRowIndex() + "], columnIndex[" + cell.getColumnIndex() + "]");
                break;
            default:
                System.err.println("Unknown cell type " + cellType + ", sheetName[" + cell.getSheet().getSheetName() + "], rowIndex[" + cell.getRowIndex() + "], columnIndex[" + cell.getColumnIndex() + "]");
                break;
        }
        return value;
    }

    /**
     * 设置单元格四周的边框
     *
     * @param cellStyle   样式对象，可以通过{@link Workbook#createCellStyle()}或{@link Cell#getCellStyle()}获得
     * @param borderStyle 边框样式（实线、虚线、加粗等）
     * @param color       边框颜色
     */
    public static void setBorderAround(CellStyle cellStyle, BorderStyle borderStyle, IndexedColors color) {
        //设置边框
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        //设置边框颜色
        cellStyle.setBottomBorderColor(color.getIndex());
        cellStyle.setTopBorderColor(color.getIndex());
        cellStyle.setLeftBorderColor(color.getIndex());
        cellStyle.setRightBorderColor(color.getIndex());
    }

    /**
     * 设置单元格四周的边框，固定黑色单实线边框（最常规的那种样式）
     */
    public static void setBorderAround(CellStyle cellStyle) {
        setBorderAround(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
    }

    /**
     * 完全移除某行
     *
     * @param sheet    工作表对象
     * @param rowIndex 要移除的行
     */
    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowIndex = sheet.getLastRowNum();//最后一行
        if (rowIndex < 0) {
            throw new IllegalArgumentException("removeRowIndex[" + rowIndex + "] can not less then 0");
        } else if (rowIndex > lastRowIndex) {
            throw new IllegalArgumentException("removeRowIndex[" + rowIndex + "] can not greater then lastRowNum[" + lastRowIndex + "]");
        } else {
            //清空数据和式样
            sheet.removeRow(sheet.getRow(rowIndex));
            //移除这行(下面行向上移1位)
            if (rowIndex < lastRowIndex) {
                sheet.shiftRows(rowIndex + 1, lastRowIndex, -1);
            }
        }
    }

    /**
     * 插入并复制式样行
     *
     * @param sheet            工作表对象
     * @param rowIndex         当前行号（在此行开始复制插入）（0-based）
     * @param styleRowIndex    式样行的行号（0-based）
     * @param columnStartIndex 起始列号（0-based）
     * @return 新复制的行
     */
    public static Row insertAndCopyRow(Sheet sheet, int rowIndex, int styleRowIndex, int columnStartIndex) {
        //行下移
        sheet.shiftRows(rowIndex, sheet.getLastRowNum(), 1, true, false);
        //行复制
        return ExcelUtil.copyRowWithStyle(sheet, styleRowIndex, rowIndex, columnStartIndex);
    }

    /**
     * 插入并复制式样行 - 从第0列开始复制
     *
     * @param sheet         工作表对象
     * @param rowIndex      当前行号（在此行开始复制插入）（0-based）
     * @param styleRowIndex 式样行的行号（0-based）
     * @return 新复制的行
     */
    public static Row insertAndCopyRow(Sheet sheet, int rowIndex, int styleRowIndex) {
        //行下移
        sheet.shiftRows(rowIndex, sheet.getLastRowNum(), 1, true, false);
        //行复制
        return ExcelUtil.copyRowWithStyle(sheet, styleRowIndex, rowIndex, 0);
    }

    /**
     * Excel添加图片
     *
     * @param arg 相关参数
     */
    public static void addImage(ExcelImage arg) throws IOException {
        Sheet sheet = arg.getSheet();
        boolean oldVersion = (sheet instanceof HSSFSheet);
        //创建位置坐标
        ClientAnchor anchor = (oldVersion ? new HSSFClientAnchor() : new XSSFClientAnchor());
        anchor.setDx1(arg.getDx1());
        anchor.setDy1(arg.getDy1());
        anchor.setDx2(arg.getDx2());
        anchor.setDy2(arg.getDy2());
        anchor.setCol1(arg.getCol1());
        anchor.setRow1(arg.getRow1());
        anchor.setCol2(arg.getCol2());
        anchor.setRow2(arg.getRow2());
        //读取图片文件
        String fileName = arg.getImageFile().getName();
        String[] nameArr = fileName.split("\\.");
        if (nameArr.length == 1) {
            throw new IOException("can not get suffix from file name: [" + fileName + "]");
        }
        String fileSuffix = nameArr[nameArr.length - 1];
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(arg.getImageFile());
        ImageIO.write(bufferedImage, fileSuffix, imageBytes);
        //确定图片类型
        int picType;
        switch (fileSuffix) {
            case "emf":
                picType = Workbook.PICTURE_TYPE_EMF;
                break;
            case "wmf":
                picType = Workbook.PICTURE_TYPE_WMF;
                break;
            case "pict":
                picType = Workbook.PICTURE_TYPE_PICT;
                break;
            case "jpg":
            case "jpeg":
                picType = Workbook.PICTURE_TYPE_JPEG;
                break;
            case "png":
                picType = Workbook.PICTURE_TYPE_PNG;
                break;
            case "dib":
                picType = Workbook.PICTURE_TYPE_DIB;
                break;
            default:
                throw new IOException("unsupported picture type: [" + fileSuffix + "]");
        }
        //加入到工作簿中，获得图片序号
        int pictureIndex = arg.getWorkbook().addPicture(imageBytes.toByteArray(), picType);
        //插入图片
        if (oldVersion) {
            //97-03版
            HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor, pictureIndex);
        } else {
            //07及以上的新版
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            drawing.createPicture(anchor, pictureIndex);
        }
    }

    /**
     * 获取或创建单元格对象
     *
     * @param row     行对象
     * @param cellNum 列号(0-based)
     */
    public static Cell getOrCreate(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            cell = row.createCell(cellNum);
        }
        return cell;
    }

    /**
     * 合并单元格
     *
     * @param sheet 工作表对象
     * @return 合并区域对象
     */
    public static CellRangeAddress combineCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(region);
        return region;
    }

    /**
     * 设置单元格背景色
     *
     * @param workbook   工作簿对象（用于创建式单元格样对象）
     * @param cell       单元格对象
     * @param colorIndex 颜色index值，可以用预设的{@link IndexedColors}
     */
    public static void setCellColor(Workbook workbook, Cell cell, short colorIndex) {
        CellStyleBuilder.builder(workbook, cell)
                .keepOriginalStyle()
                .foregroundColor(colorIndex)
                .build();
    }

}
