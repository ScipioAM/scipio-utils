import com.github.ScipioAM.scipio_utils_common.os.WindowsCmd;
import com.github.ScipioAM.scipio_utils_common.os.WindowsService;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceOption;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceResult;
import org.junit.jupiter.api.Test;

public class WindowsTest {

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

    @Test
    public void testInstall()
    {
        WinServiceOption option = new WinServiceOption("a test")
                .setDisplayName("ATestName")
                .setBinPath("D:\\ProgramFiles\\cpu-z_1.96-cn\\cpuz_x64.exe")
                .setDescription("This is a test");

        WinServiceResult result = WindowsService.newInstance()
                .install(option);
        System.out.println(result);
    }

    @Test
    public void testUninstall()
    {
        WinServiceResult result = WindowsService.newInstance()
                .uninstall("a test");
        System.out.println(result);
    }

}
