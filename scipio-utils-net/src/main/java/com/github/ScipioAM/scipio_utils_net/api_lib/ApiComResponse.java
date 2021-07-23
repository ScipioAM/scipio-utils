package com.github.ScipioAM.scipio_utils_net.api_lib;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用API响应结果
 * @author Alan Scipio
 * @since 2021/7/14
 */
public class ApiComResponse extends HashMap<String,String> implements ApiResponse {

    public ApiComResponse() { }

    public ApiComResponse(Map<? extends String, ? extends String> m) {
        super(m);
    }

    @Override
    public boolean isSuccess() {
        String httpCode = get("httpCode");
        return "200".equals(httpCode);
    }

    public int getResponseCode() {
        String httpCode = get("httpCode");
        if(httpCode == null || "".equals(httpCode)) {
            return -1;
        }
        else {
            return Integer.parseInt(httpCode);
        }
    }

    public void setResponseCode(int responseCode) {
        put("httpCode",responseCode+"");
    }

    public String getOriginData() {
        return get("originData");
    }

    public void setOriginData(String originData) {
        put("originData",originData);
    }

}
