package com.github.ScipioAM.scipio_utils_net.api_lib;

import com.github.ScipioAM.scipio_utils_common.reflect.TypeHelper;
import com.github.ScipioAM.scipio_utils_io.parser.GsonUtil;
import com.github.ScipioAM.scipio_utils_net.http.HttpRequesterBuilder;
import com.github.ScipioAM.scipio_utils_net.http.HttpUtilBuilder;
import com.github.ScipioAM.scipio_utils_net.http.IHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;

import java.util.HashMap;
import java.util.Map;

/**
 * API调用工具类
 * @param <R> API响应的具体子类，取Response之意
 * @param <S> 本父类的子类，取Self之意
 * @author Alan Scipio
 * @since 2021/7/14
 */
public abstract class ApiUtilBase<R extends ApiResponse, S extends ApiUtilBase<R,S>> {

    /**
     * API需要的验证信息key
     */
    protected String authenticationKey;

    /**
     * API需要的验证信息value
     */
    protected String authenticationValue;

    /**
     * 是否提交json格式的请求参数(仅post有效)
     * <p>true代表是提交json格式的请求参数</p>
     */
    protected boolean isJsonSubmit = false;

    /**
     * API请求时的回调监听
     */
    protected ApiRequestListener apiRequestListener;

    /**
     * HTTP请求工具建造者
     */
    private HttpRequesterBuilder httpRequesterBuilder;

    //==================================================================================================================

    /**
     * 发起get请求
     * @param url 请求url
     * @param formData 请求参数
     * @return 响应结果
     */
    public R getRequest(String url, Map<String, String> formData) throws Exception {
        return doApiRequest(url, RequestMethod.GET,formData,null);
    }

    /**
     * 发起get请求
     * @param url 请求url
     * @param formKey 单个请求参数的key
     * @param formValue 单个请求参数的value
     * @return 响应结果
     */
    public R getRequest(String url, String formKey, String formValue) throws Exception {
        Map<String,String> formData = new HashMap<>();
        formData.put(formKey,formValue);
        return doApiRequest(url,RequestMethod.GET,formData,null);
    }

    /**
     * 发起get请求(无请求参数)
     * @param url 请求url
     * @return 响应结果
     */
    public R getRequest(String url) throws Exception {
        return doApiRequest(url,RequestMethod.GET,null,null);
    }

    /**
     * 发起post请求
     * @param url 请求url
     * @param formData 请求参数
     * @return 响应结果
     */
    public R postRequest(String url, Map<String, String> formData) throws Exception {
        return doApiRequest(url,RequestMethod.POST,formData,null);
    }

    /**
     * 发起post请求
     * @param url 请求url
     * @param formKey 单个请求参数的key
     * @param formValue 单个请求参数的value
     * @return 响应结果
     */
    public R postRequest(String url, String formKey, String formValue) throws Exception {
        Map<String,String> formData = new HashMap<>();
        formData.put(formKey,formValue);
        return doApiRequest(url,RequestMethod.POST,formData,null);
    }

    public R postRequest(String url, Object jsonBean) throws Exception {
        isJsonSubmit = true;
        return doApiRequest(url,RequestMethod.POST,null,jsonBean);
    }

    //==================================================================================================================

    /**
     * 发起API请求
     * @param url 请求url
     * @param requestMethod http方法
     * @param formData 请求参数，传统表单格式，可为null
     * @param jsonData 请求参数，json格式，可为null
     * @return api响应结果
     * @throws Exception 新建ApiResponse实例失败
     */
    private R doApiRequest(String url, RequestMethod requestMethod, Map<String, String> formData, Object jsonData) throws Exception {
        //前期准备工作
        IHttpRequester httpRequester = prepareHttpRequester(url,requestMethod,formData,jsonData);
        //发起请求
        ResponseResult originResponse = requestAndGetOriginResponse(httpRequester,url,requestMethod);
        //请求后的回调
        if(apiRequestListener!=null) {
            apiRequestListener.afterRequest(httpRequester,requestMethod,url,originResponse);
        }
        //解析并组装响应对象
        R apiResponse = parseOriginResponse(originResponse);
        apiResponse.setResponseCode(originResponse.getResponseCode());
        return apiResponse;
    }

    //==================================================================================================================

    /**
     * 获取API响应的类型
     * <p>子类可重写此方法以明确指定类型，否则默认取子类的第1个泛型类型</p>
     */
    @SuppressWarnings("unchecked")
    protected Class<R> getResponseType() {
        return (Class<R>) TypeHelper.getGenericClass(this,0);
    }

