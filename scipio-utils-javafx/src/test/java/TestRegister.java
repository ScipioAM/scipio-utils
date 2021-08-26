import com.github.ScipioAM.scipio_utils_javafx.register.ActiveCode;
import com.github.ScipioAM.scipio_utils_javafx.register.RegisterMode;
import com.github.ScipioAM.scipio_utils_javafx.register.RegisterService;
import com.github.ScipioAM.scipio_utils_javafx.register.RegisterServiceImpl;
import org.junit.jupiter.api.Test;

/**
 * 测试注册相关功能
 * @author Alan Min
 * @since 2021/1/16
 */
public class TestRegister {

    private final RegisterService registerService = new RegisterServiceImpl();

    /**
     * 获取本机请求码
     */
    @Test
    public void testRequestCode()
    {
        String requestCode = registerService.getLocalRequestCode();
        System.out.println(requestCode);
    }

    /**
     * 根据请求码，生成激活码（密文）
     */
    @Test
    public void testGetActiveCode()
    {
        String requestCode = "";
        String activeCodeStr = registerService.createActiveCode(requestCode, RegisterMode.PERMANENT,null);
        System.out.println(activeCodeStr);
    }

    /**
     * 从激活码密文里解析出激活码对象
     */
    @Test
    public void testAnalyzeActiveCode()
    {
        String activeCodeStr = "SKE7vq21bFU2I2vqpiHt1dfaPmn1qTl5LiLfiE4gA3l7ugZwR1FILUr4P28qZuZB";

        ActiveCode code = registerService.analyzeActiveCode(activeCodeStr);
        System.out.println(code);
    }

}
