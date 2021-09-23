package com.github.ScipioAM.scipio_utils_common.os.constants;

/**
 * Windows服务无法启动时的错误严重性
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public enum WinServiceErrType {

    /** 指定记录错误并显示消息框，通知用户服务无法启动。 启动将继续。 这是默认值 */
    NORMAL("normal"),
    /** 指定在可能的情况下记录日志。然后计算机每次都会尝试使用上次已知的良好配置重启。但不保证一定是没问题的配置 */
    SEVERE("severe"),
    /** 指定在可能的情况下记录日志。然后计算机只会尝试使用上次已知的良好配置重启一次 */
    CRITICAL("critical"),
    /** 指定记录错误并继续启动。除了在事件日志中记录错误外，不会向用户发送任何通知 */
    IGNORE("ignore");

    public final String value;

    WinServiceErrType(String value) {
        this.value = value;
    }

}