    /**
     * 构建底层的HTTP请求工具
     * <p>如需替换工具的具体实现，请重写该方法</p>
     * @return HTTP请求工具
     */
    private IHttpRequester buildHttpRequester() {
        if(httpRequesterBuilder==null) {
            httpRequesterBuilder = HttpUtilBuilder.builder();
        }
        return httpRequesterBuilder.build();
    }

    /**
     * 前期准备工作
     * @param url 请求url
     * @param requestMethod http方法
     * @param formData 请求参数，传统表单格式，可为null
     * @param jsonData 请求参数，json格式，可为null
     * @return HTTP请求工具
     */
    protected IHttpRequester prepareHttpRequester(String url, RequestMethod requestMethod, Map<String, String> formData, Object jsonData) {
        IHttpRequester httpRequester = buildHttpRequester();
        //设置验证信息
        if((authenticationKey!=null && !"".equals(authenticationKey))) {
            httpRequester.setRequestHeader(authenticationKey,authenticationValue);
        }

        Object submitData; //供监听器使用的，请求参数对象
        HashMap<String,String> finalFormData = null;
        if(formData!=null) {
            finalFormData = ((formData instanceof HashMap) ? (HashMap<String, String>) formData : new HashMap<>(formData));
        }
        //json提交下的请求参数准备
        if(isJsonSubmit) {
            String submitJson = null;
            if(jsonData!=null) {
                submitJson = GsonUtil.toJson(jsonData);
            }
            else if(finalFormData!=null) { //如果指定json提交，但jsonData为空，而formData不为空，则序列化formData
                submitJson = GsonUtil.toJson(finalFormData);
            }
            httpRequester.setRequestJsonData(submitJson);
            submitData = submitJson;
        }
        //form提交下的请求参数准备
        else {
            httpRequester.setRequestFormData(finalFormData);
            submitData = finalFormData;
        }
        //请求前的回调
        if(apiRequestListener!=null) {
            if(!apiRequestListener.beforeRequest(httpRequester,requestMethod,url,isJsonSubmit,submitData)) {
                return null;
            }
        }
        return httpRequester;
    }

    /**
     * 发起请求并获得原始响应结果
     * @param httpRequester http客户端工具
     * @param url 请求的url
     * @param requestMethod http方法
     * @return 原始响应结果
     */
    protected ResponseResult requestAndGetOriginResponse(IHttpRequester httpRequester, String url, RequestMethod requestMethod) {
        return (requestMethod == RequestMethod.GET ? httpRequester.get(url) : httpRequester.post(url));
    }

    /**
     * 解析原始响应
     * @param originResponse 原始响应对象
     * @return api响应结果
     */
    @SuppressWarnings("unchecked")
    protected R parseOriginResponse(ResponseResult originResponse) throws Exception {
        R apiResponse;
        Class<R> clazz = getResponseType();
        if(originResponse.getResponseCode().equals(200)) {
            //解析响应返回的json
            if(clazz==ApiComResponse.class) {
                HashMap<String,String> content = GsonUtil.fromJson(originResponse.getData(),HashMap.class);
                apiResponse = clazz.getDeclaredConstructor(Map.class).newInstance(content);
            }
            else {
                apiResponse = GsonUtil.fromJson(originResponse.getData(), clazz);
            }
        }
        else {
            apiResponse = clazz.getDeclaredConstructor().newInstance();//调用ApiResponse的空参构造方法
        }
        apiResponse.setOriginData(originResponse.getData());
        return apiResponse;
    }

    //==================================================================================================================

    @SuppressWarnings("unchecked")
    protected S self() {
        return (S) this;
    }

    //==================================================================================================================

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public S setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        return this.self();
    }

    public String getAuthenticationValue() {
        return authenticationValue;
    }

    public S setAuthenticationValue(String authenticationValue) {
        this.authenticationValue = authenticationValue;
        return this.self();
    }

    public boolean isJsonSubmit() {
        return isJsonSubmit;
    }

    public S setJsonSubmit(boolean jsonSubmit) {
        isJsonSubmit = jsonSubmit;
        return this.self();
    }

    public S setApiRequestListener(ApiRequestListener apiRequestListener) {
        this.apiRequestListener = apiRequestListener;
        return this.self();
    }

    public S setHttpRequesterBuilder(HttpRequesterBuilder builder) {
        this.httpRequesterBuilder = builder;
        return this.self();
    }

}
