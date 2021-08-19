package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

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
    ResponseResult get(String urlPath) throws Exception;

    /**
     * POST请求
     * @param urlPath url路径
     * @return 响应数据
     */
    ResponseResult post(String urlPath) throws Exception;

    /**
     * POST请求(上传文件)
     * @param urlPath url路径
     * @param dataMode 响应数据的默认(默认|不需要响应体|直接返回响应的输入流)
     * @return 响应数据
     */
    ResponseResult postFile(String urlPath, ResponseDataMode dataMode) throws Exception;

    //==================================================================================================================

    /**
     * 自定义请求头
     * @param headers 请求头参数
     * @return 调用链
     */
    IHttpRequester setRequestHeader(Map<String,String> headers);

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
    default IHttpRequester setRequestJsonData(String requestJsonData) {
        System.err.println("method[setRequestJsonData] not override, nothing happened");
        return this;
    }

    /**
     * 设置请求参数 - 传统表单
     * @param formData 请求的参数本身
     * @return 调用链
     */
    default IHttpRequester setRequestFormData(Map<String,String> formData) {
        System.err.println("method[setRequestFormData] not override, nothing happened");
        return this;
    }

    /**
     * 设置请求参数 - xml数据
     * @param requestXmlData 请求的xml数据
     * @return 调用链
     */
    default IHttpRequester setRequestXmlData(String requestXmlData) {
        System.err.println("method[setRequestXmlData] not override, nothing happened");
        return this;
    }

    /**
     * 设置要提交的文件
     * @param requestFile 要提交的文件们
     * @return 调用链
     */
    default IHttpRequester setRequestFile(Map<String, File> requestFile) {
        System.err.println("method[setRequestFile] not override, nothing happened");
        return this;
    }

    /**
     * 设置要提交的文件 - 单个文件
     * @param requestFile 要提交的文件(key固定为"file")
     * @return 调用链
     */
    default IHttpRequester setRequestFile(File requestFile) {
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put("file",requestFile);
        return setRequestFile(fileMap);
    }

}
