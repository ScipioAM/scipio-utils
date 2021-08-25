package com.github.ScipioAM.scipio_utils_net.http.listener;

import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.bean.RequestInfo;
import com.github.ScipioAM.scipio_utils_net.http.common.HttpMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

/**
 * 开始执行请求前的监听器
 * @author  Alan Scipio
 * @since 1.0.0
 * @date 2021/8/23
 */
public interface StartExecuteListener {

    /**
     * 开始执行前
     * @param urlPath 请求的url
     * @param httpMethod 请求方法（GET还是POST）
     * @param requestInfo 请求头
     * @param requestContent 请求体
     * @param responseDataMode 期望的数据响应模式
     */
    void beforeExec(String urlPath,
                       HttpMethod httpMethod,
                       RequestInfo requestInfo,
                       RequestContent requestContent,
                       ResponseDataMode responseDataMode);

}
