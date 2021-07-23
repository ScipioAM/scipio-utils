import com.github.ScipioAM.scipio_utils_net.catcher.bean.CatchResult;
import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;
import com.github.ScipioAM.scipio_utils_net.catcher.impl.JsoupCatchListener;
import com.github.ScipioAM.scipio_utils_net.catcher.impl.JsoupWebCatcher;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alan scipio
 * @since 2021/6/9
 */
public class CatcherTest {

    @Test
    public void testJsoupWebCatcher() {
        String url = "https://www.66law.cn/contractmodel/19737.aspx";

        JsoupWebCatcher catcher = new JsoupWebCatcher();
        try {
            WebInfo webInfo = catcher.setCatchListener(new TestCatchListener())
                    .singleCatch(url,"D:\\temp","test",".txt");
            System.out.println("\n\nURL: "+webInfo.getUrl());
            System.out.println("Web title: "+webInfo.getWebTitle());
            System.out.println("response code: "+webInfo.getResponseCode());
            System.out.println("success: "+webInfo.getSuccess());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 抓取的具体实现
     */
    private static class TestCatchListener implements JsoupCatchListener {

        @Override
        public void jsoupCatch(WebInfo webInfo, Document document) {
            CatchResult catchResult = new CatchResult();
            //抓取文本容器
            Element textContainer = document.getElementsByClass("reader-txt").first();
            if(textContainer==null) {
                throw new RuntimeException("catch empty for <div class='reader-txt'>");
            }
            Elements pTagList = textContainer.getElementsByTag("p");
            if(pTagList==null || pTagList.size()<=0) {
                throw new RuntimeException("catch empty for p tag");
            }

            //抓取主内容
            List<String> contentList = new ArrayList<>();
            for(Element pTag : pTagList) {
                String content = pTag.text();
                contentList.add(content);
                System.out.println(content);
            }

            //保存结果
            catchResult.setResultStrList(contentList);
            webInfo.setCatchResult(catchResult);
            webInfo.setSuccess(true);
        }

    }//end of class TestCatchListener

}
