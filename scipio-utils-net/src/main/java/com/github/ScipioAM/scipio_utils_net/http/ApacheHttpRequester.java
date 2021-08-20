package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestDataMode;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Apache HttpClient的实现类
 * @author Alan Scipio
 * @since 2021/6/10
 */
public class ApacheHttpRequester extends AbstractHttpBase{

    private final  ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    /** 执行者 */
    private HttpRequest executor;

    /** 读写的超时时间，单位毫秒 */
    private Integer ioTimeout = 0;//0代表无超时，永远等待
    /** 重试次数，默认10次 */
    private Integer retries = HttpRequest.DEFAULT_NUMBER_OF_RETRIES;
    /** 响应体压缩编码，可选gzip压缩或自行实现 */
    private HttpEncoding httpEncoding = null;
    /** 执行者 */
    private HttpHeaders httpHeaders;

    //==================================================================================================================

    /**
     * 构建请求对象（实际最终执行者）
     * @param method http方法
     * @param urlPath 请求的url
     * @param requestContent 原始设置的请求内容（不包含上传文件）
     * @param uploadFiles 要上传文件
     * @return 请求对象（实际最终执行者）
     */
    private HttpRequest buildExecutor(String urlPath, HttpMethod method, RequestContent requestContent, Map<String,File> uploadFiles) throws Exception {
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        HttpRequest executor;
        if(method == HttpMethod.GET) {
            executor = requestFactory.buildGetRequest(new GenericUrl(urlPath));
        }
        else {
            HttpContent content = transformContent(requestContent,uploadFiles);
            executor = requestFactory.buildPostRequest(new GenericUrl(urlPath), content);
        }
        executor.setFollowRedirects(requestInfo.isFollowRedirects());
        executor.setNumberOfRetries(retries);
        executor.setWriteTimeout(ioTimeout);
        executor.setReadTimeout(ioTimeout);
        if(requestInfo.getConnectTimeout() != null && requestInfo.getConnectTimeout() >= 0) {
            executor.setConnectTimeout(requestInfo.getConnectTimeout());
        }
        if(httpEncoding != null) {
            executor.setEncoding(httpEncoding);
        }
        if(httpHeaders != null) {
            executor.setHeaders(httpHeaders);
        }
        //TODO 1.SSL的问题要解决
        //TODO 2.如果监听器不为null，则监听器需要绑定到对应的handler和interceptor上去。细化监听器？
        return executor;
    }

