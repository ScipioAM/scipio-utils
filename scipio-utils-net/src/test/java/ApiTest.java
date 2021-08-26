import com.github.ScipioAM.scipio_utils_net.api_lib.ApiComResponse;
import com.github.ScipioAM.scipio_utils_net.api_lib.ApiUtil;
import com.github.ScipioAM.scipio_utils_net.api_lib.virus_total.VTApiUtil;
import com.github.ScipioAM.scipio_utils_net.api_lib.virus_total.bean.VTApiResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2021/7/14
 */
public class ApiTest {

    /**
     * 测试通用API工具
     */
    @Test
    public void testComApi() {
        String url = "https://www.virustotal.com/api/v3/urls";

        ApiUtil apiUtil = ApiUtil.newInstance()
                .setAuthenticationKey("x-apikey")
                .setAuthenticationValue("");

        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("url","https://www.cnblogs.com/zhonghan/p/8435459.html");

        try {
            ApiComResponse response = apiUtil.postRequest(url,requestParams);
            System.out.println("http response code: "+response.getResponseCode());
            System.out.println("original response data: "+response.getOriginData());
            System.out.println("\n\n\n"+response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试VirusTotal的API工具
     */
    @Test
    public void testVTApi_scan() {
        String url = "https://www.virustotal.com/api/v3/files";
        File file = new File("D:\\ProgramFiles\\jd-gui-windows-1.6.6\\jd-gui.exe");

        VTApiUtil vtApiUtil = new VTApiUtil();
        vtApiUtil.setAuthenticationValue("");

        try {
            VTApiResponse response = vtApiUtil.fileRequest(url,file);
            System.out.println("http response code: "+response.getResponseCode());
            System.out.println("original response data: "+response.getOriginData());
            System.out.println("\n\n\n"+response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
