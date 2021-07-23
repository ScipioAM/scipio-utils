package com.github.ScipioAM.scipio_utils_net.http;

/**
 * 创建本模块实现的{@link HttpUtil} <br/>
 *
 * ({@link HttpUtil}的本质是包装实现Java自带的HTTP客户端)
 *
 * @author alan scipio
 * @since 2021/6/9
 */
public class HttpUtilBuilder extends HttpRequesterBuilder {

    private final HttpUtil httpUtil = new HttpUtil();

    private HttpUtilBuilder() {}

    public static HttpUtilBuilder builder() {
        return new HttpUtilBuilder();
    }

    @Override
    public IHttpRequester build() {
        return httpUtil;
    }

    //=====================================================================================

    /**
     * 设置默认的User-Agent
     */
    public HttpUtilBuilder setDefaultUserAgent() {
        httpUtil.setDefaultUserAgent();
        return this;
    }

}
