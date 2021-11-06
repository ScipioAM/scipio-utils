package com.test.bean;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelIndex;
import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author AlanScipio
 * @since 2021-09-12
 */
@ExcelIndex(
        sheetIndex = 0,
        rowStartIndex = 25
)
public class TPoDetail {

    private String poNo;

    private Integer poRowNo;

    @ExcelMapping(cellIndex = 1)
    private String partsNo;

    private String domesticPlace;

    @ExcelMapping(cellIndex = 2)
    private BigDecimal poQty;

    @ExcelMapping(cellIndex = 3)
    private BigDecimal exwPrice;

    private LocalDate lastDeliveryDate;

    private BigDecimal deliveryQty;

    private BigDecimal writeoffAmt;

    private String remark;

    private String branchNo;

    public TPoDetail() {}

    public TPoDetail(String partsNo, BigDecimal poQty, BigDecimal exwPrice) {
        this.partsNo = partsNo;
        this.poQty = poQty;
        this.exwPrice = exwPrice;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Integer getPoRowNo() {
        return poRowNo;
    }

    public void setPoRowNo(Integer poRowNo) {
        this.poRowNo = poRowNo;
    }

    public String getPartsNo() {
        return partsNo;
    }

    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    public String getDomesticPlace() {
        return domesticPlace;
    }

    public void setDomesticPlace(String domesticPlace) {
        this.domesticPlace = domesticPlace;
    }

    public BigDecimal getPoQty() {
        return poQty;
    }

    public void setPoQty(BigDecimal poQty) {
        this.poQty = poQty;
    }

    public BigDecimal getExwPrice() {
        return exwPrice;
    }

    public void setExwPrice(BigDecimal exwPrice) {
        this.exwPrice = exwPrice;
    }

    public LocalDate getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(LocalDate lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public BigDecimal getDeliveryQty() {
        return deliveryQty;
    }

    public void setDeliveryQty(BigDecimal deliveryQty) {
        this.deliveryQty = deliveryQty;
    }

    public BigDecimal getWriteoffAmt() {
        return writeoffAmt;
    }

    public void setWriteoffAmt(BigDecimal writeoffAmt) {
        this.writeoffAmt = writeoffAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }
}
