package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;

/**
 * @date 2021/9/23
 */
public class StyleBean {

    @ExcelMapping(cellIndex = 2,rowIndex = 12)
    private String acptNo;

    @ExcelMapping(cellIndex = 2,rowIndex = 13)
    private String acptQty;

    @ExcelMapping(cellIndex = 2,rowIndex = 14)
    private String acptDate;

    @ExcelMapping(cellIndex = 2,rowIndex = 15)
    private String price;

    @ExcelMapping(cellIndex = 2,rowIndex = 16)
    private String priceType;

    @ExcelMapping(cellIndex = 2,rowIndex = 17)
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
