package com.github.ScipioAM.scipio_utils_net.api_lib;

/**
 * API调用工具类 - 通用响应对象
 * @author Alan Scipio
 * @since 2021/7/14
 */
public class ApiUtil extends ApiUtilBaseEnhance<ApiComResponse,ApiUtil> {

    private ApiUtil() {}

    public static ApiUtil newInstance() {
        return new ApiUtil();
    }

}
