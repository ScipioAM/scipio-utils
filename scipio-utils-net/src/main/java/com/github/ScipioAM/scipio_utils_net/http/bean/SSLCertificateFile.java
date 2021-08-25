package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import org.apache.http.ssl.TrustStrategy;

import java.io.File;

/**
 * HTTP连接用：SSL安全证书
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
public class SSLCertificateFile {

    /** 要加载的SSL证书 */
    private File file;

    /** 证书的密码，可以为null */
    private String password;

    /** 信任策略（为null则针对该证书信任） */
    private TrustStrategy trustStrategy;

    public SSLCertificateFile() { }

    public SSLCertificateFile(File file) {
        this.file = file;
    }

    public SSLCertificateFile(String file) {
        this.file = new File(file);
    }

    public SSLCertificateFile(File file, String password) {
        this.file = file;
        this.password = password;
    }

    public SSLCertificateFile(File file, String password, TrustStrategy trustStrategy) {
        this.file = file;
        this.password = password;
        this.trustStrategy = trustStrategy;
    }

    public boolean exists() {
        return (file != null && file.exists());
    }

    public String getFileAbsolutePath() {
        return (file == null ? null : file.getAbsolutePath());
    }

    public String getFilePath() {
        return (file == null ? null : file.getPath());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String file) {
        this.file = new File(file);
    }

    public String getPassword() {
        return password;
    }

    public char[] getPasswordArr() {
        return (StringUtil.isNull(password) ? null : password.toCharArray());
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TrustStrategy getTrustStrategy() {
        return trustStrategy;
    }

    public void setTrustStrategy(TrustStrategy trustStrategy) {
        this.trustStrategy = trustStrategy;
    }
}
