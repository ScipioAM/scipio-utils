package com.github.ScipioAM.scipio_utils_net.http;

/**
 * HTTP请求工具创建者
 * @author alan scipio
 * @since 2021/6/9
 */
public abstract class HttpRequesterBuilder {

    /**
     * 创建HTTP请求工具
     * @return 一个新的HTTP请求工具实例
     */
    public abstract IHttpRequester build();

    //=====================================================================================

//    public HttpRequesterBuilder setUserAgent()

}
