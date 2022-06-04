package com.github.ScipioAM.scipio_utils_net.mail;

/**
 * 邮箱账号信息
 *
 * @author Alan Scipio
 * @since 1.0.1-p1
 */
public class MailAccount {

    /**
     * [必填]SMTP服务器地址
     */
    private String smtpHost;

    /**
     * [必填]SMTP服务器端口号
     */
    private Integer smtpPort;

    /**
     * 是否要认证用户，默认认证，为false时username和password无用
     */
    private boolean auth = false;

    /**
     * SMTP服务器认证的用户名
     */
    private String username;

    /**
     * SMTP服务器认证的密码/授权码
     */
    private String password;

    /**
     * 是否用SSL加密，true代表是
     */
    private boolean sslFlag = false;

    /**
     * 连接超时，单位毫秒，为null不超时
     */
    private Long timeOut = 20000L;

    /**
     * 是否默认开启调试模式（控制台输出很多调试信息），默认不开启
     */
    private boolean isDebug = false;

    public MailAccount() {
    }

    public MailAccount(String smtpHost, Integer smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public MailAccount(String smtpHost, Integer smtpPort, String username, String password) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public MailAccount setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
        return this;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public MailAccount setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    public boolean isAuth() {
        return auth;
    }

    public MailAccount setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MailAccount setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailAccount setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getSslFlag() {
        return sslFlag;
    }

    public MailAccount setSslFlag(Boolean sslFlag) {
        this.sslFlag = sslFlag;
        return this;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public MailAccount setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public MailAccount setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }
}
