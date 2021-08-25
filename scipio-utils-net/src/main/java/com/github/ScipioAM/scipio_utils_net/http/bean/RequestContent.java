package com.github.ScipioAM.scipio_utils_net.http.bean;

import com.github.ScipioAM.scipio_utils_net.http.common.RequestDataMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求的内容（请求体）
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/20
 */
public class RequestContent {

    /** 请求数据的模式（获取对应的content） */
    protected RequestDataMode mode;

    /** 字符串内容（json、xml等） */
    private String strContent;

    /** 传统内容（例如x=1&y=2...） */
    private Map<String,String> formContent;

    /** [可选]请求内容的类型（为null则由RequestDataMode决定） */
    private String contentType;

    public RequestContent() { }

    public RequestContent(RequestDataMode mode) {
        this.mode = mode;
    }

    public RequestContent(RequestDataMode mode, String strContent) {
        this.mode = mode;
        this.strContent = strContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public RequestDataMode getMode() {
        return mode;
    }

    public void setMode(RequestDataMode mode) {
        this.mode = mode;
    }

    public String getStrContent() {
        return strContent;
    }

    public void setJsonContent(String strContent) {
        mode = RequestDataMode.TEXT_JSON;
        this.strContent = strContent;
    }

    public void setXmlContent(String strContent) {
        mode = RequestDataMode.TEXT_XML;
        this.strContent = strContent;
    }

    public void setTextContent(String strContent) {
        mode = RequestDataMode.TEXT_PLAIN;
        this.strContent = strContent;
    }

    public Map<String, String> getFormContent() {
        return formContent;
    }

    public void setFormContent(Map<String, String> formContent) {
        this.formContent = formContent;
    }

    public void addFormContent(String key, String value) {
        if(formContent == null) {
            formContent = new HashMap<>();
        }
        formContent.put(key, value);
    }
}
