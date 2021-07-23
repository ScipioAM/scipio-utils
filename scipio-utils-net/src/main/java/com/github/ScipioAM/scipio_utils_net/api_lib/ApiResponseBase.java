package com.github.ScipioAM.scipio_utils_net.api_lib;

/**
 * 调用API的响应结果 - 公共父类，方便子类的快速创建
 * @author Alan Scipio
 * @since 2021/7/14
 */
public abstract class ApiResponseBase implements ApiResponse{

    /**
     * API请求是否成功
     */
    protected boolean success;

    /**
     * HTTP响应码
     */
    protected int responseCode;

    /**
     * 响应体内容
     */
    protected String originData;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        success = (responseCode == 200);
    }

    @Override
    public String getOriginData() {
        return originData;
    }

    @Override
    public void setOriginData(String originData) {
        this.originData = originData;
    }

}
