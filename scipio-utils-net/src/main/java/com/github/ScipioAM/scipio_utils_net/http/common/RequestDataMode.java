package com.github.ScipioAM.scipio_utils_net.http.common;

import com.github.ScipioAM.scipio_utils_net.MimeType;

/**
 * 请求数据的模式
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2020/9/24
 */
public enum RequestDataMode {

    FORM(null),//传统传参方式(比如x=1&y=2...)
    TEXT_JSON(MimeType.APPLICATION_JSON.value),//直接输出json数据
    TEXT_XML(MimeType.TEXT_XML.value),//直接输出xml数据
    TEXT_PLAIN(MimeType.TEXT_PLAIN.value);//直接输出文本数据

    public final String contentType;

    RequestDataMode(String contentType) {
        this.contentType = contentType;
    }

    public static RequestDataMode getDefaultMode() {
        return FORM;
    }

}
