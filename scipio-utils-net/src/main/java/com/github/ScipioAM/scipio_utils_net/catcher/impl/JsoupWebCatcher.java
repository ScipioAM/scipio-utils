package com.github.ScipioAM.scipio_utils_net.catcher.impl;

import com.github.ScipioAM.scipio_utils_net.catcher.CatchListener;
import com.github.ScipioAM.scipio_utils_net.catcher.IOListener;
import com.github.ScipioAM.scipio_utils_net.catcher.WebCatcher;
import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

/**
 * Jsoup实现的web爬虫
 * @author alan scipio
 * @since 2021/6/9
 */
public class JsoupWebCatcher extends WebCatcher {

    //解析后的jsoup文档对象，在catchListener.onCatch()的params数组中的下标位置
    public static final int INDEX_DOCUMENT = 0;

    private JsoupCatchListener catchListener;

    /**
     * 单网页抓取
     * @param urlStr 网页url
     * @param catchListener 具体抓取逻辑实现的监听器
     * @param ioListener 抓取数据保存的监听器
     * @return 网页信息（包括抓取结果）
     */
    @Override
    public WebInfo singleCatch(String urlStr,
                               CatchListener catchListener,
                               IOListener ioListener) throws Exception
    {
        URL url = new URL(urlStr);
        WebInfo webInfo = new WebInfo(url);
        //发起HTTP请求
        ResponseResult response = doHttpRequest(urlStr);
        webInfo.setResponseCode(response.getResponseCode());
        webInfo.setHtml(response.getData());
        //解析HTML
        Document document = Jsoup.parse(response.getData());
        //执行抓取逻辑
        if(catchListener!=null) {
            if(catchListener instanceof JsoupCatchListener) {
                this.catchListener = (JsoupCatchListener) catchListener;
            }
            catchListener.onCatch(webInfo,document);
        }
        //执行IO逻辑(保存数据)
        if(ioListener!=null) {
            ioListener.onProcess(webInfo,document);
        }
        return webInfo;
    }

    public WebInfo singleCatch(String urlStr, IOListener ioListener) throws Exception {
        return singleCatch(urlStr,catchListener,ioListener);
    }

    public WebInfo singleCatch(String urlStr) throws Exception {
        return singleCatch(urlStr,catchListener,null);
    }

    public WebInfo singleCatch(String url, String filePath, String fileName, String fileSuffix) throws Exception {
        FileIOListener fileIOListener = new FileIOListener(filePath,fileName,fileSuffix);
        return singleCatch(url,catchListener,fileIOListener);
    }

    //=====================================================================================

    public JsoupWebCatcher setCatchListener(JsoupCatchListener catchListener) {
        this.catchListener = catchListener;
        return this;
    }

    public JsoupCatchListener getCatchListener() {
        return catchListener;
    }

}
