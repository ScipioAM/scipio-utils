import com.github.ScipioAM.scipio_utils_common.os.WindowsCmd;
import com.github.ScipioAM.scipio_utils_common.os.WindowsService;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceResult;
import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void test0()
    {
        String cmd = "sc query wuauserv";

        String result = WindowsCmd.execute(cmd);
        System.out.println(result);
    }

    @Test
    public void test1()
    {
        String str = "redis";

        WindowsService service = WindowsService.newInstance();
        WinServiceResult status = service.start(str);
        System.out.println(status);
    }

}
