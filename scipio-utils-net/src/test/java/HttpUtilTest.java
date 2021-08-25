import com.github.ScipioAM.scipio_utils_net.http.HttpUtil;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.github.ScipioAM.scipio_utils_net.http.listener.DownloadListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.FileUploadListener;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

/**
 * Class: HttpTest
 * Description:
 * Author: Alan Min
 * Create Date: 2019/7/26
 */
public class HttpUtilTest {

    /**
     * 监听器测试：请求后的回调
     */
    @Test
    public void listenerTest()
    {
        String httpUrl="http://www.shuquge.com/";
        String httpsUrl="https://www.baidu.com/";

        HttpUtil httpUtil=new HttpUtil();
        httpUtil.setHandleResponseForTest();

        ResponseResult response = httpUtil.get(httpsUrl);
        System.out.println("响应码："+response.getResponseCode());
        if(response.getResponseCode()!=-1) {
            System.out.println("\n响应体数据：\n"+response.getData());
        }
    }

    /**
     * 测试jmimemagic对contentType的识别
     */
    @Test
    public void testContentType()
    {
        String filePath="D:\\创作资源\\彩色简洁扁平化新年工作计划PPT模板.pptx";
        File file=new File(filePath);

        String fileContentType;
        try {
            MagicMatch match = Magic.getMagicMatch(file,false);
            fileContentType=match.getMimeType();
            System.out.println(fileContentType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 测试remove.bg的api - AI智能抠图
     */
    @Test
    public void fileTest()
    {
        String originalFilePath="D:\\图库\\car001.jpg";
        String newFilePath="D:\\图库\\removebg_test0";
        String url="https://api.remove.bg/v1.0/removebg";
        String removebg_apiKey="***";

        HashMap<String,String> headParams=new HashMap<>();
        headParams.put("X-Api-Key",removebg_apiKey);

        HashMap<String,String> params=new HashMap<>();
        params.put("size","auto");
        HashMap<String,File> fileParams=new HashMap<>();
        fileParams.put("image_file",new File(originalFilePath));

        HttpUtil httpUtil=new HttpUtil();
        //响应后的回调
        httpUtil.setResponseSuccessHandler((responseCode, result) -> {
            System.out.println("对方响应结果：成功！开始写入响应返回的文件到本地");
            System.out.println("本地路径："+newFilePath);
        });
        httpUtil.setResponseFailureHandler((responseCode, result) ->
                System.out.println("对方响应结果：失败")
        );

        //发起请求的方法
        System.out.println("源文件："+originalFilePath);
        System.out.println("开始发起请求");
        ResponseResult response = httpUtil.setRequestForm(params)
//                .setFiddlerProxy()
                .setRequestHeader(headParams)
                .setUploadFile(fileParams)
                .setDownloadFilePath(newFilePath)//设置downloadFilePath后会自动设定响应数据模式为DOWNLOAD_FILE
                .setDownloadListener(DownloadListener.EMPTY_IMPL)
                .setUploadListener(FileUploadListener.EMPTY_IMPL)
                .setDownloadAutoExtension(true)//根据ContentType自动决定下载文件的扩展名
                .postFile(url);
        System.out.println("响应码："+response.getResponseCode());
        System.out.println("错误信息："+response.getErrorMsg());
    }

}
