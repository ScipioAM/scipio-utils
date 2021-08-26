package com.github.ScipioAM.scipio_utils_net.catcher.impl;

import com.github.ScipioAM.scipio_utils_net.catcher.CatchListener;
import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 针对Jsoup的抓取实现
 * @author Alan Scipio
 * @since 1.0.0
 * @date 2021/6/9
 */
@FunctionalInterface
public interface JsoupCatchListener extends CatchListener {

    /**
     * jsoup实现下的抓取处理
     * @param webInfo 网页信息
     * @param document 解析后的jsoup文档对象
     */
    void jsoupCatch(WebInfo webInfo, Document document);

    @Override
    default void onCatch(WebInfo webInfo, Object... params) {
        Document document = (Document) params[JsoupWebCatcher.INDEX_DOCUMENT];
        //网页title标签
        Element titleTag = document.getElementsByTag("title").first();
        if(titleTag!=null) {
            String title = titleTag.text();
            webInfo.setWebTitle(title);
        }
        else {
            System.out.println("Html tag <title> is missing");
        }
        //自定义抓取逻辑
        System.out.println("start to do catch job with jsoup");
        jsoupCatch(webInfo,document);
        System.out.println("catch job finished");
    }

}
