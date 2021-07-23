package com.github.ScipioAM.scipio_utils_net.mail;

/**
 * 邮件发送结果
 * @author Alan Scipio
 * @since 2021/6/25
 */
public enum MailSendResult {

    SUCCESS(0,"mail send success");

    /**
     * 结果代码
     */
    public final int code;

    /**
     * 描述
     */
    public final String desc;

    MailSendResult(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
