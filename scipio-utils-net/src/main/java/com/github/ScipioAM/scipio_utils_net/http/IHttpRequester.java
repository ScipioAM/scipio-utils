package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.github.ScipioAM.scipio_utils_net.http.listener.*;

import javax.net.ssl.TrustManager;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具
 * @author alan scipio
 * @since 2021/6/9
 */
public interface IHttpRequester {

    /**
     * GET请求
     * @param urlPath url路径
     * @return 响应数据
     */
    ResponseResult get(String urlPath, ResponseDataMode responseDataMode) throws Exception;

    default ResponseResult get(String urlPath) throws Exception {
        return get(urlPath,ResponseDataMode.DEFAULT);
    }

    /**
     * POST请求
     * @param urlPath url路径
     * @return 响应数据
     */
    ResponseResult post(String urlPath, ResponseDataMode dataMode) throws Exception;

    default ResponseResult post(String urlPath) throws Exception {
        return post(urlPath,ResponseDataMode.DEFAULT);
    }

    /**
     * POST请求(上传文件)
     * @param urlPath url路径
     * @param dataMode 响应数据的默认(默认|不需要响应体|直接返回响应的输入流)
     * @return 响应数据
     */
    ResponseResult postFile(String urlPath, ResponseDataMode dataMode) throws Exception;

    default ResponseResult postFile(String urlPath) throws Exception {
        return postFile(urlPath,ResponseDataMode.DEFAULT);
    }

    /**
     * 下载文件
     * @param urlPath url路径
     * @param downloadFileDir 下载文件的父路径
     * @param fileName 下载文件的文件名
     * @return 响应数据
     */
    ResponseResult download(String urlPath, String downloadFileDir, String fileName) throws Exception;

    //==================================================================================================================

    /**
     * 自定义请求头
     * @param headers 请求头参数
     * @return 调用链
     */
    default IHttpRequester setRequestHeader(Map<String,String> headers) {
        System.err.println("method[setRequestHeader] not override, nothing happened");
        return this;
    }

    /**
     * 自定义请求头 - 仅一对参数
     * @param headKey 参数key
     * @param headValue 参数value
     * @return 调用链
     */
    default IHttpRequester setRequestHeader(String headKey, String headValue) {
        Map<String,String> headers = new HashMap<>();
        headers.put(headKey, headValue);
        return setRequestHeader(headers);
    }

    /**
     * 设置请求参数 - json数据
     * @param requestJsonData 请求的json数据
     * @return 调用链
     */
    default IHttpRequester setRequestJson(String requestJsonData) {
        System.err.println("method[setRequestJson] not override, nothing happened");
        return this;
    }

    /**
     * 设置请求参数 - 传统表单
     * @param formData 请求的参数本身
     * @return 调用链
     */
    default IHttpRequester setRequestForm(Map<String,String> formData) {
        System.err.println("method[setRequestForm] not override, nothing happened");
        return this;
    }

    /**
     * 设置请求参数 - xml数据
     * @param requestXmlData 请求的xml数据
     * @return 调用链
     */
    default IHttpRequester setRequestXml(String requestXmlData) {
        System.err.println("method[requestXmlData] not override, nothing happened");
        return this;
    }

    default IHttpRequester setRequestText(String content) {
        System.err.println("method[setRequestText] not override, nothing happened");
        return this;
    }

    /**
     * 设置要提交的文件
     * @param uploadFiles 要提交的文件们
     * @return 调用链
     */
    default IHttpRequester setUploadFile(Map<String, File> uploadFiles) {
        System.err.println("method[setRequestFile] not override, nothing happened");
        return this;
    }

    /**
     * 设置要提交的文件 - 单个文件
     * @param uploadFile 要提交的文件(key固定为"file")
     * @return 调用链
     */
    default IHttpRequester setUploadFile(File uploadFile) {
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put("file",uploadFile);
        return setUploadFile(fileMap);
    }

    /**
     * 设置上传文件缓冲区大小
     */
    default IHttpRequester setFileBufferSize(Integer fileBufferSize) {
        System.err.println("method[setFileBufferSize] not override, nothing happened");
        return this;
    }

    /**
     * 设置下载文件的全路径
     */
    default IHttpRequester setDownloadFilePath(String downloadFilePath) {
        System.err.println("method[setDownloadFilePath] not override, nothing happened");
        return this;
    }

    /**
     * 设置followRedirects参数
     * <p>参数含义：是否关闭重定向以获取跳转后的真实地址</p>
     */
    default IHttpRequester setFollowRedirects(boolean followRedirects) {
        System.err.println("method[followRedirects] not override, nothing happened");
        return this;
    }

    /**
     * 设置userAgent
     */
    default IHttpRequester setUserAgent(String userAgent) {
        System.err.println("method[setUserAgent] not override, nothing happened");
        return this;
    }

    /**
     * 设置请求和响应的编码字符集
     */
    default IHttpRequester setCharset(String charset) {
        System.err.println("method[setCharset] not override, nothing happened");
        return this;
    }

    /**
     * 设置信任管理器（决定了信任哪些SSL证书）
     */
    default IHttpRequester setTrustManagers(TrustManager... managers) {
        System.err.println("method[setTrustManagers] not override, nothing happened");
        return this;
    }

    /**
     * 设置SSLContext创建者
     */
    default IHttpRequester setSSLContextInitializer(SSLContextInitializer sslContextInitializer) {
        System.err.println("method[setSSLContextInitializer] not override, nothing happened");
        return this;
    }

    /**
     * 设置上传监听器
     */
    default IHttpRequester setUploadListener(UploadListener uploadListener) {
        System.err.println("method[setUploadListener] not override, nothing happened");
        return this;
    }

    /**
     * 设置下载监听器
     */
    default IHttpRequester setDownloadListener(DownloadListener downloadListener) {
        System.err.println("method[setDownloadListener] not override, nothing happened");
        return this;
    }

    /**
     * 设置成功响应后的处理器（2xx的HTTP响应码）
     */
    default IHttpRequester setResponseSuccessHandler(ResponseSuccessHandler responseSuccessHandler) {
        System.err.println("method[setResponseSuccessHandler] not override, nothing happened");
        return this;
    }

    /**
     * 设置失败响应后的处理器（非2xx的HTTP响应码）
     */
    default IHttpRequester setResponseFailureHandler(ResponseFailureHandler responseFailureHandler) {
        System.err.println("method[setResponseFailureHandler] not override, nothing happened");
        return this;
    }

    /**
     * 设置执行时抛出异常的处理器（通常是最外层catch）
     */
    default IHttpRequester setExecuteErrorHandler(ExecuteErrorHandler executeErrorHandler) {
        System.err.println("method[setExecuteErrorHandler] not override, nothing happened");
        return this;
    }

    /**
     * 设置开始执行请求前的监听器
     */
    default IHttpRequester setStartExecuteListener(StartExecuteListener startExecuteListener) {
        System.err.println("method[setStartExecuteListener] not override, nothing happened");
        return this;
    }

    /**
     * 设置预期的数据响应模式
     */
    default IHttpRequester setResponseDataMode(ResponseDataMode responseDataMode) {
        System.err.println("method[setResponseDataMode] not override, nothing happened");
        return this;
    }

}
