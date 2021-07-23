import com.github.ScipioAM.scipio_utils_net.http.ApacheHttpRequester;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import org.junit.Test;

/**
 * @since 2021/6/10
 */
public class RequesterTest {

    @Test
    public void test0()
    {
        ApacheHttpRequester httpRequester = new ApacheHttpRequester();
        ResponseResult response = httpRequester.get("https://www.baeldung.com/google-http-client");
        System.out.println(response);
    }

}
