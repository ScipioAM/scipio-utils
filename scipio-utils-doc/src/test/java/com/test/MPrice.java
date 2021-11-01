package com.test;

import com.github.ScipioAM.scipio_utils_doc.excel.annotations.ExcelMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author AlanScipio
 * @since 2021-09-12
 */
public class MPrice {

    @ExcelMapping(cellIndex = 3)
    private String partsNo;

    @ExcelMapping(cellIndex = 2)
    private String custCode;

    private Integer saleType;

    private String saleOrderNo;

    @ExcelMapping(cellIndex = 10)
    private String effectiveDate;

    @ExcelMapping(cellIndex = 11)
    private String invalidDate;

    @ExcelMapping(cellIndex = 6)
    private BigDecimal exwPrice;

    @ExcelMapping(cellIndex = 7)
    private BigDecimal fobPrice;

    @ExcelMapping(cellIndex = 8)
    private BigDecimal fobPriceUsd;

    private String fileId;

    private LocalDate readDate;

    //品目名称
    private String partsName;

    //客户名称
    private String custName;

    //供应商名称
    private String supplierName;

    protected Boolean deleteFlag;
    protected String remark0;
    protected String remark1;
    protected String remark2;
    protected String remark3;
    protected String remark4;
    protected LocalDateTime createTime;
    protected String createId;
    protected LocalDateTime updateTime;
    protected String updateId;
    protected String opId;

    public MPrice() {}

    public MPrice(String partsNo, String custCode, Integer saleType, String saleOrderNo, String effectiveDate) {
        this.partsNo = partsNo;
        this.custCode = custCode;
        this.saleType = saleType;
        this.saleOrderNo = saleOrderNo;
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "MPrice{" +
                "partsNo='" + partsNo + '\'' +
                ", custCode='" + custCode + '\'' +
                ", saleType=" + saleType +
                ", saleOrderNo='" + saleOrderNo + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", invalidDate='" + invalidDate + '\'' +
                ", exwPrice=" + exwPrice +
                ", fobPrice=" + fobPrice +
                ", fobPriceUsd=" + fobPriceUsd +
                ", fileId='" + fileId + '\'' +
                ", readDate=" + readDate +
                ", partsName='" + partsName + '\'' +
                ", custName='" + custName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", remark0='" + remark0 + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", remark4='" + remark4 + '\'' +
                ", createTime=" + createTime +
                ", createId='" + createId + '\'' +
                ", updateTime=" + updateTime +
                ", updateId='" + updateId + '\'' +
                ", opId='" + opId + '\'' +
                '}';
    }

    public String getPartsNo() {
        return partsNo;
    }

    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public BigDecimal getExwPrice() {
        return exwPrice;
    }

    public void setExwPrice(BigDecimal exwPrice) {
        this.exwPrice = exwPrice;
    }

    public BigDecimal getFobPrice() {
        return fobPrice;
    }

    public void setFobPrice(BigDecimal fobPrice) {
        this.fobPrice = fobPrice;
    }

    public BigDecimal getFobPriceUsd() {
        return fobPriceUsd;
    }

    public void setFobPriceUsd(BigDecimal fobPriceUsd) {
        this.fobPriceUsd = fobPriceUsd;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public LocalDate getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDate readDate) {
        this.readDate = readDate;
    }

    public String getPartsName() {
        return partsName;
    }

    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getRemark0() {
        return remark0;
    }

    public void setRemark0(String remark0) {
        this.remark0 = remark0;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
