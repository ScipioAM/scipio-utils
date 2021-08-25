package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

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
    private String charset = "UTF-8";

    private String userAgent = null;

    /** 代理 */
    private Proxy proxy = null;

    /** 自定义请求头 */
    private Map<String,String> requestHeaders;

    /** 连接超时，单位毫米 */
    private Integer connectTimeout;

    /** 是否关闭重定向以获取跳转后的真实地址,默认false */
    private boolean isFollowRedirects = false;

    /** 上传文件时的缓冲区大小 */
    private Integer fileBufferSize;

    /** 下载文件的全路径，如果不为空则代表需要下载 */
    private String downloadFilePath;

    /** 预期响应数据的默认 */
    private ResponseDataMode responseDataMode;

    /** 下载时自动生成文件后缀（根据contentType进行截取） */
    private boolean downloadAutoExtension = false;

    public boolean isDownloadAutoExtension() {
        return downloadAutoExtension;
    }

    public void setDownloadAutoExtension(boolean downloadAutoExtension) {
        this.downloadAutoExtension = downloadAutoExtension;
    }

    public ResponseDataMode getResponseDataMode() {
        return responseDataMode;
    }

    public void setResponseDataMode(ResponseDataMode responseDataMode) {
        this.responseDataMode = responseDataMode;
    }

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
