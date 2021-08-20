package com.github.ScipioAM.scipio_utils_net.api_lib;

import com.github.ScipioAM.scipio_utils_net.http.IHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API调用工具类 - 增强型(主要是File相关增强)
 * @param <R> API响应的具体子类，取Response之意
 * @param <S> 本父类的子类，取Self之意
 * @author Alan Scipio
 * @since 2021/7/14
 */
public abstract class ApiUtilBaseEnhance<R extends ApiResponse, S extends ApiUtilBase<R,S>> extends ApiUtilBase<R,S> {

    /**
     * 请求时提交的文件map
     */
    protected HashMap<String, File> submitFileMap = new HashMap<>();

    //==================================================================================================================

    public R fileRequest(String url, ResponseDataMode responseDataMode, Map<String, String> formData) throws Exception {
        return doApiFileRequest(url,responseDataMode,formData,null);
    }

    public R fileRequest(String url, ResponseDataMode responseDataMode, Object jsonData) throws Exception {
        isJsonSubmit = true;
        return doApiFileRequest(url,responseDataMode,null,jsonData);
    }

    public R fileRequest(String url, Map<String, String> formData) throws Exception {
        return doApiFileRequest(url,ResponseDataMode.DEFAULT,formData,null);
    }

    public R fileRequest(String url,  Object jsonData) throws Exception {
        isJsonSubmit = true;
        return doApiFileRequest(url,ResponseDataMode.DEFAULT,null,jsonData);
    }

    public R fileRequest(String url, File submitFile, Map<String, String> formData) throws Exception {
        setSubmitFile(submitFile);
        return doApiFileRequest(url,ResponseDataMode.DEFAULT,formData,null);
    }

    public R fileRequest(String url, File submitFile, Object jsonData) throws Exception {
        isJsonSubmit = true;
        setSubmitFile(submitFile);
        return doApiFileRequest(url,ResponseDataMode.DEFAULT,null,jsonData);
    }

    public R fileRequest(String url, File submitFile) throws Exception {
        setSubmitFile(submitFile);
        return doApiFileRequest(url,ResponseDataMode.DEFAULT,null,null);
    }

    //==================================================================================================================

    /**
     * 发起API请求 - 上传文件版
     * @param url 请求url
     * @param responseDataMode 预先指定响应数据的模式
     * @param formData 请求参数，传统表单格式，可为null
     * @param jsonData 请求参数，json格式，可为null
     * @return api响应结果
     * @throws Exception 新建ApiResponse实例失败
     */
    private R doApiFileRequest(String url, ResponseDataMode responseDataMode, Map<String, String> formData, Object jsonData) throws Exception {
        //前期准备工作
        IHttpRequester httpRequester = prepareHttpRequester(url, HttpMethod.POST,formData,jsonData);
        httpRequester.setUploadFile(submitFileMap);
        //发起请求
        ResponseResult originResponse = httpRequester.postFile(url,responseDataMode);
        //请求后的回调
        if(apiRequestListener!=null) {
            apiRequestListener.afterRequest(httpRequester, HttpMethod.POST,url,originResponse);
        }
        //解析并组装响应对象
        R apiResponse = parseFileResponse(originResponse,responseDataMode);
        submitFileMap.clear();//重置上传文件map
        return apiResponse;
    }

    //==================================================================================================================

    /**
     * 解析原始响应 - 上传文件版
     * @param originResponse 原始响应对象
     * @return api响应结果
     */
    protected R parseFileResponse(ResponseResult originResponse, ResponseDataMode responseDataMode) throws Exception {
        Class<R> clazz = getResponseType();
        R apiResponse;
        //指定直接接收响应的输入流(保存输入流中的字节数据)
        if(responseDataMode==ResponseDataMode.STREAM_ONLY) {
            apiResponse = clazz.getDeclaredConstructor().newInstance();//调用ApiResponse的空参构造方法
            if(clazz!=ApiComResponse.class) {
                apiResponse.setResponseBytes(originResponse.getResponseStream());
            }
        }
        //指定传统的json响应
        else if(responseDataMode==ResponseDataMode.DEFAULT) {
            apiResponse = parseOriginResponse(originResponse);
        }
        //指定无响应体
        else {
            apiResponse = clazz.getDeclaredConstructor().newInstance();//调用ApiResponse的空参构造方法
        }
        apiResponse.setResponseCode(originResponse.getResponseCode());
        return apiResponse;
    }

    //==================================================================================================================

    public HashMap<String, File> getSubmitFileMap() {
        return submitFileMap;
    }

    public S setSubmitFile(HashMap<String, File> submitFileMap) {
        this.submitFileMap.putAll(submitFileMap);
        return super.self();
    }

    public S setSubmitFile(String fileKey, File file) {
        submitFileMap.put(fileKey,file);
        return super.self();
    }

    public S setSubmitFile(File file) {
        submitFileMap.put("file",file);
        return super.self();
    }

    public S setSubmitFile(List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            submitFileMap.put("f" + i, file);
        }
        return super.self();
    }

}
