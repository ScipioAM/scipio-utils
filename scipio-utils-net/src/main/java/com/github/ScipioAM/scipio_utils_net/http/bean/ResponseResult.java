package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class: ResponseData
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/24
 */
public class ResponseResult {

    //执行代码出现异常时的响应码
    public static final int EXECUTE_ERR_CODE = -1;

    private String requestUrl;

    private HttpMethod httpMethod;

    //HTTP响应码，为-1则代表运行时抛了异常
    private Integer responseCode;

    private ResponseDataMode dataMode;

    //响应数据
    private String data;

    //响应头
    private Map<String, String> headers;

    //响应的压缩编码
    private String contentEncoding;

    //响应的数据长度
    private Long contentLength;

    //响应数据类型
    private String contentType;

    //响应的连接对象
    private URLConnection connObj;

    //响应的输入流（只当responseDataMode为直接返回输入流时才set）
    private InputStream responseStream;

    //异常信息
    private String errorMsg;

    public ResponseResult() { }

    public ResponseResult(Integer responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "requestUrl='" + requestUrl + '\'' +
                ", httpMethod=" + httpMethod +
                ", responseCode=" + responseCode +
                ", dataMode=" + dataMode +
                ", data='" + data + '\'' +
                ", headers=" + headers +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLength=" + contentLength +
                ", contentType='" + contentType + '\'' +
                ", connObj=" + connObj +
                ", responseStream=" + responseStream +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public ResponseDataMode getDataMode() {
        return dataMode;
    }

    public void setDataMode(ResponseDataMode dataMode) {
        this.dataMode = dataMode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public InputStream getResponseStream() {
        return responseStream;
    }

    public void setResponseStream(InputStream responseStream) {
        this.responseStream = responseStream;
    }

    public URLConnection getConnObj() {
        return connObj;
    }

    public void setConnObj(URLConnection connObj) {
        this.connObj = connObj;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        if(headers==null)
            return null;

        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        if(headers==null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
