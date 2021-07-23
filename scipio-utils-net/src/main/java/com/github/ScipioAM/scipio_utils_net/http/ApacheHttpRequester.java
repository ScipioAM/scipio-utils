package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;

import java.util.Map;

/**
 * Apache HttpClient的实现类
 * @since 2021/6/10
 */
public class ApacheHttpRequester implements IHttpRequester{

    private final  ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    //TODO 有待进一步探索和改造，目前只是一个最低限度的test demo
    @Override
    public ResponseResult get(String urlPath) {
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        try {
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(urlPath));
            System.out.println(request);

            ResponseResult response = new ResponseResult();

            String rawResponse = request.execute().parseAsString();
            response.setData(rawResponse);
            System.out.println(rawResponse);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseResult post(String urlPath) {
        return null;
    }

    @Override
    public ResponseResult postFile(String urlPath, ResponseDataMode dataMode) {
        return null;
    }

    @Override
    public ApacheHttpRequester setRequestHeader(Map<String, String> headers) {
        return this;
    }

}
