package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.common.*;
import com.github.ScipioAM.scipio_utils_net.http.listener.DownloadListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.ResponseListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.UploadListener;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 包装实现Java自带的HTTP客户端
 * @author Alan Scipo
 * @since 2020/9/24
 */
public class HttpUtil extends AbstractHttpUtil implements IHttpRequester{

    /**
     * 请求时发送的数据（为空则不发送）
     * 为json或xml的string字符串，或者HashMap形式的字符串键值对
     */
    private Object requestData;
    /**
     * 请求时上传的文件
     * 单个上传时为File对象，多个上传时为HashMap<String,File>键值对
     */
    private Object requestFile;
    /**
     * HTTP请求方法
     */
    private RequestDataMode requestDataMode = RequestDataMode.DEFAULT;

    //----------------------------------------------------------------------------------------------

    /**
     * GET请求
     * @param urlPath url路径
     * @param responseDataMode 接收响应数据的模式
     * @return 响应数据
     */
    public ResponseResult get(String urlPath, ResponseDataMode responseDataMode) {
        ResponseResult response;
        try {
            response = doRequest(urlPath, RequestMethod.GET,requestDataMode,responseDataMode,requestData);
        }catch (Exception e) {
            if(responseListener!=null)
                responseListener.onError(e);
            response = new ResponseResult(-1);
        }
        return response;
    }

    /**
     * GET请求（默认传参，默认响应）
     */
    @Override
    public ResponseResult get(String urlPath){
        return get(urlPath, ResponseDataMode.DEFAULT);
    }

    /**
     * POST请求
     * @param urlPath url路径
     * @param responseDataMode 接收响应数据的模式
     * @return 响应数据
     */
    public ResponseResult post(String urlPath, ResponseDataMode responseDataMode) {
        ResponseResult response;
        try {
            response = doRequest(urlPath, RequestMethod.POST,requestDataMode,responseDataMode,requestData);
        }catch (Exception e) {
            if(responseListener!=null)
                responseListener.onError(e);
            response = new ResponseResult(-1);
        }
        return response;
    }

