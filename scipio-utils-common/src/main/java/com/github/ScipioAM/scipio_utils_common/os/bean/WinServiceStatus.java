package com.github.ScipioAM.scipio_utils_common.os.bean;

import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;

/**
 * Windows Service的状态
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WinServiceStatus {

    /** cmd里反馈执行是否成功 */
    private Boolean execSuccess;

    /** cmd里反馈的错误信息 */
    private String errorMsg;

    private String serviceName;

    private WinServiceType serviceType;

    private WinServiceRunState state;

    private Integer win32ExitCode;

    private Integer serviceExitCode;

    private Integer checkPoint;

    private Integer waitHint;

    //==================================================================================================================


    @Override
    public String toString() {
        return "WinServiceStatus{" +
                "execSuccess=" + execSuccess +
                ", errorMsg='" + errorMsg + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceType=" + serviceType +
                ", state=" + state +
                ", win32ExitCode=" + win32ExitCode +
                ", serviceExitCode=" + serviceExitCode +
                ", checkPoint=" + checkPoint +
                ", waitHint=" + waitHint +
                '}';
    }

    public Boolean getExecSuccess() {
        return execSuccess;
    }

    public void setExecSuccess(Boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
}
