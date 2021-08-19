package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.common.RequestMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;

import java.util.Map;

/**
 * Apache HttpClient的实现类
 * @since 2021/6/10
 */
public class ApacheHttpRequester implements IHttpRequester{

    private final  ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    @Override
    public ResponseResult get(String urlPath) throws Exception {
        return doRequest(RequestMethod.GET,urlPath);
    }

    @Override
    public ResponseResult post(String urlPath) throws Exception {
        return doRequest(RequestMethod.POST,urlPath);
    }

    @Override
    public ResponseResult postFile(String urlPath, ResponseDataMode dataMode) throws Exception {
        return null;
    }

    //==================================================================================================================

    /**
     * 构建请求对象（实际最终执行者）
     * @param method http方法
     * @param urlPath 请求的url
     * @return 请求对象（实际最终执行者）
     */
    //TODO 待完成(组织头信息、请求体等)
    private HttpRequest buildExecutor(RequestMethod method, String urlPath) throws Exception {
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        HttpRequest executor = requestFactory.buildGetRequest(new GenericUrl(urlPath));
        return executor;
    }

    /**
     * 执行请求
     * @param method http方法
     * @param urlPath 请求的url
     * @return 响应结果
     */
    private ResponseResult doRequest(RequestMethod method, String urlPath) throws Exception {
        //构建请求对象
        HttpRequest executor;
        try {
            executor = buildExecutor(method,urlPath);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        ResponseResult response = new ResponseResult();
        HttpResponse rawResponse;
        try {
            rawResponse = executor.execute();//发起请求
            //成功后的响应处理（响应码为2xx）
            response.setResponseCode(rawResponse.getStatusCode());
            response.setData(rawResponse.parseAsString());
            response.setContentEncoding(rawResponse.getContentEncoding());
            response.setContentType(rawResponse.getContentType());
        }catch (Exception e) {
            //失败后的响应处理（响应码不是2xx）
            if(e instanceof HttpResponseException) {
                HttpResponseException hre = (HttpResponseException) e;
                response.setResponseCode(hre.getStatusCode());
                response.setErrorMsg(hre.getStatusMessage());
                response.setData(hre.getContent());
            }
            else {
                response.setResponseCode(-1);
                response.setErrorMsg(e.toString());
            }
            e.printStackTrace();
        }
        return response;
    }

    //==================================================================================================================

    @Override
    public ApacheHttpRequester setRequestHeader(Map<String, String> headers) {
        return this;
    }

}
