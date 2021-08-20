package com.github.ScipioAM.scipio_utils_net.http.bean;

import java.net.Proxy;
import java.util.Map;

/**
 * 请求的基础信息（请求头）
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/20
 */
public class RequestInfo {

    /** 编码字符集，默认UTF-8 */
    protected String charset = "UTF-8";

    protected String userAgent = null;

    /** 代理 */
    protected Proxy proxy = null;

    /** 自定义请求头 */
    protected Map<String,String> requestHeaders;

    /** 连接超时，单位毫米 */
    protected Integer connectTimeout;

    /** 是否关闭重定向以获取跳转后的真实地址,默认false */
    protected boolean isFollowRedirects = false;

    /** 上传文件时的缓冲区大小 */
    protected Integer fileBufferSize;

    /** 下载文件的全路径，如果不为空则代表需要下载 */
    protected String downloadFilePath;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isFollowRedirects() {
        return isFollowRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        isFollowRedirects = followRedirects;
    }

    public Integer getFileBufferSize() {
        return fileBufferSize;
    }

    public void setFileBufferSize(Integer fileBufferSize) {
        this.fileBufferSize = fileBufferSize;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }
}
