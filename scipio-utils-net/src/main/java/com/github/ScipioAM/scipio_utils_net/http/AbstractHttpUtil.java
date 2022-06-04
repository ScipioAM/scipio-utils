package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alan Scipio
 * @date 2018/6/28
 * @since 1.0.0
 */
public abstract class AbstractHttpUtil extends AbstractHttpBase {

    /**
     * 发起http请求并获取返回的数据(不包括输出文件)
     *
     * @param urlPath          url路径
     * @param httpMethod       get请求还是post请求
     * @param requestContent   请求的内容
     * @param responseDataMode 希望返回数据以什么形式封装
     * @return 响应数据
     * @throws IOException              输出请求体异常
     * @throws IllegalArgumentException 传入的参数非法
     */
    @Override
    protected ResponseResult doRequest(String urlPath, HttpMethod httpMethod, RequestContent requestContent, ResponseDataMode responseDataMode)
            throws IOException, IllegalArgumentException {
        //确定contentType
        String contentType = null;
        if (requestContent != null) {
            contentType = requestContent.getContentType();
            if (contentType == null || "".equals(contentType)) {
                contentType = requestContent.getMode().contentType;
            }
        }
        //准备最终url
        String finalUrl = prepareFinalUrl(urlPath, httpMethod, requestContent);
        URL url = new URL(finalUrl);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) (requestInfo.getProxy() == null ? url.openConnection() : url.openConnection(requestInfo.getProxy()));
        //公共的请求头设置
        setCommonConnectionData(conn, contentType, httpMethod);
        //如果是https必要的处理
        if (isHttpsProtocol(url)) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            try {
                httpsConn.setSSLSocketFactory(super.createSSLSocketFactory());//设置SSL
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //输出请求内容
        outputRequestContent(conn, httpMethod, requestContent);
        //获取响应码
        int responseCode = conn.getResponseCode();//获取返回的状态码
        //处理返回的数据
        ResponseResult result = prepareResponseResult(urlPath, responseCode, httpMethod, conn);
        result = super.handleResponse(result, responseCode, responseDataMode, conn.getInputStream(), conn.getContentLength(), conn.getContentType(), conn.getContentEncoding());
        return result;
    }

    /**
     * 发起http请求，上传文件（可能还包括其他参数），获取返回的数据
     *
     * @param urlPath          请求的url
     * @param requestParams    请求的字符串参数
     * @param uploadFiles      要上传的文件
     * @param responseDataMode 希望返回数据以什么形式封装
     * @return 响应数据
     * @throws IOException              输出请求体异常
     * @throws IllegalArgumentException 传入的参数非法
     */
    @Override
    protected ResponseResult doFileRequest(String urlPath, Map<String, String> requestParams, Map<String, File> uploadFiles, ResponseDataMode responseDataMode)
            throws IOException, IllegalArgumentException {
        //Content-Type的定义
        String contentType = "multipart/form-data; boundary=" + BOUNDARY;

        URL url = new URL(urlPath);
        //打开连接
        HttpURLConnection conn = (HttpURLConnection) (requestInfo.getProxy() == null ? url.openConnection() : url.openConnection(requestInfo.getProxy()));
        //公共的请求头设置
        setCommonConnectionData(conn, contentType, HttpMethod.POST);
        //https的情况下
        if (isHttpsProtocol(url)) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            try {
                httpsConn.setSSLSocketFactory(super.createSSLSocketFactory());//设置SSL
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //执行数据输出操作
        doMultipartOutput(conn.getOutputStream(), requestParams, uploadFiles);
        //获取响应码
        int responseCode = conn.getResponseCode();
        //处理返回的数据
        ResponseResult result = prepareResponseResult(urlPath, responseCode, HttpMethod.POST, conn);
        result = super.handleResponse(result, responseCode, responseDataMode, conn.getInputStream(), conn.getContentLength(), conn.getContentType(), conn.getContentEncoding());
        return result;
    }

    private ResponseResult prepareResponseResult(String urlPath, int responseCode, HttpMethod httpMethod, HttpURLConnection conn) {
        ResponseResult result = new ResponseResult();
        result.setRequestUrl(urlPath);
        result.setHttpMethod(httpMethod);
        result.setResponseCode(responseCode);
        result.setContentEncoding(conn.getContentEncoding());
        result.setContentLength(conn.getContentLengthLong());
        result.setContentType(conn.getContentType());
        result.setHeaders(transformHeaders(conn.getHeaderFields()));
        result.setConnObj(conn);
        return result;
    }

    private Map<String, String> transformHeaders(Map<String, List<String>> originalHeaders) {
        Map<String, String> finalHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> header : originalHeaders.entrySet()) {
            StringBuilder value = new StringBuilder();
            List<String> headerValueList = header.getValue();
            if (headerValueList != null && headerValueList.size() > 0) {
                for (String f : headerValueList) {
                    value.append(f).append(",");
                }
                value.deleteCharAt(value.length() - 1);
            }
            finalHeaders.put(header.getKey(), value.toString());
        }
        return finalHeaders;
    }

}
