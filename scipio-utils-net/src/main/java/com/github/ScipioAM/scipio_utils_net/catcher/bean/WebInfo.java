package com.github.ScipioAM.scipio_utils_net.catcher.bean;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网页抓取信息（包括抓取结果）
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2021/6/9
 */
public class WebInfo {

    /**
     * 网页的url
     */
    private URL url;

    /**
     * 是否成功的标志
     */
    private Boolean success;

    /**
     * 网页的HTTP请求码
     */
    private Integer responseCode;

    /**
     * 网页的html内容
     */
    private String html;

    /**
     * 网页title标签的内容
     */
    private String webTitle;

    /**
     * 抓取结果集
     */
    private CatchResult catchResult;

    public WebInfo() { }

    public WebInfo(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUrl(String urlStr) throws MalformedURLException {
        this.url = new URL(urlStr);
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public CatchResult getCatchResult() {
        return catchResult;
    }

    public void setCatchResult(CatchResult catchResult) {
        this.catchResult = catchResult;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "WebInfo{" +
                "url=" + url +
                ", success=" + success +
                ", responseCode=" + responseCode +
                ", html='" + html + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", catchResult=" + catchResult +
                '}';
    }
}
