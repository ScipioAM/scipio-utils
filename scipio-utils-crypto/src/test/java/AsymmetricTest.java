import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;
import com.github.ScipioAM.scipio_utils_crypto.mode.ACAlgorithm;
import org.junit.Test;

/**
 * Class: AsymmetricTest
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/28
 */
public class AsymmetricTest {

    @Test
    public void test00()
    {
        String content = "0987654321abcdefgzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        CryptoUtil cryptoUtil = new CryptoUtil();
        try {
            System.out.println("生成公钥和私钥");
            cryptoUtil.generateKeyPair(ACAlgorithm.RSA);
            System.out.println("原始明文："+content);
            String content_encrypt = cryptoUtil.encryptStrRSA(content);
            System.out.println("加密后的密文："+content_encrypt);
            String content_decrypt = cryptoUtil.decryptStrRSA(content_encrypt);
            System.out.println("解密后的明文："+content_decrypt);

            boolean isSuccess = cryptoUtil.outputKeyPairFile("E:\\aaa");
            System.out.println("保存公钥和私钥到本地文件："+isSuccess);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从本地文件读取私钥，非对称解密
     */
    @Test
    public void test01()
    {
        //私钥文件保存的字符串是:
        //MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMUfHMHzW6xsCOkSorp33du6e2gkpukpF1X+k1mL5E8QSAVHg5ySYDmZI7ho+j7mO5fAgv7UbTPXonfUR5niQnT7G+W5YWu7MhLMD3ex1vz0K/AGYvf8TaGeHjL7kWG+vlFN7dhZiJg+Zly+pGN5ecMrQyr82NLmPpfOlLpkLAb7AgMBAAECgYBp46WqFlVnkP1UXrtlmaBnOYyzTAUReQ5dFo0G7FvvkNpvY5Qylnr0VexXUqUjBsB6a2KzC1/CGpLe9fykkGOLy1Hgi564q3UQ9FLWSL4I7w+IvAAuWlwIs1mPw1zSTQjuLHv/q1wRvS2EVuD+YuOJ83+KgSrTaSyp/A5vRN3QCQJBAPVraaS5DtaDRsEykqKPamAGQS7Eys3BOhA//k6fFG7CucNxJ6o2Ur82dOt7rLSj+t9kUc+jJJ3ITWZRyZFLEd0CQQDNnqfNQUUbNTf1dN5m4R3jnbudX0nOerKPFJjjKPVgewbh033EDgw3RKmZG09ug2rGW84cd8Teco8iU5kJpCq3AkEAyivmJ1BbrNMQIm9q5IeIdfoxEDoFZ2JaVrCg6luaFXRMW3iH2GxP1j0iLCsBpv2+2PBnhMk8jM5JFJUjm5Ff8QJAM61XMP/hdWgXq0n3B1Y2o9klqPSk4Q73R3x+vEC0As6IuULlDdwV9+H62e8CNJ00vaufDoqPYPTbwl3OTZ0D2wJAN32J/UMSnkF1sl+/2ojVIV3y4Kf3ew/9YCK1pMpxmkUeW4PJltimEro7csEVODwUZCYvrOQSNG9DhPw37LfFgw==
        String originalContent = "0987654321abcdefg";
        String secretContent = "uQsEA0vJxppVmTZhb95NrYjcFMr2wjQWYgAp5nFwGj5/oVQbHLOTJMhHEA8B0g0uvLm3KZePgrvCw70w9caDBLacy3Tz39bsBLDvt9LCu4YXa9NuZ27F0IJoqGXpEI+EbyEoXmP8FKhfeOpI9pruUsOdCPj5lIUuLc0zWR8MhNs=";
        CryptoUtil cryptoUtil = new CryptoUtil();
        try {
            System.out.println("原始密文："+secretContent);
            cryptoUtil.getPrivateKeyFromFile("E:\\aaa\\privateKey", ACAlgorithm.RSA);
            System.out.println("读取私钥文件完成");
            String content_decrypt = cryptoUtil.decryptStrRSA(secretContent);
            System.out.println("解密后的明文："+content_decrypt);
            System.out.println("原始明文与解密后的明文比对："+originalContent.equals(content_decrypt));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
