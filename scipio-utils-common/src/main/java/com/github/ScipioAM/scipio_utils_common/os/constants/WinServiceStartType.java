package com.github.ScipioAM.scipio_utils_common.os.constants;

/**
 * Windows服务启动类型
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public enum WinServiceStartType {

    /** 指定启动加载程序加载的设备驱动程序 */
    BOOT("boot"),
    /** 定在内核初始化期间启动的设备驱动程序 */
    SYSTEM("system"),
    /** 指定每次重新启动计算机时自动启动的服务，即使没有用户登录到计算机，该服务也运行 */
    AUTO("auto"),
    /** 指定必须手动启动的服务。 如果未指定 start=xxx ，则这是默认值 */
    DEMAND("demand"),
    /** 指定无法启动的服务。 若要启动已禁用的服务，将启动类型更改为其他值 */
    DISABLED("disabled"),
    /** 指定在其他自动服务启动后的一小段时间自动启动的服务 */
    DELAYED_AUTO("delayed-auto");

    public final String value;

    WinServiceStartType(String value) {
        this.value = value;
    }

}