    @Override
    protected ResponseResult doRequest(String urlPath, HttpMethod httpMethod, RequestContent requestContent, ResponseDataMode responseDataMode) throws IllegalArgumentException {
        if(executor == null) {
            //构建请求对象
            try {
                executor = buildExecutor(urlPath,httpMethod,requestContent,null);
            }catch (Exception e) {
                e.printStackTrace();
                ResponseResult result = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
                result.setErrorMsg(e.toString());
                return result;
            }
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
                response.setResponseCode(ResponseResult.EXECUTE_ERR_CODE);
                response.setErrorMsg(e.toString());
            }
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected ResponseResult doFileRequest(String urlPath, Map<String, String> requestParams, Map<String, File> uploadFiles, ResponseDataMode responseDataMode) throws IOException, IllegalArgumentException {
        //构建请求对象
        try {
            executor = buildExecutor(urlPath,HttpMethod.POST,requestContent,uploadFiles);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseResult result = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
            result.setErrorMsg(e.toString());
            return result;
        }
        return doRequest(urlPath,HttpMethod.POST,requestContent,responseDataMode);
    }

    //==================================================================================================================

    private HttpContent transformContent(RequestContent requestContent, Map<String, File> uploadFiles) throws UnsupportedEncodingException {
        if(requestContent == null) {
            return null;
        }
        //常规内容
        return (uploadFiles == null || uploadFiles.size() <= 0) ? buildNormalContent(requestContent) : buildMultipartContent(requestContent, uploadFiles);
    }

    private ByteArrayContent buildNormalContent(RequestContent requestContent) throws UnsupportedEncodingException {
        String content = null;
        String contentType = null;
        RequestDataMode requestDataMode = requestContent.getMode();
        switch (requestDataMode) {
            case FORM:
                content = setContentForPost(requestContent.getFormContent());
                break;
            case TEXT_XML:
            case TEXT_JSON:
            case TEXT_PLAIN:
                contentType = requestDataMode.contentType;
                content = requestContent.getStrContent();
        }
        return new ByteArrayContent(contentType,content.getBytes(requestInfo.getCharset()));
    }

    private MultipartContent buildMultipartContent(RequestContent requestContent, Map<String, File> uploadFiles) {
        MultipartContent content = new MultipartContent().setMediaType(
                new HttpMediaType("multipart/form-data")
                        .setParameter("boundary", BOUNDARY));
        //添加参数
        Map<String,String> params = requestContent.getFormContent();
        if(params != null && params.size() > 0) {
            for (Map.Entry<String,String> param : params.entrySet()) {
                MultipartContent.Part part = new MultipartContent.Part(
                        new ByteArrayContent(null, param.getValue().getBytes()));
                part.setHeaders(new HttpHeaders().set(
                        "Content-Disposition", String.format("form-data; name=\"%s\"", param.getKey())));
                content.addPart(part);
            }
        }
        //添加文件
        for(Map.Entry<String,File> fileEntry : uploadFiles.entrySet()) {
            FileContent fileContent = new FileContent(
                    getContentTypeByFile(fileEntry.getValue()), fileEntry.getValue());
            MultipartContent.Part part = new MultipartContent.Part(fileContent);
            part.setHeaders(new HttpHeaders().set(
                    "Content-Disposition",
                    String.format("form-data; name=\"content\"; filename=\"%s\"", fileEntry.getKey())));
            content.addPart(part);
        }
        return content;
    }

    //==================================================================================================================

    @Override
    public ApacheHttpRequester setRequestHeader(Map<String, String> headers) {
        if(headers == null || headers.size() <= 0) {
            return this;
        }
        if(httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }
        for(Map.Entry<String,String> header : headers.entrySet()) {
            httpHeaders.set(header.getKey(),header.getValue());
        }
        return this;
    }

    @Override
    public ApacheHttpRequester setRequestHeader(String headKey, String headValue) {
        if(headKey == null || "".equals(headKey)) {
            return this;
        }
        if(httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.set(headKey,headValue);
        return this;
    }

    @Override
    public ApacheHttpRequester setRequestJson(String requestJsonData) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setJsonContent(requestJsonData);
        return this;
    }

    @Override
    public ApacheHttpRequester setRequestForm(Map<String, String> formData) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setFormContent(formData);
        return this;
    }

    public ApacheHttpRequester addRequestForm(String key, String value) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.addFormContent(key, value);
        return this;
    }

    @Override
    public ApacheHttpRequester setRequestXml(String requestXmlData) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setXmlContent(requestXmlData);
        return this;
    }

    @Override
    public ApacheHttpRequester setRequestText(String content) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setTextContent(content);
        return this;
    }

    @Override
    public ApacheHttpRequester setUploadFile(Map<String, File> uploadFiles) {
        this.uploadFiles = uploadFiles;
        return this;
    }

    @Override
    public ApacheHttpRequester setFileBufferSize(Integer fileBufferSize) {
        requestInfo.setFileBufferSize(fileBufferSize);
        return this;
    }

    @Override
    public ApacheHttpRequester setDownloadFilePath(String downloadFilePath) {
        requestInfo.setDownloadFilePath(downloadFilePath);
        return this;
    }

    @Override
    public ApacheHttpRequester setFollowRedirects(boolean followRedirects) {
        requestInfo.setFollowRedirects(followRedirects);
        return this;
    }

    @Override
    public ApacheHttpRequester setUserAgent(String userAgent) {
        requestInfo.setUserAgent(userAgent);
        return this;
    }

    @Override
    public ApacheHttpRequester setCharset(String charset) {
        requestInfo.setCharset(charset);
        return this;
    }

    public Integer getIoTimeout() {
        return ioTimeout;
    }

    public ApacheHttpRequester setIoTimeout(Integer ioTimeout) {
        this.ioTimeout = ioTimeout;
        return this;
    }

    public Integer getRetries() {
        return retries;
    }

    public ApacheHttpRequester setRetries(Integer retries) {
        this.retries = retries;
        return this;
    }

    public HttpEncoding getHttpEncoding() {
        return httpEncoding;
    }

    public ApacheHttpRequester setHttpEncoding(HttpEncoding httpEncoding) {
        this.httpEncoding = httpEncoding;
        return this;
    }

}
