package com.github.ScipioAM.scipio_utils_net.mail;

/**
 * 邮件发送信息
 * @author Alan Scipio
 * @since 2021/6/25
 */
public class SendInfo {

    /**
     * 发件人
     */
    private String fromAddress;

    /**
     * SMTP服务器
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码/授权码
     */
    private String password;

    /**
     * <b>是否用SSL加密</b>
     */
    private Boolean sslFlag;

    /**
     * 附件最大大小(MB)
     */
    private Integer maxFileSize;

    //TODO 正文、标题、抄送、附件等

}
