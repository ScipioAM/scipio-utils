package com.github.ScipioAM.scipio_utils_net.http.listener;

import java.net.URLConnection;

/**
 * Interface: ResponseListener
 * Description: http请求后，响应时的回调（响应监听器）
 * Author: Alan Min
 * Create Date: 2019/7/26
 */
public interface ResponseListener {

    /**
     * 响应成功时的回调
     * @param responseCode 响应码，只有200是成功的
     * @param conn 整个http连接对象
     */
    void onSuccess(int responseCode, URLConnection conn);

    /**
     * 响应失败时的回调
     * @param responseCode 响应码，只有200是成功的
     * @param conn 整个http连接对象
     */
    void onFailure(int responseCode, URLConnection conn);

    /**
     * 响应期间抛出异常时的回调
     * @param e 异常对象
     */
    void onError(Exception e);

}
