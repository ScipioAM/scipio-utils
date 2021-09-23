import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;
import com.github.ScipioAM.scipio_utils_crypto.MessageDigestUtil;
import com.github.ScipioAM.scipio_utils_crypto.SimpleFileCrypto;
import com.github.ScipioAM.scipio_utils_crypto.mode.ACAlgorithm;
import com.github.ScipioAM.scipio_utils_crypto.mode.SCAlgorithm;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Class: FileTest
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/28
 */
public class FileTest {

    //源文件
    String srcFile = "C:\\Users\\a\\Pictures\\Uplay\\刺客信条：大革命\\刺客信条：大革命2019-4-30-22-33-21.jpg";
//    String srcFile = "D:\\MyVideos\\杂\\[Youtube]PAK 40  German 75mm anti tank gun.mp4";
//    String srcFile = "D:\\MyVideos\\杂\\江泽民与华莱士谈笑风生_中文字幕 - 20分钟精华版_.mkv";
//    String srcFile = "D:\\MyVideos\\电影\\毒液.HD.720p.中英双字幕.电影天堂.mkv";
//    String srcFile = "D:\\MyVideos\\电影\\X战警：逆转未来.特效中英字幕.X-Men.Days.of.Future.Past.2014.BD720P.X264.AAC.English&Mandarin.CHS-ENG.Mp4Ba.mp4";
    //加密文件
    String encFile = "D:\\enc.jpg";
    //解密文件
    String decFile = "D:\\dec.jpg";

    @Test
    public void testSimpleFileCrypto()
    {
        SimpleFileCrypto crypto = new SimpleFileCrypto();
        try {
            System.out.println("源文件是："+srcFile);
            crypto.generateRandomKey();
            System.out.println("已生成密钥："+crypto.getKey());
            crypto.encrypt(srcFile,encFile);
            System.out.println("已完成加密，加密文件是："+encFile);

//            crypto.setKey(306);
            crypto.decrypt(encFile,decFile);
            System.out.println("已完成解密，解密文件是："+decFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSymmetric()
    {
        CryptoUtil crypto = new CryptoUtil();
        try {
            System.out.println("源文件是："+srcFile);
            FileInputStream srcIn = new FileInputStream(srcFile);
            FileOutputStream encOut = new FileOutputStream(encFile);
            crypto.encryptStreamAES(srcIn,encOut);
            System.out.println("已完成加密，加密文件是："+encFile);
            boolean isSuccess = crypto.outputSymmetricKeyFile("D:\\","test1");
            System.out.println("保存密钥到本地文件："+isSuccess);

            System.out.println("加密文件是："+encFile);
            crypto.getSymmetricKeyFromFile("D:\\test1.key", SCAlgorithm.AES);
            System.out.println("从本地文件读取密钥完成");
            FileInputStream encIn = new FileInputStream(encFile);
            FileOutputStream decOut = new FileOutputStream(decFile);
            crypto.decryptStreamAES(encIn,decOut);
            System.out.println("已完成解密，解密文件是："+decFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAsymmetric()
    {
        CryptoUtil crypto = new CryptoUtil();
        //耗时很长时，设置进度监听器
//        File srcFileObj = new File(srcFile);
//        crypto.setProcessingListener(readCount -> {
//            double newPercent = ((readCount*1.0) / srcFileObj.length()) * 100.0;
//            System.out.println("处理进度: "+String.format("%.1f",newPercent)+"%");
//        });
        try {
            //生成公私钥，加密文件，保存公私钥到本地文件
//            System.out.println("源文件是："+srcFile);
//            crypto.generateKeyPair(ACAlgorithm.RSA);
//            System.out.println("已生成公私钥");
//            FileInputStream srcIn = new FileInputStream(new File(srcFile));
//            FileOutputStream encOut = new FileOutputStream(new File(encFile));
//            crypto.encryptStreamRSA(srcIn,encOut);
//            System.out.println("已完成加密，加密文件是："+encFile);
//            boolean isSuccess = crypto.outputKeyPairFile("D:\\");
//            System.out.println("保存公私钥到本地文件："+isSuccess);

            //用本地公钥文件加密源文件
//            System.out.println("\n源文件是："+srcFile);
//            crypto.getPublicKeyFromFile("D:\\publicKey.pub", ACAlgorithm.RSA);
//            System.out.println("从本地文件读取公钥完成");
//            FileInputStream in = new FileInputStream(new File(srcFile));
//            FileOutputStream out = new FileOutputStream(new File(encFile));
//            crypto.encryptStreamRSA(in,out);
//            System.out.println("已完成加密，加密文件是："+encFile);

            //用本地私钥解密文件
            System.out.println("\n加密文件是："+encFile);
            crypto.getPrivateKeyFromFile("D:\\publicKey-2.pub", ACAlgorithm.RSA);
            System.out.println("从本地文件读取私钥完成");
            FileInputStream in = new FileInputStream(encFile);
            FileOutputStream out = new FileOutputStream(decFile);
            crypto.decryptStreamRSA(in,out);
            System.out.println("已完成解密，解密文件是："+decFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMd5Checksum()
    {
        File file = new File("F:\\序列号.txt");
        MessageDigestUtil util = new MessageDigestUtil();
        try {
            System.out.println(util.getMd5Checksum(file));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMd5CheckFile()
    {
        MessageDigestUtil util = new MessageDigestUtil();
        File file0 = new File("F:\\序列号.txt");
        File file1 = new File("E:\\序列号.txt");
        try {
            System.out.println(util.compareFileMd5(file0,file1));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
