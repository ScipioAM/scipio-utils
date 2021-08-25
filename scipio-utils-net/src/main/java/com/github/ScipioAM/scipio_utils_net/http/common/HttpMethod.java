package com.github.ScipioAM.scipio_utils_net.http.common;

/**
 * HTTP请求的方式
 * @author  Alan Min
 * @since 1.0.0
 * @date  2020/9/24
 */
public enum HttpMethod {

    GET("GET"),
    POST("POST");

    public final String value;

    HttpMethod(String value) {
        this.value = value;
    }

}
