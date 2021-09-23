package com.github.ScipioAM.scipio_utils_common.os.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows服务类型
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public enum WinServiceType {

    /** 指定在其自己的进程中运行的服务。 它不会与其他服务共享可执行文件。 这是默认值 */
    OWN(10,"own","WIN32_OWN_PROCESS"),
    /** 指定作为共享进程运行的服务。 它与其他服务共享可执行文件 */
    SHARE(20,"share","WIN32_SHARE_PROCESS"),
    /** 指定驱动程序 */
    KERNEL(1,"kernel","_KERNEL_DRIVER"),
    /** 指定文件系统驱动程序 */
    FILE_SYS(2,"filesys","FILE_SYSTEM_DRIVER"),
    /** 指定可与桌面交互的服务，从用户处接收输入。 交互式服务必须在 LocalSystem 帐户下运行。 此类型必须与 type= own 或 type= shared (一起使用，例如 ，type= interact type= own) 。 仅使用 type= interact 将产生错误。 */
    INTERACT(100,"interact","INTERACTIVE_PROCESS"),
    /** 其他出现但未被定义的类型 */
    OTHER(-1,null,null),
    /** 解析时未匹配中 */
    NO_MATCH(-2,null,null);


    public final int code;//Windows中的代码
    public final String value;//调用命令时用的参数值
    public final String winName;//在Windows中的名称

    private String unknownCode;//未知代码
    private String unknownWinName;//未知Windows中Service类型名称(或未匹配中时的整个字符串)

    WinServiceType(int code, String value, String winName) {
        this.code = code;
        this.value = value;
        this.winName = winName;
    }

    public String getUnknownCode() {
        return unknownCode;
    }

    public String getUnknownWinName() {
        return unknownWinName;
    }

    /**
     * 针对sc相关命令的返回消息进行解析（只针对Service类型那一行）
     * @param line 要解析的一行消息
     * @return 解析结果，未匹配中正则就返回NO_MATCH
     */
    public static WinServiceType analyze(String line) {
        Pattern pattern = Pattern.compile("(\\s+TYPE\\s+:\\s)(\\w{1,2})(\\s{1,2})(.+)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            try {
                int code = Integer.parseInt(matcher.group(2));
                switch (code) {
                    case 1:
                        return KERNEL;
                    case 2:
                        return FILE_SYS;
                    case 10:
                        return OWN;
                    case 20:
                        return SHARE;
                    case 100:
                        return INTERACT;
                    default:
                        OTHER.unknownCode = matcher.group(2);
                        OTHER.unknownWinName = matcher.group(4);
                        return OTHER;
                }
            }catch (Exception e) {
                System.err.println("Windows service type code is not a number : " + matcher.group(2));
                OTHER.unknownWinName = matcher.group(4);
                OTHER.unknownCode = matcher.group(2);
                return OTHER;
            }
        }
        else {
            NO_MATCH.unknownWinName = line;
            return NO_MATCH;
        }
    }

}
