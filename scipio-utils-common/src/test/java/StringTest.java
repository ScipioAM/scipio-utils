import com.github.ScipioAM.scipio_utils_common.os.WindowsCmd;
import com.github.ScipioAM.scipio_utils_common.os.constants.WinServiceType;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class: StringTest
 * Description:
 * Author: Alan Min
 * Create Date: 2020/10/4
 */
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
        String str = "        TYPE               : e0  USER_SHARE_PROCESS INSTANCE";

        WinServiceType serviceType = WinServiceType.analyze(str);
        System.out.println(serviceType);
    }

}
