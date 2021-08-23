package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.listener.*;

import javax.net.ssl.TrustManager;
import java.io.File;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 包装实现Java自带的HTTP客户端
 * @author Alan Scipo
 * @since 2020/9/24
 */
public class HttpUtil extends AbstractHttpUtil implements IHttpRequester{

    @Override
    public HttpUtil setRequestJson(String content) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setJsonContent(content);
        return this;
    }

    @Override
    public HttpUtil setRequestXml(String content) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setXmlContent(content);
        return this;
    }

    @Override
    public HttpUtil setRequestForm(Map<String,String> content) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setFormContent(content);
        return this;
    }

    public HttpUtil addRequestForm(String key, String value) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.addFormContent(key, value);
        return this;
    }

    @Override
    public HttpUtil setRequestText(String content) {
        if(requestContent == null) {
            requestContent = new RequestContent();
        }
        requestContent.setTextContent(content);
        return this;
    }

    @Override
    public HttpUtil setUploadFile(Map<String,File> uploadFiles) {
        this.uploadFiles = uploadFiles;
        return this;
    }

    //==================================================================================================================

    /**
     * 设置请求和响应的编码字符集
     */
    @Override
    public HttpUtil setCharset(String charset) {
        requestInfo.setCharset(charset);
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
            requestInfo.setProxy(null);
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
            requestInfo.setProxy(new Proxy(Proxy.Type.HTTP,sa));
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
    @Override
    public HttpUtil setUserAgent(String userAgent) {
        requestInfo.setUserAgent(userAgent);
        return this;
    }

    /**
     * 设置默认userAgent
     */
    public HttpUtil setDefaultUserAgent() {
        requestInfo.setUserAgent(getDefaultUserAgent());
        return this;
    }

    /**
     * 设置自定义请求头参数
     */
    @Override
    public HttpUtil setRequestHeader(Map<String, String> headers) {
        requestInfo.setRequestHeaders(headers);
        return this;
    }

    public HttpUtil addRequestHeader(Map<String,String> headers) {
        if(requestInfo.getRequestHeaders()==null) {
            requestInfo.setRequestHeaders(headers);
        }
        else {
            requestInfo.getRequestHeaders().putAll(headers);
        }
        return this;
    }

    /**
     * 设置自定义请求头参数（仅一对数据）
     */
    public HttpUtil addRequestHeader(String key, String value) {
        if( key==null || "".equals(key) ) {
            return this;
        }
        if(requestInfo.getRequestHeaders()==null) {
            requestInfo.setRequestHeaders(new HashMap<>());
        }
        requestInfo.getRequestHeaders().put(key,value);
        return this;
    }

    /**
     * 设置followRedirects参数
     * 参数含义：是否关闭重定向以获取跳转后的真实地址
     */
    @Override
    public HttpUtil setFollowRedirects(boolean followRedirects) {
        requestInfo.setFollowRedirects(followRedirects);
        return this;
    }

    @Override
    public HttpUtil setResponseSuccessHandler(ResponseSuccessHandler responseSuccessHandler) {
        this.responseSuccessHandler = responseSuccessHandler;
        return this;
    }

    @Override
    public HttpUtil setResponseFailureHandler(ResponseFailureHandler responseFailureHandler) {
        this.responseFailureHandler = responseFailureHandler;
        return this;
    }

    @Override
    public HttpUtil setExecuteErrorHandler(ExecuteErrorHandler executeErrorHandler) {
        this.executeErrorHandler = executeErrorHandler;
        return this;
    }

    @Override
    public HttpUtil setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

    @Override
    public HttpUtil setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    @Override
    public HttpUtil setStartExecuteListener(StartExecuteListener startExecuteListener) {
        this.startExecuteListener = startExecuteListener;
        return this;
    }

    /**
     * 设置上传文件缓冲区大小
     */
    @Override
    public HttpUtil setFileBufferSize(Integer fileBufferSize) {
        requestInfo.setFileBufferSize(fileBufferSize);
        return this;
    }

    /**
     * 设置下载文件的全路径
     */
    @Override
    public HttpUtil setDownloadFilePath(String downloadFilePath) {
        requestInfo.setDownloadFilePath(downloadFilePath);
        return this;
    }

    @Override
    public HttpUtil setTrustManagers(TrustManager... trustManagers) {
        super.trustManagers = trustManagers;
        return this;
    }

    @Override
    public HttpUtil setSSLContextInitializer(SSLContextInitializer sslContextInitializer) {
        super.sslContextInitializer = sslContextInitializer;
        return this;
    }

    //==================================================================================================================

    /**
     * 设置对响应结果的测试处理
     */
    public void setHandleResponseForTest()
    {
        setResponseSuccessHandler((responseCode, result) ->
                System.out.println("=============== http request success:"+responseCode+" ===============")
        );
        setResponseFailureHandler((responseCode, result) -> {
                switch (responseCode) {
                    case 400:
                        System.out.println("Error:400 bad request");
                    case 404:
                        System.out.println("Error:404 not found");
                    case 500:
                        System.out.println("Error:500 Server error");
                    case 503:
                        System.out.println("Error:503 Server unavailable");
                    default:
                        System.out.println("Error:response code " + responseCode);
            }
        });
    }

}
