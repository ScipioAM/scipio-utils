package com.github.ScipioAM.scipio_utils_net.http.common;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: ResponseData
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/24
 */
public class ResponseResult {

    //HTTP响应码，为-1则代表运行时抛了异常
    private Integer responseCode;

    //响应数据
    private String data;

    //响应头
    private Map<String, List<String>> headers;

    //响应的字符编码
    private String contentEncoding;

    //响应的数据长度
    private Integer contentLength;

    //响应的连接对象
    private URLConnection connObj;

    //响应的输入流（只当responseDataMode为直接返回输入流时才set）
    private InputStream responseStream;

    public ResponseResult() { }

    public ResponseResult(Integer responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", data='" + data + '\'' +
                ", headers=" + headers +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLength=" + contentLength +
                ", connObj=" + connObj +
                ", responseStream=" + responseStream +
                '}';
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

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public List<String> getHeader(String key) {
        if(headers==null)
            return null;

        return headers.get(key);
    }

    public String getOneHeader(String key) {
        if(headers==null)
            return null;

        List<String> headerList = headers.get(key);
        if(headerList!=null) {
            return headerList.get(0);
        }
        else {
            return null;
        }
    }

    public void setHeader(String key, List<String> value) {
        if(headers==null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    public void setHeader(String key, String value) {
        if(headers==null) {
            headers = new HashMap<>();
        }
        List<String> headerList = headers.get(key);
        if(headerList==null) {
            headerList = new ArrayList<>();
        }
        headerList.add(value);
        headers.put(key, headerList);
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }
}
