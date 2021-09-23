package com.github.ScipioAM.scipio_utils_net.catcher;

import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;
import com.github.ScipioAM.scipio_utils_net.catcher.impl.FileIOListener;
import com.github.ScipioAM.scipio_utils_net.http.HttpUtil;
import com.github.ScipioAM.scipio_utils_net.http.IHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * web爬虫 - 易用性父类
 * @author alan scipio
 * @since 2021/6/9
 */
public abstract class WebCatcher {

    protected IHttpRequester httpRequester;
    protected HttpMethod httpMethod = HttpMethod.GET; //默认get方法

    /**
     * 单网页抓取
     *
     * @param url           网页url
     * @param catchListener 具体抓取逻辑实现的监听器
     * @param ioListener    抓取数据保存的监听器
     * @return 网页信息（包括抓取结果）
     */
    public abstract WebInfo singleCatch(String url, CatchListener catchListener, IOListener ioListener) throws Exception;

    /**
     * 单网页抓取 - 不做IO操作（保存数据）
     */
    public WebInfo singleCatch(String url, CatchListener catchListener) throws Exception {
        return singleCatch(url, catchListener, null);
    }

    /**
     * 单网页抓取 - TXT文件IO操作
     * <p>注意：webInfo.catchResult.resultStrList必须有值，只会将该list的内容写入文件</p>
     *
     * @param url           网页url
     * @param catchListener 具体抓取逻辑实现的监听器
     * @param filePath      结果集保存文件的路径
     * @param fileName      结果集保存文件的名称
     * @param fileSuffix    结果集保存文件的后缀
     * @return 网页信息（包括抓取结果）
     */
    public WebInfo singleCatch(String url, CatchListener catchListener, String filePath, String fileName, String fileSuffix) throws Exception {
        FileIOListener fileIOListener = new FileIOListener(filePath, fileName, fileSuffix);
        return singleCatch(url, catchListener, fileIOListener);
    }

    public WebInfo singleCatch(String url, CatchListener catchListener, String filePath, String fileName) throws Exception {
        FileIOListener fileIOListener = new FileIOListener(filePath, fileName);
        return singleCatch(url, catchListener, fileIOListener);
    }

    //=====================================================================================

    /**
     * 发起HTTP请求
     *
     * @param urlStr 请求的URL
     * @return 响应结果
     * @throws IOException URL格式错误或响应码不是2xx
     */
    public ResponseResult doHttpRequest(String urlStr) throws Exception {
        //准备HTTP请求工具（客户端工具）
        IHttpRequester httpRequester = getRequester();
        System.out.println("httpRequester has been built: " + httpRequester);
        //发起HTTP请求
        ResponseResult response;
        System.out.println("start send http request to [" + urlStr + "] by " + httpMethod.value + " method");
        response = (httpMethod == HttpMethod.GET) ? httpRequester.get(urlStr) : httpRequester.post(urlStr);
        if (response.getResponseCode() < 200 || response.getResponseCode() >= 400) {
            throw new ResponseException("response error code:" + response.getResponseCode(), response);
        }
        System.out.println("request successfully, http response code:" + response.getResponseCode());
        return response;
    }

    /**
     * 获取HTTP请求工具
     */
    private IHttpRequester getRequester() {
        if (httpRequester == null) { //未设置过则构建默认
            httpRequester = buildDefaultRequester();
        }
        return httpRequester;
    }

    /**
     * 构建默认HTTP请求工具
     * <p>子类可重写此方法以指定自己需要的默认HTTP请求工具</p>
     * @return 默认的HTTP请求工具
     */
    protected IHttpRequester buildDefaultRequester() {
        return new HttpUtil()
                .setDefaultUserAgent();
    }

    //=====================================================================================

    /**
     * 设置HTTP请求工具的实例
     * @param requester HTTP请求工具的实例
     * @return 本身(链式调用)
     */
    public WebCatcher resetHttpRequester(IHttpRequester requester) {
        this.httpRequester = requester;
        return this;
    }

