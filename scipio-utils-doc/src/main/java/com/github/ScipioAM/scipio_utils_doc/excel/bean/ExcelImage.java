package com.github.ScipioAM.scipio_utils_doc.excel.bean;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;

/**
 * Excel图片相关参数
 *
 * @author Alan Scipio
 * @since 2022/1/24
 */
public class ExcelImage {

    public static ExcelImage build() {
        return new ExcelImage();
    }

    /** 要添加的图片文件 */
    private File imageFile;
    /** 工作簿对象 */
    private Workbook workbook;
    /** 工作表对象，为null则取sheetName */
    private Sheet sheet;
    /** 工作表下标（0-based），为null则取sheetIndex */
    private String sheetName;
    /** 工作表下标（0-based），为null则取0 */
    private Integer sheetIndex;

    /**
     * 图片左上顶点单元格内的x坐标（0-1023）
     */
    private Integer dx1;
    /**
     * 图片左上顶点单元格内的y坐标（0-255）
     */
    private Integer dy1;

    /**
     * 图片右下顶点单元格内的x坐标（0-1023）
     */
    private Integer dx2;
    /**
     * 图片右下顶点单元格内的y坐标（0-255）
     */
    private Integer dy2;

    /**
     * 左上顶点单元格的列下标（0-based）
     */
    private Integer col1;
    /**
     * 左上顶点单元格的行下标（0-based）
     */
    private Integer row1;

    /**
     * 右下顶点单元格的列下标（0-based）
     */
    private Integer col2;
    /**
     * 右下顶点单元格的行下标（0-based）
     */
    private Integer row2;

    /**
     * 设置铺满左上顶点单元格
     */
    public ExcelImage setAnchorStartFill() {
        dx1 = 0;
        dy1 = 0;
        return this;
    }

    /**
     * 设置铺满右下顶点单元格
     */
    public ExcelImage setAnchorEndFill() {
        dx2 = 1023;
        dy2 = 255;
        return this;
    }

    /**
     * 设置铺满左上和右下顶点单元格
     */
    public ExcelImage setAnchorAllFill() {
        return setAnchorStartFill().setAnchorEndFill();
    }

    /**
     * 设置铺满左上顶点单元格
     */
    public ExcelImage setAnchorColRow(int col1, int row1, int col2, int row2) {
        this.col1 = col1;
        this.row1 = row1;
        this.col2 = col2;
        this.row2 = row2;
        return this;
    }

    public Sheet getSheet() {
        if(workbook == null) {
            throw new IllegalArgumentException("workbook can not be null! please set a valid workbook object.");
        }
        if (sheet != null) {
            return sheet;
        } else if (sheetName != null && !"".equals(sheetName)) {
            return workbook.getSheet(sheetName);
        } else if (sheetIndex != null && sheetIndex >= 0) {
            return workbook.getSheetAt(sheetIndex);
        } else {
            return workbook.getSheetAt(0);
        }
    }

    public File getImageFile() {
        return imageFile;
    }

    public ExcelImage setImageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public ExcelImage setWorkbook(Workbook workbook) {
        this.workbook = workbook;
        return this;
    }

    public ExcelImage setSheet(Sheet sheet) {
        this.sheet = sheet;
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelImage setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public ExcelImage setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    public Integer getDx1() {
        return dx1;
    }

    public ExcelImage setDx1(Integer dx1) {
        this.dx1 = dx1;
        return this;
    }

    public Integer getDy1() {
        return dy1;
    }

    public ExcelImage setDy1(Integer dy1) {
        this.dy1 = dy1;
        return this;
    }

    public Integer getDx2() {
        return dx2;
    }

    public ExcelImage setDx2(Integer dx2) {
        this.dx2 = dx2;
        return this;
    }

    public Integer getDy2() {
        return dy2;
    }

    public ExcelImage setDy2(Integer dy2) {
        this.dy2 = dy2;
        return this;
    }

    public Integer getCol1() {
        return col1;
    }

    public ExcelImage setCol1(Integer col1) {
        this.col1 = col1;
        return this;
    }

    public Integer getRow1() {
        return row1;
    }

    public ExcelImage setRow1(Integer row1) {
        this.row1 = row1;
        return this;
    }

    public Integer getCol2() {
        return col2;
    }

    public ExcelImage setCol2(Integer col2) {
        this.col2 = col2;
        return this;
    }

    public Integer getRow2() {
        return row2;
    }

    public ExcelImage setRow2(Integer row2) {
        this.row2 = row2;
        return this;
    }
}
