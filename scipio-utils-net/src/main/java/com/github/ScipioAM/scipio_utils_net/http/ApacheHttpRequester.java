package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.bean.ProgressFileContent;
import com.github.ScipioAM.scipio_utils_net.http.bean.ProgressMultipartContent;
import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestDataMode;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.github.ScipioAM.scipio_utils_net.http.listener.ApacheSSLFactoryInitializer;
import com.github.ScipioAM.scipio_utils_net.http.listener.SSLContextInitializer;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Apache HttpClient的实现类
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/6/10
 */
public class ApacheHttpRequester extends AbstractHttpBase{

    private ApacheHttpTransport httpTransport;

    private ApacheSSLFactoryInitializer sslFactoryInitializer = ApacheSSLFactoryInitializer.DEFAULT;

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

    public ApacheHttpRequester() {
        super();
        super.sslContextInitializer = SSLContextInitializer.APACHE_DEFAULT;
    }

    //==================================================================================================================

    /**
     * 检查并创建HttpTransport对象
     */
    private void checkAndBuildHttpTransport() throws Exception {
        if(httpTransport != null) {
            return;
        }
        if(super.sslContextInitializer == null) {
            super.sslContextInitializer = SSLContextInitializer.APACHE_DEFAULT;
        }
        if(sslFactoryInitializer == null) {
            sslFactoryInitializer = ApacheSSLFactoryInitializer.DEFAULT;
        }
        HttpClientBuilder httpClientBuilder = ApacheHttpTransport.newDefaultHttpClientBuilder();
        //创建SSL上下文（并指定证书）
        SSLContext sslContext = sslContextInitializer.build(super.trustManagers);
        //创建SSL工厂
        SSLConnectionSocketFactory factory = sslFactoryInitializer.build(sslContext);
        //绑定工厂
        httpClientBuilder.setSSLSocketFactory(factory);
        //构建HttpTransport对象
        HttpClient httpClient = httpClientBuilder.build();
        httpTransport = new ApacheHttpTransport(httpClient);
    }

    /**
     * 构建请求对象（实际最终执行者）
     * @param method http方法
     * @param urlPath 请求的url
     * @param requestContent 原始设置的请求内容（不包含上传文件）
     * @param uploadFiles 要上传文件
     * @return 请求对象（实际最终执行者）
     */
    private HttpRequest buildExecutor(String urlPath, HttpMethod method, RequestContent requestContent, Map<String,File> uploadFiles, ResponseDataMode responseDataMode) throws Exception {
        checkAndBuildHttpTransport();
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        HttpRequest executor;
        if(method == HttpMethod.GET) {
            executor = requestFactory.buildGetRequest(new GenericUrl(urlPath));
        }
        else {
            HttpContent content = transformContent(requestContent,uploadFiles);
            executor = requestFactory.buildPostRequest(new GenericUrl(urlPath), content);
        }
        executor.setFollowRedirects(super.requestInfo.isFollowRedirects());
        executor.setNumberOfRetries(retries);
        executor.setWriteTimeout(ioTimeout);
        executor.setReadTimeout(ioTimeout);

        if(super.requestInfo.getConnectTimeout() != null && super.requestInfo.getConnectTimeout() >= 0) {
            executor.setConnectTimeout(super.requestInfo.getConnectTimeout());
        }
        if(httpEncoding != null) {
            executor.setEncoding(httpEncoding);
        }
        if(httpHeaders != null) {
            executor.setHeaders(httpHeaders);
        }
        //可以设置为返回原始InputStream
        executor.setResponseReturnRawInputStream((responseDataMode == ResponseDataMode.STREAM_ONLY || responseDataMode == ResponseDataMode.DOWNLOAD_FILE));
        //设置响应成功或失败的监听器
        if(super.responseSuccessHandler != null || super.responseFailureHandler != null) {
            executor.setResponseInterceptor((rawResponse) -> {
                ResponseResult result = new ResponseResult();
                result.setResponseCode(rawResponse.getStatusCode());
                result.setContentEncoding(rawResponse.getContentEncoding());
                result.setContentType(rawResponse.getContentType());
                result.setData(rawResponse.parseAsString());
                if(rawResponse.isSuccessStatusCode()) {
                    //成功时的响应
                    if(super.responseSuccessHandler != null) {
                        super.responseSuccessHandler.handle(rawResponse.getStatusCode(), result);
                    }
                }
                else {
                    //失败时的响应
                    if(super.responseFailureHandler != null) {
                        result.setErrorMsg(rawResponse.getStatusMessage());
                    }
                }
            });
        }
        //设置执行前的监听回调
        if(super.startExecuteListener != null) {
            executor.setInterceptor((httpRequest)->
                    super.startExecuteListener.beforeExec(urlPath,method,requestInfo,requestContent,responseDataMode)
            );
        }
        return executor;
    }

