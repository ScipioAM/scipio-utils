package com.github.ScipioAM.scipio_utils_javafx.persistence;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * @author Alan Scipio
 * @since 1.0.10 _ 2022/2/21
 */
public class BaseEntity {

    protected String updateTime;
    protected String remark0;

    @Basic
    @Column(name = "UPDATE_TIME")
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "REMARK0")
    public String getRemark0() {
        return remark0;
    }

    public void setRemark0(String remark0) {
        this.remark0 = remark0;
    }

}
