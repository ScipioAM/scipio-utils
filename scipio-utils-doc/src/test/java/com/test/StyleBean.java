package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;

/**
 * @date 2021/9/23
 */
@ExcelIndex(
        sheetIndex = 5,
        rowStartIndex = 11,
        rowLength = 6,
        columnStartIndex = 2,
        columnLength = 1)
public class StyleBean {

    @ExcelMapping(rowIndex = 11)
    private String acptNo;

    @ExcelMapping(rowIndex = 12)
    private String acptQty;

    @ExcelMapping(rowIndex = 13)
    private String acptDate;

    @ExcelMapping(rowIndex = 14)
    private String price;

    @ExcelMapping(rowIndex = 15)
    private String priceType;

    @ExcelMapping(rowIndex = 16)
    private String materialCost;

    @Override
    public String toString() {
        return "StyleBean{" +
                "acptNo='" + acptNo + '\'' +
                ", acptQty='" + acptQty + '\'' +
                ", acptDate='" + acptDate + '\'' +
                ", price='" + price + '\'' +
                ", priceType='" + priceType + '\'' +
                ", materialCost='" + materialCost + '\'' +
                '}';
    }

    public String getAcptNo() {
        return acptNo;
    }

    public void setAcptNo(String acptNo) {
        this.acptNo = acptNo;
    }

    public String getAcptQty() {
        return acptQty;
    }

    public void setAcptQty(String acptQty) {
        this.acptQty = acptQty;
    }

    public String getAcptDate() {
        return acptDate;
    }

    public void setAcptDate(String acptDate) {
        this.acptDate = acptDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(String materialCost) {
        this.materialCost = materialCost;
    }
}
