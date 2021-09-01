package com.github.ScipioAM.scipio_utils_common.os.bean;

import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceErrType;
import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceStartType;
import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;

/**
 * Windows Service 安装\卸载相关参数
 * <p>可参考：https://docs.microsoft.com/zh-cn/windows-server/administration/windows-commands/sc-create</p>
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WinServiceOption {

    /**
     * 服务名（相当于ID）
     */
    private String serviceName;

    /**
     * 指定给人看的别名
     */
    private String displayName;

    /**
     * 服务类型
     */
    private WinServiceType serviceType;

    /**
     * 服务启动类型
     */
    private WinServiceStartType startType;

    /**
     * 服务无法启动时的错误严重性
     */
    private WinServiceErrType errorType;

    /**
     * [create必填]服务二进制文件的路径
     */
    private String binPath;

    /**
     * 服务从属的服务组名称
     * <p>(组列表存储在注册表中的 HKLM\System\CurrentControlSet\Control\ServiceGroupOrder 子项中。 默认值为 null。)</p>
     */
    private String group;

    /**
     * 指定是否从 CreateService调用获取TagID。仅用于启动驱动程序时。
     */
    private Boolean tag;

    /**
     * 指定必须在此服务之前启动的服务或组的名称。 名称由 / 分割。
     */
    private String depend;

    /**
     * 指定服务将在其中运行的帐户的名称，或指定Windows驱动程序的驱动程序对象的名称。 默认设置为 LocalSystem。
     */
    private String obj;

    /**
     * 指定密码。 如果使用 LocalSystem 以外的帐户，则这是必需的
     */
    private String password;

    //==================================================================================================================

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public WinServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(WinServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public WinServiceStartType getStartType() {
        return startType;
    }

    public void setStartType(WinServiceStartType startType) {
        this.startType = startType;
    }

    public WinServiceErrType getErrorType() {
        return errorType;
    }

    public void setErrorType(WinServiceErrType errorType) {
        this.errorType = errorType;
    }

    public String getBinPath() {
        return binPath;
    }

    public void setBinPath(String binPath) {
        this.binPath = binPath;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getTag() {
        return tag;
    }

    public String getTagStr() {
        if(tag == null) {
            return null;
        }
        else {
            return tag ? "yes" : "no";
        }
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public String getDepend() {
        return depend;
    }

    public void setDepend(String depend) {
        this.depend = depend;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