    @Override
    protected ResponseResult doRequest(String urlPath, HttpMethod httpMethod, RequestContent requestContent, ResponseDataMode responseDataMode) throws IOException, IllegalArgumentException {
        if(executor == null) {
            //构建请求对象
            try {
                executor = buildExecutor(urlPath,httpMethod,requestContent,null,responseDataMode);
            }catch (Exception e) {
                e.printStackTrace();
                ResponseResult result = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
                result.setErrorMsg(e.toString());
                if(super.executeErrorHandler != null) {
                    super.executeErrorHandler.handle(urlPath,httpMethod,e);
                }
                return result;
            }
        }

        ResponseResult response = new ResponseResult();
        InputStream in = null;
        long contentLength = 0L;
        String encoding = null;
        HttpResponse rawResponse;
        try {
            rawResponse = executor.execute();//发起请求
            //成功后的响应处理（响应码为2xx）
            response.setResponseCode(rawResponse.getStatusCode());
            response.setContentEncoding(rawResponse.getContentEncoding());
            response.setContentType(rawResponse.getContentType());
            response.setHeaders(transformHeaders(rawResponse.getHeaders()));
            encoding = rawResponse.getHeaders().getContentEncoding();
            if(responseDataMode == ResponseDataMode.STREAM_ONLY || responseDataMode == ResponseDataMode.DOWNLOAD_FILE) {
                in = rawResponse.getContent();
                contentLength = rawResponse.getHeaders().getContentLength();
                response.setResponseStream(in);
            }
            else {
                response.setData(rawResponse.parseAsString());
            }
        }catch (Exception e) {
            e.printStackTrace();
            //失败后的响应处理（响应码不是2xx）
            if(e instanceof HttpResponseException) {
                HttpResponseException hre = (HttpResponseException) e;
                response.setResponseCode(hre.getStatusCode());
                response.setErrorMsg(hre.getStatusMessage());
                response.setData(hre.getContent());
                response.setContentType(hre.getHeaders().getContentType());
                response.setContentLength(hre.getHeaders().getContentLength());
                response.setContentEncoding(hre.getHeaders().getContentEncoding());
                response.setHeaders(transformHeaders(hre.getHeaders()));
            }
            else {
                response.setResponseCode(ResponseResult.EXECUTE_ERR_CODE);
                response.setErrorMsg(e.toString());
            }
            if(super.executeErrorHandler != null) {
                super.executeErrorHandler.handle(urlPath,httpMethod,e);
            }
        }
        response = super.handleResponse(response,response.getResponseCode(),responseDataMode, in, contentLength, encoding);
        return response;
    }

