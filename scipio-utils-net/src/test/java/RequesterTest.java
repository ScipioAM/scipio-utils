import com.github.ScipioAM.scipio_utils_net.http.ApacheHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.File;

/**
 * @since 2021/6/10
 */
public class RequesterTest {

    @Test
    public void test0() {
        try {
            //Creating SSLContextBuilder object
            SSLContextBuilder SSLBuilder = SSLContexts.custom();

            //Loading the Keystore file
            File file = new File("C:\\Program Files\\Java\\jdk1.8.0_162\\jre\\lib\\security\\cacerts.jks");
            SSLBuilder = SSLBuilder.loadTrustMaterial(file,
                    "changeit".toCharArray());

            //Building the SSLContext usiong the build() method
            SSLContext sslcontext = SSLBuilder.build();

            //Creating SSLConnectionSocketFactory object
            SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());

            //Creating HttpClientBuilder
            HttpClientBuilder clientbuilder = HttpClients.custom();

            //Setting the SSLConnectionSocketFactory
            clientbuilder.setSSLSocketFactory(sslConSocFactory);

            //Building the CloseableHttpClient
            CloseableHttpClient httpclient = clientbuilder.build();

            //Creating the HttpGet request
            HttpGet httpget = new HttpGet("https://example.com/");

            //Executing the request
            HttpResponse httpresponse = httpclient.execute(httpget);

            //printing the status line
            System.out.println(httpresponse.getStatusLine());

            //Retrieving the HttpEntity and displaying the no.of bytes read
            HttpEntity entity = httpresponse.getEntity();
            if (entity != null) {
                System.out.println(EntityUtils.toByteArray(entity).length);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHttpClient() {
        String url0 = "https://example.com/";

        ApacheHttpRequester requester = new ApacheHttpRequester()
                .setDefaultUserAgent();
        ResponseResult result = requester.get(url0);
        System.out.println("Response code: " + result.getResponseCode());
        System.out.println("Error message: " + result.getErrorMsg());
        System.out.println("Response data: " + result.getData());
    }

}
