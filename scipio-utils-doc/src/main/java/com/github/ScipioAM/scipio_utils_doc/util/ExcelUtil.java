package com.github.ScipioAM.scipio_utils_doc.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;

/**
 * Excel静态工具类
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/8
 */
public class ExcelUtil {

    /** 旧版最大行数 */
    public static final int ROW_MAX_OLD = 65536;
    /** 旧版最大列数 */
    public static final int COLUMN_MAX_OLD = 256;
    /** 新版最大行数 */
    public static final int ROW_MAX_NEW = 1048576;
    /** 新版最大列数 */
    public static final int COLUMN_MAX_NEW = 16384;

    /**
     * 判断是否为旧版Excel(97-07版)
     * @param fileName 要检查的文件名
     * @return true代表是旧版
     */
    public static boolean isOldVersion(String fileName) {
        if(!fileName.contains(".")) {
            throw new IllegalArgumentException("fileName not contains a extension, : " + fileName);
        }
        String[] arr = fileName.split("\\.");
        String extension = arr[arr.length - 1];
        return ("xls".equals(extension));
    }

    /**
     * 判断是否为旧版Excel(97-07版)
     * @param file 要检查的文件
     * @return true代表是旧版
     */
    public static boolean isOldVersion(File file) {
        if(file == null) {
            throw new NullPointerException("file object is null");
        }
        String fileName = file.getName();
        return isOldVersion(fileName);
    }

    /**
     * 创建带式样的行
     * @param sheet 工作表
     * @param styleRowIndex 式样行的行号（从0开始）
     * @param newRowIndex 要新建行的行号（从0开始），如果与式样行行号一样，则直接返回式样行
     * @param startColumn 起始列号（从0开始）
     * @return 被创建的行（带式样）
     */
    public static Row createRowWithStyle(Sheet sheet, int styleRowIndex, int newRowIndex, int startColumn) {
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

        int maxColCnt = styleRow.getLastCellNum();
        Row newRow = sheet.createRow(newRowIndex);
        for (int i = startColumn; i < maxColCnt; i++) {
            Cell cell = newRow.createCell(i);
            if (styleRow.getCell(i) != null) {
                cell.setCellStyle(styleRow.getCell(i).getCellStyle());
            }
        }
        return newRow;
    }

    /**
     * 检查行扫描范围是否合法
     * @param isOldVersion 是否为旧版excel
     * @param rowStartIndex 开始扫描的index
     * @param rowLength 扫描长度
     * @throws IllegalArgumentException 范围非法则抛出此异常
     */
    public static void checkRowMax(boolean isOldVersion, int rowStartIndex, int rowLength) throws IllegalArgumentException {
        if(isOldVersion) {
            if((rowStartIndex + rowLength) > ROW_MAX_OLD) {
                throw new IllegalArgumentException("row scan range(rowLength + rowStartIndex) can not greater then " + ROW_MAX_OLD);
            }
        }
        else {
            if((rowStartIndex + rowLength) > ROW_MAX_NEW) {
                throw new IllegalArgumentException("row scan range(rowLength + rowStartIndex) can not greater then " + ROW_MAX_NEW);
            }
        }
    }

    /**
     * 检查行扫描范围是否合法
     * @param isOldVersion 是否为旧版excel
     * @param columnStartIndex 开始扫描的index
     * @param columnLength 扫描长度
     * @throws IllegalArgumentException 范围非法则抛出此异常
     */
    public static void checkColumnMax(boolean isOldVersion, int columnStartIndex, int columnLength) throws IllegalArgumentException {
        if(isOldVersion) {
            if((columnStartIndex + columnLength) > COLUMN_MAX_OLD) {
                throw new IllegalArgumentException("column scan range(columnLength + columnStartIndex) can not greater then " + COLUMN_MAX_OLD);
            }
        }
        else {
            if((columnStartIndex + columnLength) > COLUMN_MAX_NEW) {
                throw new IllegalArgumentException("column scan range(columnLength + columnStartIndex) can not greater then " + COLUMN_MAX_NEW);
            }
        }
    }

}
