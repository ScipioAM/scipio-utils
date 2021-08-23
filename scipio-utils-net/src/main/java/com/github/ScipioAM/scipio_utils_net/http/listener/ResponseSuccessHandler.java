package com.github.ScipioAM.scipio_utils_net.http.listener;

import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;

/**
 * 成功响应后的处理器
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
@FunctionalInterface
public interface ResponseSuccessHandler {

    /**
     * 成功响应后的处理（2xx响应码）
     * @param responseCode 响应码
     * @param result 响应结果
     */
    void handle(int responseCode, ResponseResult result);

}
