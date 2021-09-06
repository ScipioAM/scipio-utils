package com.github.ScipioAM.scipio_utils_common.os.bean;

import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;

/**
 * sc命令执行后的结果或service当前状态
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WinServiceResult {

    /** cmd里反馈执行是否成功 */
    private Boolean execSuccess;

    /** cmd里原始输出的信息 */
    private String originalMsg;

    /** 服务名 */
    private String serviceName;

    /** 服务类型 */
    private WinServiceType serviceType;

    /** 服务当前状态 */
    private WinServiceRunState state;

    private Integer win32ExitCode;

    private Integer serviceExitCode;

    private Integer checkPoint;

    private Integer waitHint;

    /** 服务运行时的进程id */
    private Integer pid;

    /** 服务的sid */
    private String sid;

    /** 备注 */
    private String remarks;

    //==================================================================================================================

    @Override
    public String toString() {
        return "WinServiceStatus{" +
                "execSuccess=" + execSuccess +
                ", originalMsg='" + originalMsg + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceType=" + serviceType +
                ", state=" + state +
                ", win32ExitCode=" + win32ExitCode +
                ", serviceExitCode=" + serviceExitCode +
                ", checkPoint=" + checkPoint +
                ", waitHint=" + waitHint +
                ", pid=" + pid +
                ", sid='" + sid + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public Boolean getExecSuccess() {
        return execSuccess;
    }

    public void setExecSuccess(Boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public WinServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(WinServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public WinServiceRunState getState() {
        return state;
    }

    public void setState(WinServiceRunState state) {
        this.state = state;
    }

    public Integer getWin32ExitCode() {
        return win32ExitCode;
    }

    public void setWin32ExitCode(Integer win32ExitCode) {
        this.win32ExitCode = win32ExitCode;
    }

    public Integer getServiceExitCode() {
        return serviceExitCode;
    }

    public void setServiceExitCode(Integer serviceExitCode) {
        this.serviceExitCode = serviceExitCode;
    }

    public Integer getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(Integer checkPoint) {
        this.checkPoint = checkPoint;
    }

    public Integer getWaitHint() {
        return waitHint;
    }

    public void setWaitHint(Integer waitHint) {
        this.waitHint = waitHint;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getOriginalMsg() {
        return originalMsg;
    }

    public void setOriginalMsg(String originalMsg) {
        this.originalMsg = originalMsg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
