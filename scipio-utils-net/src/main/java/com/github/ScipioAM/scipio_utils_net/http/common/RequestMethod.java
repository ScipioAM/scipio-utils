package com.github.ScipioAM.scipio_utils_net.http.common;

/**
 * Class: RequestMethod
 * Description: HTTP请求的方式
 * Author: Alan Min
 * Create Date: 2020/9/24
 */
public enum RequestMethod {

    GET("GET"),
    POST("POST");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
