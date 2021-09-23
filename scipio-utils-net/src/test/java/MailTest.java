import com.github.ScipioAM.scipio_utils_net.mail.*;
import org.junit.jupiter.api.Test;

/**
 * 邮件测试
 * @date 2021/8/19
 */
public class MailTest {

    private MailAccount initAccount() {
        return new MailAccount()
                .setDebug(true)
                .setSmtpHost("")
                .setSmtpPort(0)
                .setSslFlag(true)
                .setAuth(true)
                .setUsername("")
                .setPassword("");
    }

    private MailInfo initInfo() {
        return new MailInfo()
                .addTo("")
                .addAttachment("")
                .setSubject("Test-sender01")
                .setContent("这是一个测试信息");
    }

    @Test
    public void testDefault() {
        MailSender sender = new DefaultMailSender();
        try {
            MailSendResult result = sender.send(initAccount(),initInfo());
            result.print();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
