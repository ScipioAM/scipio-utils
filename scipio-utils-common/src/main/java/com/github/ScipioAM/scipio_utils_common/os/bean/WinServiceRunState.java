package com.github.ScipioAM.scipio_utils_common.os.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows Service 运行状态
 *      <p>参考：https://docs.microsoft.com/en-us/windows/win32/api/winsvc/ns-winsvc-service_status</p>
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public enum WinServiceRunState {

    /** service不在运行 */
    STOPPED(1),
    /** service在启动中 */
    START_PENDING(2),
    /** service在停止中 */
    STOP_PENDING(3),
    /** service在运行 */
    RUNNING(4),
    /** service在继续中 */
    CONTINUE_PENDING(5),
    /** service在暂停中 */
    PAUSE_PENDING(6),
    /** service已暂停 */
    PAUSED(7),
    /** 未知状态 */
    UNKNOWN(-1),
    /** 解析时未匹配中 */
    NO_MATCH(-2);

    public final int code;//Windows中的代码
    public String remark;//备注（未知状态时或解析未匹配中时的整个字符串）

    WinServiceRunState(int code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    /**
     * 针对sc相关命令的返回消息进行解析（只针对Service类型那一行）
     * @param line 要解析的一行消息
     * @return 解析结果，未匹配中正则就返回NO_MATCH
     */
    public static WinServiceRunState analyze(String line) {
        Pattern pattern = Pattern.compile("(\\s+STATE\\s+:\\s)(\\d{1,2})(\\s{1,2})(.+)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            try {
                int code = Integer.parseInt(matcher.group(2));
                switch (code) {
                    case 1:
                        return STOPPED;
                    case 2:
                        return START_PENDING;
                    case 3:
                        return STOP_PENDING;
                    case 4:
                        return RUNNING;
                    case 5:
                        return CONTINUE_PENDING;
                    case 6:
                        return PAUSE_PENDING;
                    case 7:
                        return PAUSED;
                    default:
                        UNKNOWN.remark = line;
                        return UNKNOWN;
                }
            } catch (Exception e) {
                System.err.println("Windows service run state is not a number : " + matcher.group(2));
                UNKNOWN.remark = line;
                return UNKNOWN;
            }
        }
        else {
            NO_MATCH.remark = line;
            return NO_MATCH;
        }
    }

}
