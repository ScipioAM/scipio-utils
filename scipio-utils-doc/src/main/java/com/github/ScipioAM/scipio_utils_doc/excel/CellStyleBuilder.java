package com.github.ScipioAM.scipio_utils_doc.excel;

import org.apache.poi.ss.usermodel.*;

/**
 * 单元格式样构造器
 *
 * @since 2022/5/7
 */
public class CellStyleBuilder {

    private final CellStyle style;

    private DataFormat dataFormat;

    private Cell targetCell;

    public CellStyleBuilder(Workbook workbook) {
        style = workbook.createCellStyle();
        dataFormat = workbook.createDataFormat();
    }

    public static CellStyleBuilder builder(Workbook workbook) {
        return new CellStyleBuilder(workbook);
    }

    public static CellStyleBuilder builder(Workbook workbook, Cell cell) {
        CellStyleBuilder builder = new CellStyleBuilder(workbook);
        builder.setTargetCell(cell);
        return builder;
    }

    public CellStyle build() {
        if (targetCell != null) {
            targetCell.setCellStyle(style);
        }
        return this.style;
    }

    /**
     * 应用单元格式样（保持原式样的基础上）
     *
     * @param cell 目标单元格对象
     * @return 单元格式样
     */
    public CellStyle build(Cell cell) {
        cell.setCellStyle(this.style);
        return this.style;
    }

    public CellStyleBuilder keepCellStyle(Cell cell) {
        this.style.cloneStyleFrom(cell.getCellStyle());
        return this;
    }

    public CellStyleBuilder keepOriginalStyle() {
        if (targetCell != null) {
            style.cloneStyleFrom(targetCell.getCellStyle());
        } else {
            throw new IllegalStateException("targetCell is null while call method keepOriginalStyle()");
        }
        return this;
    }

    /**
     * 设置单元格颜色
     *
     * @param colorIndex  颜色index值，可以用{@link IndexedColors}
     * @param patternType 填充类型
     */
    public CellStyleBuilder foregroundColor(short colorIndex, FillPatternType patternType) {
        style.setFillForegroundColor(colorIndex);
        style.setFillPattern(patternType);
        return this;
    }

    /**
     * 设置单元格颜色，
     * 固定填充类型为{@link FillPatternType#SOLID_FOREGROUND}
     *
     * @param colorIndex 颜色index值，可以用{@link IndexedColors}
     */
    public CellStyleBuilder foregroundColor(short colorIndex) {
        return foregroundColor(colorIndex, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为黑色
     */
    public CellStyleBuilder foregroundColorBlack() {
        return foregroundColor(IndexedColors.BLACK.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为红色
     */
    public CellStyleBuilder foregroundColorRed() {
        return foregroundColor(IndexedColors.RED.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为黄色
     */
    public CellStyleBuilder foregroundColorYellow() {
        return foregroundColor(IndexedColors.YELLOW.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为橙色
     */
    public CellStyleBuilder foregroundColorOrange() {
        return foregroundColor(IndexedColors.ORANGE.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为蓝色
     */
    public CellStyleBuilder foregroundColorBlue() {
        return foregroundColor(IndexedColors.BLUE.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格颜色为白色
     */
    public CellStyleBuilder foregroundColorWhite() {
        return foregroundColor(IndexedColors.WHITE.index, FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置水平位置
     */
    public CellStyleBuilder alignment(HorizontalAlignment alignment) {
        style.setAlignment(alignment);
        return this;
    }

    /**
     * 设置垂直位置
     */
    public CellStyleBuilder verticalAlignment(VerticalAlignment alignment) {
        style.setVerticalAlignment(alignment);
        return this;
    }

    /**
     * 设置水平、垂直方向上均居中
     */
    public CellStyleBuilder allAlignmentCenter() {
        return alignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置水平方向上均居中
     */
    public CellStyleBuilder alignmentCenter() {
        return alignment(HorizontalAlignment.CENTER);
    }

    /**
     * 设置垂直方向上均居中
     */
    public CellStyleBuilder verticalAlignmentCenter() {
        return verticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置单元格锁定
     */
    public CellStyleBuilder locked() {
        style.setLocked(true);
        return this;
    }

    /**
     * 设置单元格不锁定
     */
    public CellStyleBuilder unlocked() {
        style.setLocked(false);
        return this;
    }

    /**
     * 设置单元格隐藏
     */
    public CellStyleBuilder hidden() {
        style.setHidden(true);
        return this;
    }

    /**
     * 设置单元格不隐藏
     */
    public CellStyleBuilder unhidden() {
        style.setHidden(false);
        return this;
    }

    /**
     * 设置数据格式（根据格式式样）
     */
    public CellStyleBuilder dataFormat(String format) {
        style.setDataFormat(dataFormat.getFormat(format));
        return this;
    }

    /**
     * 设置数据格式（根据内置的格式下标）
     */
    public CellStyleBuilder dataFormat(short index) {
        style.setDataFormat(index);
        return this;
    }

    /**
     * 设置字体
     *
     * @param font 字体对象
     */
    public CellStyleBuilder font(Font font) {
        style.setFont(font);
        return this;
    }

    /**
     * 设置单元格四周的边框
     *
     * @param borderStyle 边框样式（实线、虚线、加粗等）
     * @param colorIndex  边框颜色
     */
    public CellStyleBuilder borderAround(BorderStyle borderStyle, short colorIndex) {
        //设置边框
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        //设置边框颜色
        style.setBottomBorderColor(colorIndex);
        style.setTopBorderColor(colorIndex);
        style.setLeftBorderColor(colorIndex);
        style.setRightBorderColor(colorIndex);
        return this;
    }

    /**
     * 设置单元格四周的边框，固定黑色单实线边框（最常规的那种样式）
     */
    public CellStyleBuilder borderAroundBlackThin() {
        return borderAround(BorderStyle.THIN, IndexedColors.BLACK.index);
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }

    public DataFormat getDataFormatObj() {
        return dataFormat;
    }

    public String getDataFormat(short index) {
        return dataFormat.getFormat(index);
    }

    public void setDataFormatObj(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }
}
