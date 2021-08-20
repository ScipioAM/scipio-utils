package com.github.ScipioAM.scipio_utils_net.http.common;

import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;

import java.io.IOException;

/**
 * 响应异常（可选辅助）
 * @author alan scipio
 * @since 2021/6/9
 */
public class ResponseException extends IOException {

    /**
     * HTTP响应码
     */
    private int responseCode;

    private ResponseResult content;

    public ResponseException(String message, ResponseResult content) {
        super(message);
        setContent(content);
    }

    public ResponseException(String message, Throwable cause, ResponseResult content) {
        super(message, cause);
        this.content = content;
    }

    public ResponseException(Throwable cause, ResponseResult content) {
        super(cause);
        this.content = content;
    }

    private void setContent(ResponseResult content) {
        if(content!=null) {
            this.responseCode = content.getResponseCode();
        }
        this.content = content;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public ResponseResult getContent() {
        return content;
    }
}
