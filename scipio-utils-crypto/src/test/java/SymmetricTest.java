import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;
import com.github.ScipioAM.scipio_utils_crypto.mode.SCAlgorithm;
import org.junit.jupiter.api.Test;

/**
 * Class: SymmetricTest
 * Description: 对称加解密的相关测试
 * Author: Alan Min
 * Create Date: 2020/9/27
 */
public class SymmetricTest {

    /**
     * 对称加密、解密，及存储密钥到本地
     */
    @Test
    public void test00()
    {
        String content = "123asd___666666666666666666666";
        CryptoUtil cryptoUtil = new CryptoUtil();
        try {
            System.out.println("原始明文："+content);
            String content_encrypt = cryptoUtil.encryptStrAES(content);
            System.out.println("加密后的密文："+content_encrypt);
            String content_decrypt = cryptoUtil.decryptStrAES(content_encrypt);
            System.out.println("解密后的明文："+content_decrypt);

            boolean isSuccess = cryptoUtil.outputSymmetricKeyFile("E:\\","test1");
            System.out.println("保存密钥到本地文件："+isSuccess);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从本地文件读取密钥，对称解密
     */
    @Test
    public void test01()
    {
        //密钥文件保存的字符串是5v+QA2CZctW4TAthCImpnA==
        String originalContent = "123asd___666666666666666666666";
        String secretContent = "OrTyLVrSHLq+ZV+PLXgidfXPCKeEZzW7ZLuyzLo0wdY=";
        CryptoUtil cryptoUtil = new CryptoUtil();
        try {
            System.out.println("原始密文："+secretContent);
            cryptoUtil.getSymmetricKeyFromFile("E:\\test1.key", SCAlgorithm.AES);
            System.out.println("读取密钥文件完成");
            String content_decrypt = cryptoUtil.decryptStrAES(secretContent);
            System.out.println("解密后的明文："+content_decrypt);
            System.out.println("原始明文与解密后的明文比对："+originalContent.equals(content_decrypt));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