    /**
     * POST请求（默认传参，默认响应）
     */
    @Override
    public ResponseResult post(String urlPath) {
        return post(urlPath, ResponseDataMode.DEFAULT);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * POST请求
     * @param urlPath url路径
     * @param responseDataMode 接收响应数据的模式
     * @return 响应数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public ResponseResult postFile(String urlPath, ResponseDataMode responseDataMode) {
        ResponseResult response;
        try {
            if(requestData instanceof HashMap)
                response = doFileRequest(urlPath, (HashMap<String,String>)requestData,requestFile,responseDataMode);
            else
                response = doFileRequest(urlPath, null,requestFile,responseDataMode);
        }catch (Exception e) {
            if(responseListener!=null)
                responseListener.onError(e);
            response = new ResponseResult(-1);
        }
        return response;
    }

    /**
     * POST请求（默认传参）
     */
    public ResponseResult postFile(String urlPath) {
        return postFile(urlPath, ResponseDataMode.DEFAULT);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public HttpUtil setRequestJsonData(String requestData) {
        this.requestData = requestData;
        this.requestDataMode = RequestDataMode.JSON;
        return this;
    }

    @Override
    public HttpUtil setRequestXmlData(String requestData) {
        this.requestData = requestData;
        this.requestDataMode = RequestDataMode.XML;
        return this;
    }

    @Override
    public HttpUtil setRequestFormData(Map<String,String> requestData) {
        this.requestData = ((requestData instanceof HashMap) ? requestData : new HashMap<>(requestData));
        this.requestDataMode = RequestDataMode.DEFAULT;
        return this;
    }

    @Override
    public HttpUtil setRequestFile(File requestFile) {
        this.requestFile = requestFile;
        return this;
    }

    @Override
    public HttpUtil setRequestFile(Map<String,File> requestFile) {
        this.requestFile = ((requestFile instanceof HashMap) ? requestFile : new HashMap<>(requestFile));
        return this;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 设置请求头的编码字符集
     */
    public HttpUtil setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     *  关闭代理
     * @param isGlobalSet 是否全局设置
     */
    public HttpUtil turnOffProxy(boolean isGlobalSet) {
        if(isGlobalSet) {
            System.setProperty("http.proxySet", "false");
            System.getProperties().remove("http.proxyHost");
            System.getProperties().remove("http.proxyPort");
            System.getProperties().remove("https.proxyHost");
            System.getProperties().remove("https.proxyPort");
        }
        else {
            proxy = null;
        }
        return this;
    }

    /**
     * 打开代理
     * @param isGlobalSet 是否全局设置
     * @param host 代理服务器地址
     * @param port 代理服务器端口
     */
    public HttpUtil turnOnProxy(boolean isGlobalSet,String host,String port) {
        if(isGlobalSet) {
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", port);
            System.setProperty("https.proxyHost", host);
            System.setProperty("https.proxyPort", port);
        }
        else {
            SocketAddress sa = new InetSocketAddress(host,Integer.parseInt(port));
            proxy = new Proxy(Proxy.Type.HTTP,sa);
        }
        return this;
    }

    /**
     * 用fiddler监听java的请求需要设置
     */
    public HttpUtil setFiddlerProxy()
    {
        System.out.println("============ Set default Fiddler setting for java application ============");
        System.setProperty("proxyPort", "8888");
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxySet", "true");
        return this;
    }

    /**
     * 设置userAgent
     */
    public HttpUtil setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * 设置默认userAgent
     */
    public HttpUtil setDefaultUserAgent() {
        this.userAgent = PresetUserAgent.UA_CHROME66_MAC;
        return this;
    }

    /**
     * 设置自定义请求头参数
     */
    @Override
    public HttpUtil setRequestHeader(Map<String, String> headers) {
        if(this.reqHeaderParam==null) {
            this.reqHeaderParam = headers;
        }
        else {
            this.reqHeaderParam.putAll(headers);
        }
        return this;
    }

    /**
     * 设置自定义请求头参数（仅一对数据）
     */
    @Override
    public HttpUtil setRequestHeader(String key, String value) {
        if( key==null || "".equals(key) ) {
            return this;
        }
        if(reqHeaderParam==null) {
            reqHeaderParam = new HashMap<>();
        }
        reqHeaderParam.put(key,value);
        return this;
    }

    /**
     * 设置followRedirects参数
     * 参数含义：是否关闭重定向以获取跳转后的真实地址
     */
    public HttpUtil setFollowRedirects(boolean followRedirects) {
        isFollowRedirects = followRedirects;
        return this;
    }

    /**
     * 设置响应监听器
     */
    public HttpUtil setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
        return this;
    }

    /**
     * 设置上传监听器
     */
    public HttpUtil setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

    /**
     * 设置下载监听器
     */
    public HttpUtil setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    /**
     * 设置上传文件缓冲区大小
     */
    public HttpUtil setFileBufferSize(Integer fileBufferSize) {
        this.fileBufferSize = fileBufferSize;
        return this;
    }

    /**
     * 设置下载文件的全路径
     */
    public HttpUtil setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
        return this;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 设置对响应结果的测试处理
     */
    public void enableHandleResponseForTest()
    {
        setResponseListener(new ResponseListener() {
            //成功时的处理
            @Override
            public void onSuccess(int responseCode, URLConnection conn) {
                System.out.println("=============== http request success:"+responseCode+" ===============");
            }
            //失败时的处理
            @Override
            public void onFailure(int responseCode, URLConnection conn) {
                switch (responseCode)
                {
                    case 400:
                        System.out.println("Error:400 bad request");
                    case 404:
                        System.out.println("Error:404 not found");
                    case 500:
                        System.out.println("Error:500 Server error");
                    case 503:
                        System.out.println("Error:503 Server unavailable");
                    default:
                        System.out.println("Error:response code "+responseCode);
                }
            }
            //异常时的处理
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