    /**
     * 设置HTTP请求工具
     * @param requesterType HTTP请求工具的类型(根据此类型自动生成实例)
     * @return 本身(链式调用)
     * @throws Exception 自动生成实例失败(没有空参的构造方法)
     */
    public WebCatcher resetHttpRequester(Class<? extends IHttpRequester> requesterType) throws Exception {
        assert requesterType != null;
        httpRequester = requesterType.getDeclaredConstructor().newInstance();
        return this;
    }

    /**
     * 指定HTTP请求方法
     * @param httpMethod GET还是POST方法
     * @return 本身(链式调用)
     */
    public WebCatcher setRequestMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public IHttpRequester getHttpRequester() {
        return httpRequester;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * 自定义请求头
     * @param headers 请求头参数
     * @return 调用链
     */
    public WebCatcher setRequestHeader(Map<String,String> headers) {
        IHttpRequester requester = getRequester();
        requester.setRequestHeader(headers);
        return this;
    }

    /**
     * 自定义请求头 - 仅一对参数
     * @param headKey 参数key
     * @param headValue 参数value
     * @return 调用链
     */
    public WebCatcher setRequestHeader(String headKey, String headValue) {
        IHttpRequester requester = getRequester();
        requester.setRequestHeader(headKey, headValue);
        return this;
    }

    /**
     * 设置请求参数 - json数据
     * @param requestJsonData 请求的json数据
     * @return 调用链
     */
    public WebCatcher setRequestJson(String requestJsonData) {
        IHttpRequester requester = getRequester();
        requester.setRequestJson(requestJsonData);
        return this;
    }

    /**
     * 设置请求参数 - 传统表单
     * @param formData 请求的参数本身
     * @return 调用链
     */
    public WebCatcher setRequestForm(Map<String,String> formData) {
        IHttpRequester requester = getRequester();
        requester.setRequestForm(formData);
        return this;
    }

    /**
     * 设置请求参数 - xml数据
     * @param requestXmlData 请求的xml数据
     * @return 调用链
     */
    public WebCatcher setRequestXml(String requestXmlData) {
        IHttpRequester requester = getRequester();
        requester.setRequestXml(requestXmlData);
        return this;
    }

    public WebCatcher setRequestText(String requestText) {
        IHttpRequester requester = getRequester();
        requester.setRequestText(requestText);
        return this;
    }

    /**
     * 设置要提交的文件
     * @param uploadFiles 要提交的文件们
     * @return 调用链
     */
    public WebCatcher setUploadFile(Map<String, File> uploadFiles) {
        IHttpRequester requester = getRequester();
        requester.setUploadFile(uploadFiles);
        return this;
    }

    /**
     * 设置要提交的文件 - 单个文件
     * @param uploadFile 要提交的文件(key固定为"file")
     * @return 调用链
     */
    public WebCatcher setUploadFile(File uploadFile) {
        IHttpRequester requester = getRequester();
        requester.setUploadFile(uploadFile);
        return this;
    }

    /**
     * 设置下载文件的全路径
     */
    public WebCatcher setDownloadFilePath(String downloadFilePath) {
        IHttpRequester requester = getRequester();
        requester.setDownloadFilePath(downloadFilePath);
        return this;
    }

    /**
     * 设置followRedirects参数
     * <p>参数含义：是否追踪重定向以获取跳转后的真实地址</p>
     */
    public WebCatcher setFollowRedirects(boolean followRedirects) {
        IHttpRequester requester = getRequester();
        requester.setFollowRedirects(followRedirects);
        return this;
    }

    /**
     * 设置userAgent
     */
    public WebCatcher setUserAgent(String userAgent) {
        IHttpRequester requester = getRequester();
        requester.setUserAgent(userAgent);
        return this;
    }

    /**
     * 设置期望的数据响应模式
     */
    public WebCatcher setResponseDataMode(ResponseDataMode responseDataMode) {
        IHttpRequester requester = getRequester();
        requester.setResponseDataMode(responseDataMode);
        return this;
    }

}
