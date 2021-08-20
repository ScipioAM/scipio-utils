package com.github.ScipioAM.scipio_utils_net.api_lib;

import com.github.ScipioAM.scipio_utils_net.http.IHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;

/**
 * API请求时的回调监听
 * @author Alan Scipio
 * @since 2021/7/14
 */
public interface ApiRequestListener {

    /**
     * 发起请求前的回调
     * @param httpRequester http客户端工具
     * @param httpMethod http方法
     * @param url 请求url
     * @param submitData 请求参数，视isJsonSubmit而定    <br/><br/>
     *                           <p>如果isJsonSubmit为true，则是String型的json数据</p>
     *                           <p>如果isJsonSubmit为false，则是HashMap型的表单数据 </p><br/>
     * @return 是否继续执行，返回true代表要继续执行
     */
    default boolean beforeRequest(IHttpRequester httpRequester, HttpMethod httpMethod, String url, boolean isJsonSubmit, Object submitData) {
        return true;
    }

    /**
     * 发起请求后的回调
     * @param httpRequester http客户端工具
     * @param httpMethod http方法
     * @param url 请求url
     * @param originResponse 原始响应对象，最后会被转化为{@link ApiResponse}
     */
    default void afterRequest(IHttpRequester httpRequester, HttpMethod httpMethod, String url, ResponseResult originResponse) { }

}