    @Override
    protected ResponseResult doFileRequest(String urlPath, Map<String, String> requestParams, Map<String, File> uploadFiles, ResponseDataMode responseDataMode) throws IOException, IllegalArgumentException {
        //构建请求对象
        try {
            executor = buildExecutor(urlPath,HttpMethod.POST,requestContent,uploadFiles,responseDataMode);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseResult result = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
            result.setErrorMsg(e.toString());
            if(super.executeErrorHandler != null) {
                super.executeErrorHandler.handle(urlPath,HttpMethod.POST,e);
            }
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
        return new ByteArrayContent(contentType,content.getBytes(super.requestInfo.getCharset()));
    }

    private MultipartContent buildMultipartContent(RequestContent requestContent, Map<String, File> uploadFiles) {
        ProgressMultipartContent allContent = new ProgressMultipartContent()
                .setMediaType(new HttpMediaType("multipart/form-data")
                .setParameter("boundary", BOUNDARY))
                .setUploadListener(super.uploadListener);
        //添加参数
        Map<String,String> params = requestContent.getFormContent();
        if(params != null && params.size() > 0) {
            for (Map.Entry<String,String> param : params.entrySet()) {
                MultipartContent.Part paramsPart = new MultipartContent.Part(
                        new ByteArrayContent(null, param.getValue().getBytes()));
                paramsPart.setHeaders(new HttpHeaders().set(
                        "Content-Disposition", String.format("form-data; name=\"%s\"", param.getKey())));
                allContent.addPart(paramsPart);
            }
        }
        //添加文件
        for(Map.Entry<String,File> fileEntry : uploadFiles.entrySet()) {
            ProgressFileContent fileSubContent = new ProgressFileContent(
                    getContentTypeByFile(fileEntry.getValue()), fileEntry.getValue())
                    .setUploadListener(super.uploadListener);
            MultipartContent.Part filePart = new MultipartContent.Part(fileSubContent);
            filePart.setHeaders(new HttpHeaders().set(
                    "Content-Disposition",
                    String.format("form-data; name=\"%s\"; filename=\"%s\"", fileEntry.getKey(), fileEntry.getValue().getName())));
            allContent.addPart(filePart);
        }
        return allContent;
    }

    private Map<String,String> transformHeaders(HttpHeaders originalHeaders) {
        Map<String,String> finalHeaders = new HashMap<>();
        for(Map.Entry<String,Object> header : originalHeaders.entrySet()) {
            String value = (header.getValue() == null ? "" : header.getValue().toString());
            finalHeaders.put(header.getKey(),value);
        }
        return finalHeaders;
    }

    //==================================================================================================================

    public ApacheHttpRequester resetExecutor() {
        executor = null;
        return this;
    }

    public ApacheHttpRequester resetHttpTransport() {
        httpTransport = null;
        executor = null;
        return this;
    }

    public ApacheHttpRequester setHttpTransport(ApacheHttpTransport httpTransport) {
        this.httpTransport = httpTransport;
        executor = null;
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

    public ApacheHttpTransport getHttpTransport() {
        return httpTransport;
    }

    @Override
    public ApacheHttpRequester setTrustManagers(TrustManager... trustManagers) {
        super.trustManagers = trustManagers;
        return this;
    }

    public ApacheHttpRequester setSSLContextInitializer(SSLContextInitializer initializer) {
        super.sslContextInitializer = initializer;
        return this;
    }

    public void setSSLFactoryInitializer(ApacheSSLFactoryInitializer sslFactoryInitializer) {
        this.sslFactoryInitializer = sslFactoryInitializer;
    }

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
        super.requestInfo.setFileBufferSize(fileBufferSize);
        return this;
    }

    @Override
    public ApacheHttpRequester setDownloadFilePath(String downloadFilePath) {
        super.requestInfo.setDownloadFilePath(downloadFilePath);
        return this;
    }

    @Override
    public ApacheHttpRequester setFollowRedirects(boolean followRedirects) {
        super.requestInfo.setFollowRedirects(followRedirects);
        return this;
    }

    @Override
    public ApacheHttpRequester setUserAgent(String userAgent) {
        super.requestInfo.setUserAgent(userAgent);
        return this;
    }

    /** 设置默认User-Agent */
    public ApacheHttpRequester setDefaultUserAgent() {
        super.requestInfo.setUserAgent(getDefaultUserAgent());
        return this;
    }

    @Override
    public ApacheHttpRequester setCharset(String charset) {
        super.requestInfo.setCharset(charset);
        return this;
    }

    @Override
    public ApacheHttpRequester setResponseDataMode(ResponseDataMode responseDataMode) {
        requestInfo.setResponseDataMode(responseDataMode);
        return this;
    }

    /**
     * 用Fiddler监听java的请求需要设置
     */
    public ApacheHttpRequester setFiddlerProxy()
    {
        System.out.println("============ Set default Fiddler setting for java application ============");
        System.setProperty("proxyPort", "8888");
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxySet", "true");
        return this;
    }

}
