package com.github.ScipioAM.scipio_utils_net.http.listener;

import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;

/**
 * 执行异常处理器
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
@FunctionalInterface
public interface ExecuteErrorHandler {

    /**
     * 执行期间抛异常时的处理（最外层的catch）
     * @param urlPath 请求的url
     * @param httpMethod HTTP请求方法（GET或POST）
     * @param ex 抛出的异常
     */
    void handle(String urlPath, HttpMethod httpMethod, Exception ex);

}
