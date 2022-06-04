package com.github.ScipioAM.scipio_utils_net.mail;

/**
 * 邮件发送结果
 *
 * @author Alan Scipio
 * @since 2021/6/25
 */
public enum MailSendResult {

    //发送成功
    SUCCESS(0, "mail send success"),
    //失败 - SMTP服务器报告发送失败
    FAIL_SEND(101, "mail send failed, SMTP server report failure"),
    FAIL_CONNECT(102, "connect to smtp server failed"),
    FAIL_CLOSE(103, "close connection failed");

    /**
     * 结果代码
     */
    public final int code;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 子结果（一般如发送失败，关闭也失败）
     */
    public MailSendResult subResult;

    MailSendResult(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 控制台打印结果
     */
    public void print() {
        print(false);
    }

    private void print(boolean isSubResult) {
        String prefix = (isSubResult ? "SubResult, " : "");
        if (code == 0) {
            System.out.println(prefix + "ResultCode[" + code + "] Send success");
        } else {
            System.err.println("ResultCode[" + code + "] " + desc);
        }
        if (subResult != null) {
            subResult.print(true);
        }
    }

}
