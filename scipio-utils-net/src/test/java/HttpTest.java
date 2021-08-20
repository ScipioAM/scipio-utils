import com.github.ScipioAM.scipio_utils_net.http.ApacheHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.HttpUtil;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.listener.ResponseListener;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Class: HttpTest
 * Description:
 * Author: Alan Min
 * Create Date: 2019/7/26
 */
public class HttpTest {

    /**
     * 监听器测试：请求后的回调
     */
    @Test
    public void listenerTest()
    {
        String httpUrl="http://www.shuquge.com/";
        String httpsUrl="https://www.baidu.com/";

        HttpUtil httpUtil=new HttpUtil();
        httpUtil.setResponseListener(new ResponseListener() {
            @Override
            public void onSuccess(int responseCode, HttpURLConnection conn) {
                HttpsURLConnection httpsConn=(HttpsURLConnection)conn;
                System.out.println("=================== success:"+responseCode+" ===================");
                try {
                    System.out.println(httpsConn.getContent());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int responseCode, HttpURLConnection conn) {
                System.out.println("=================== failed:"+responseCode+" ===================");
            }
        });

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
        String originalFilePath="F:\\MyPicture\\壁纸\\游戏\\刺客信条\\Assassin's Creed (35).jpg";
        String newFilePath="C:\\Users\\a\\Desktop\\removebg_test0.jpg";
        String url="https://api.remove.bg/v1.0/removebg";
        String removebg_apiKey="********";

        HttpUtil httpUtil=new HttpUtil();
        httpUtil.setFiddlerProxy();

        HashMap<String,String> headParams=new HashMap<>();
        headParams.put("X-Api-Key",removebg_apiKey);
        httpUtil.setRequestHeader(headParams);

        HashMap<String,String> params=new HashMap<>();
        params.put("size","auto");
        HashMap<String,File> fileParams=new HashMap<>();
        fileParams.put("image_file",new File(originalFilePath));

        //响应后的回调
        httpUtil.setResponseListener(new ResponseListener() {
            @Override
            public void onSuccess(int i, HttpURLConnection urlConnection) {
                System.out.println("对方响应结果：成功！开始写入响应返回的文件到本地");
                System.out.println("本地路径："+newFilePath);
            }
            @Override
            public void onFailure(int i, HttpURLConnection urlConnection) {
                System.out.println("对方响应结果：失败");
            }
        });

        //发起请求的方法
        System.out.println("源文件："+originalFilePath);
        System.out.println("开始发起请求");
        ResponseResult response = httpUtil.setRequestForm(params)
                .setUploadFile(fileParams)
                .setDownloadFilePath(newFilePath)
                .postFile(url);
        System.out.println("响应码："+response.getResponseCode());
    }

    @Test
    public void testHttpClient() {
        String url0 = "http://www.baidu.com";

        ApacheHttpRequester requester = new ApacheHttpRequester();
        ResponseResult result = requester.get(url0);
        System.out.println(result);
    }

}
