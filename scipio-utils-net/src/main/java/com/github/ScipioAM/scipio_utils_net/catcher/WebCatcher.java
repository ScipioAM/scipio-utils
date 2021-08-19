package com.github.ScipioAM.scipio_utils_net.catcher;

import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;
import com.github.ScipioAM.scipio_utils_net.catcher.impl.FileIOListener;
import com.github.ScipioAM.scipio_utils_net.http.HttpRequesterBuilder;
import com.github.ScipioAM.scipio_utils_net.http.HttpUtilBuilder;
import com.github.ScipioAM.scipio_utils_net.http.IHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseException;

import java.io.IOException;

/**
 * web爬虫
 *
 * @author alan scipio
 * @since 2021/6/9
 */
public abstract class WebCatcher {

    protected HttpRequesterBuilder httpRequesterBuilder;
    protected RequestMethod requestMethod = RequestMethod.GET; //默认get方法

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
     * 单网页抓取 - TXT文件IO操作 <br/>
     * 注意：webInfo.catchResult.resultStrList必须有值，只会将该list的内容写入文件
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
        IHttpRequester httpRequester = buildRequester();
        System.out.println("httpRequester has been built: " + httpRequester);
        //发起HTTP请求
        ResponseResult response;
        if (requestMethod == RequestMethod.GET) {
            System.out.println("start send http request to [" + urlStr + "] by get method");
            response = httpRequester.get(urlStr);
        } else {
            System.out.println("start send http request to [" + urlStr + "] by post method");
            response = httpRequester.post(urlStr);
        }
        if (response.getResponseCode() < 200 || response.getResponseCode() >= 300) {
            throw new ResponseException("response error code:" + response.getResponseCode(), response);
        }
        System.out.println("request successfully, http response code:" + response.getResponseCode());
        return response;
    }

    /**
     * 创建HTTP请求工具
     */
    private IHttpRequester buildRequester() {
        if (httpRequesterBuilder == null) {
            httpRequesterBuilder = HttpUtilBuilder.builder()
                    .setDefaultUserAgent();
        }
        return httpRequesterBuilder.build();
    }

    //=====================================================================================

    public void setHttpRequesterBuilder(HttpRequesterBuilder builder) {
        this.httpRequesterBuilder = builder;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

}
