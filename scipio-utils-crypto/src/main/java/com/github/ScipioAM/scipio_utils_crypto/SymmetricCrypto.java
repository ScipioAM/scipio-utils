package com.github.ScipioAM.scipio_utils_crypto;

import com.github.ScipioAM.scipio_utils_crypto.mode.Charset;
import com.github.ScipioAM.scipio_utils_crypto.mode.SCAlgorithm;
import com.github.ScipioAM.scipio_utils_io.Base64Util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Class: SymmetricCrypto
 * Description: 对称加解密
 * Author: Alan Min
 * Create Time: 2018/6/1
 */
public class SymmetricCrypto extends AbstractCrypto {

    private SecretKeySpec secretKey;//最终生成的密钥对象

    //---------------------------------------------------------------------------------

    /**
     * 字符串的加解密
     * @param algorithm 加解密的算法
     * @param userSeed 用户密钥（第一次必须，第二次可为空）
     * @param content 明文或密文
     * @param charset 明文或密文的字符编码
     * @param isEncrypt 是否为加密
     * @param algorithmParameterSpec 算法要用到的参数
     * @return 密文或明文
     */
    public String strCrypt(SCAlgorithm algorithm, String userSeed,
                           String content, Charset charset, boolean isEncrypt,
                           AlgorithmParameterSpec algorithmParameterSpec) throws Exception
    {
        String result;
        //加密
        if (isEncrypt) {
            byte[] byte_content = content.getBytes(charset.getName());
            byte[] byte_encrypt = bytesCrypt(true,algorithm,userSeed,byte_content,algorithmParameterSpec);
            result = Base64Util.encodeToStr(byte_encrypt);//返回Base64编码的加密结果
        }
        //解密
        else {
            byte[] byte_decode = Base64Util.decodeToBytes(content);//将加密并编码后的内容解码成字节数组
            byte[] byte_content = bytesCrypt(false,algorithm,userSeed,byte_decode,algorithmParameterSpec);
            result = new String(byte_content, charset.getName());
        }
        return result;
    }

    /**
     * 字节的加解密
     * @param isEncrypt 是否为加密
     * @param algorithm 解密的算法（需与加密时的一样）
     * @param userSeed 用户密钥（可为空）
     * @param bytes 要处理的字节数据
     * @return 处理过的字节数据
     */
    public byte[] bytesCrypt(boolean isEncrypt, SCAlgorithm algorithm, String userSeed, byte[] bytes,
                             AlgorithmParameterSpec algorithmParameterSpec)
            throws Exception
    {
        //检查密钥对象
        checkSavedKey(userSeed,algorithm);
        //根据密钥生成密码器
        Cipher cipher = Cipher.getInstance(algorithm.getName());
        //初始化密码器
        if(algorithmParameterSpec==null)
            cipher.init((isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), secretKey);
        else
            cipher.init((isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), secretKey,algorithmParameterSpec);
        //解密
        return cipher.doFinal(bytes);
    }

    /**
     * 流的加解密
     * @param isEncrypt 是否为加密
     * @param algorithm 加密算法
     * @param userSeed 用户密钥（可为空）
     * @param in 输入流
     * @param out 输出流
     */
    public void streamCrypt(boolean isEncrypt, SCAlgorithm algorithm, String userSeed, InputStream in, OutputStream out,
                            AlgorithmParameterSpec algorithmParameterSpec)
            throws Exception
    {
        //检查密钥对象
        checkSavedKey(userSeed,algorithm);
        //根据密钥生成密码器
        Cipher cipher = Cipher.getInstance(algorithm.getName());
        int count;
        long processedCount = 0;//已读取并处理的字节数
        byte[] cache = new byte[bufferSize];
        //加密
        if(isEncrypt) {
            //初始化密码器
            if(algorithmParameterSpec==null)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            else
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,algorithmParameterSpec);
            //开始加密
            try(CipherInputStream cin = new CipherInputStream(in,cipher)) {
                while ((count = cin.read(cache)) != -1) {
                    out.write(cache, 0, count);
                    out.flush();
                    processedCount += count;
                    if(processingListener!=null) {
                        processingListener.onProcess(processedCount);
                    }
                }//end while
            }finally {
                if(out!=null){
                    out.close();
                }
            }
        }
        //解密
        else {
            //初始化密码器
            if(algorithmParameterSpec==null)
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            else
                cipher.init(Cipher.DECRYPT_MODE, secretKey,algorithmParameterSpec);
            //开始解密
            try(CipherOutputStream cout = new CipherOutputStream(out,cipher)) {
                while ((count = in.read(cache)) != -1) {
                    cout.write(cache, 0, count);
                    cout.flush();
                    processedCount += count;
                    if(processingListener!=null) {
                        processingListener.onProcess(processedCount);
                    }
                }//end while
            }finally {
                if(in!=null){
                    in.close();
                }
            }
        }
    }//end doCryptStream()



    //---------------------------------------------------------------------------------

    /**
     * 设置密钥
     * @param userSeed 用户密钥，如果该项为空则生成随机密钥
     * @param algorithm 算法（用户密钥为空时，可为空）
     */
    public void setSecretKey(String userSeed, SCAlgorithm algorithm) throws NoSuchAlgorithmException {
        //获取原始密钥
        SecretKey original_key = generateOriginalKey(algorithm, userSeed);
        if(original_key!=null) {
            //生成最终密钥
            secretKey = new SecretKeySpec(original_key.getEncoded(), algorithm.getName());
        }
    }

    /**
     * 设置密钥
     * @param encodedKeyBytes 用户密钥（该项为空则对保存的密钥对象进行空值检查）
     * @param algorithm 算法（用户密钥为空时，可为空）
     */
    public void setSecretKey(byte[] encodedKeyBytes, SCAlgorithm algorithm) {
        secretKey = new SecretKeySpec(encodedKeyBytes, algorithm.getName());
    }

    /**
     * 检查保存的密钥，如果为null则生成
     * @param userSeed 用户密钥，如果该项为空则生成随机密钥
     * @param algorithm 算法（用户密钥为空时，可为空）
     */
    public void checkSavedKey(String userSeed, SCAlgorithm algorithm) throws NoSuchAlgorithmException {
        if(secretKey==null) {
            setSecretKey(userSeed,algorithm);
        }
        //覆盖新的密钥
        else if(userSeed!=null && !"".equals(userSeed)) {
            setSecretKey(userSeed, algorithm);
        }
    }

    /**
     * 生成原始密钥
     */
    private SecretKey generateOriginalKey(SCAlgorithm algorithm, String userSeed) throws NoSuchAlgorithmException {
        KeyGenerator keygen;//构造密钥生成器
        try {
            keygen = KeyGenerator.getInstance(algorithm.getName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        SecureRandom secureRandom = getSecureRandom(userSeed);
        //初始化密钥生成器
        switch (algorithm)
        {
            case AES:
                keygen.init(128, secureRandom);break;
            case DESEDE:
                keygen.init(168, secureRandom);break;
            case DES:
                keygen.init(56, secureRandom);break;
        }
        //生成密钥
        return keygen.generateKey();
    }

    //---------------------------------------------------------------------------------

    /**
     * 计算密文或明文总长，因为采用块加密，每块16字节
     * @param srcFile 加密的文件
     * @return 加密后的byte长度
     */
    public long getEncryptFileSize(File srcFile) {
        return (srcFile.length() / 16) * 16 + 16;
    }

    public SecretKeySpec getSecretKey() {
        return secretKey;
    }

}