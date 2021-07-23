package com.github.ScipioAM.scipio_utils_crypto;

import com.github.ScipioAM.scipio_utils_crypto.mode.ACAlgorithm;
import com.github.ScipioAM.scipio_utils_crypto.mode.Charset;
import com.github.ScipioAM.scipio_utils_io.Base64Util;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class: AsymmetricCrypto
 * Description: 非对称加解密
 * Author: Alan Min
 * Create Date: 2020/9/27
 */
public class AsymmetricCrypto extends AbstractCrypto{

    private final int MAX_ENCRYPT_BLOCK = 117;//加密字节块的最大长度
    private final int MAX_DECRYPT_BLOCK = 128;//解密字节块的最大长度

    private PrivateKey privateKey;//私钥
    private PublicKey publicKey;//公钥

    //---------------------------------------------------------------------------------

    /**
     * 字符串 公钥加密或私钥解密
     * @param algorithm 非对称的算法
     * @param content 待处理的字符串
     * @param charset 待处理字符串的字符集
     * @return 处理过的字符串
     */
    public String strCrypt(boolean isEncrypt, ACAlgorithm algorithm, String content, Charset charset) throws Exception
    {
        if(isEncrypt) {
            byte[] byte_content = content.getBytes(charset.getName());
            byte[] byte_encrypt = bytesCrypt(true,algorithm,byte_content);
            return Base64Util.encodeToStr(byte_encrypt);//返回Base64编码的加密结果
        }
        else {
            byte[] byte_decode = Base64Util.decodeToBytes(content);//将加密并编码后的内容解码成字节数组
            byte[] byte_content = bytesCrypt(false,algorithm,byte_decode);
            return new String(byte_content, charset.getName());
        }
    }

    /**
     * 字节 公钥加密或私钥解密
     * @param isEncrypt 是否为加密
     * @param algorithm 非对称的算法
     * @param contentBytes 要处理的字节数据
     * @return 处理过的字节数据
     */
    public byte[] bytesCrypt(boolean isEncrypt, ACAlgorithm algorithm, byte[] contentBytes) throws Exception
    {
        int BLOCK_SIZE;
        Cipher cipher = Cipher.getInstance(algorithm.getName());
        //加密
        if(isEncrypt) {
            checkPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            BLOCK_SIZE = MAX_ENCRYPT_BLOCK;
        }
        //解密
        else {
            checkPrivateKey();
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            BLOCK_SIZE = MAX_DECRYPT_BLOCK;
        }
        //对数据分段处理
        int inputLen = contentBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        long processedCount = 0L;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            while (inputLen - offSet > 0) {
                int processSize;//每次处理的实际字节长度
                if (inputLen - offSet > BLOCK_SIZE) {
                    cache = cipher.doFinal(contentBytes, offSet, BLOCK_SIZE);
                    processSize = BLOCK_SIZE;
                } else {
                    cache = cipher.doFinal(contentBytes, offSet, inputLen - offSet);
                    processSize = (inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                out.flush();
                i++;
                offSet = i * BLOCK_SIZE;

                processedCount += processSize;
                if(processingListener!=null) {
                    processingListener.onProcess(processedCount);
                }
            }
            return out.toByteArray();
        }
    }

    /**
     * 流 公钥加密或私钥解密
     * @param isEncrypt 是否为加密
     * @param algorithm 非对称的算法
     */
    public void streamCrypt(boolean isEncrypt, ACAlgorithm algorithm, InputStream in, OutputStream out) throws Exception
    {
        int count;
        //读文件
        byte[] readBytes;
        byte[] readBuffer = new byte[bufferSize];
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while( (count=in.read(readBuffer))>-1 ) {
                bos.write(readBuffer,0,count);
                bos.flush();
            }
            readBytes = bos.toByteArray();
        }finally {
            in.close();
        }
        //处理
        byte[] processedBytes = bytesCrypt(isEncrypt,algorithm,readBytes);
        //输出
        readBuffer = new byte[bufferSize];
        try (ByteArrayInputStream is = new ByteArrayInputStream(processedBytes)) {
            while ((count = is.read(readBuffer)) != -1) {  //读取
                out.write(readBuffer, 0, count);
                out.flush();
            }
        }finally {
            out.close();
        }
    }

    //---------------------------------------------------------------------------------

    /**
     * 生成私钥和公钥
     */
    public void generateKeyPair(ACAlgorithm algorithm, String userSeed) throws NoSuchAlgorithmException {
        KeyPairGenerator keygen;//构造密钥生成器
        try {
            keygen = KeyPairGenerator.getInstance(algorithm.getName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }
        SecureRandom secureRandom = getSecureRandom(userSeed);
        //初始化密钥生成器
        switch (algorithm)
        {
            case RSA:
                keygen.initialize(1024, secureRandom);break;
            case DSA:
                keygen.initialize(512, secureRandom);break;
        }
        //生成密钥对
        KeyPair keyPair = keygen.generateKeyPair();
        //生成私钥和公钥
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    /**
     * 从字符串还原私钥
     * @param algorithm 私钥字符串原本的算法
     * @param keyStr 私钥字符串
     * @throws InvalidKeySpecException 密钥字符串非法
     */
    public void setPrivateKey(ACAlgorithm algorithm, String keyStr) throws InvalidKeySpecException {
        byte[] key_decoded = Base64Util.decodeToBytes(keyStr);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
            PKCS8EncodedKeySpec pkSpec = new PKCS8EncodedKeySpec(key_decoded);
            privateKey = keyFactory.generatePrivate(pkSpec);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    /**
     * 从字符串还原公钥
     * @param algorithm 公钥字符串原本的算法
     * @param keyStr 公钥字符串
     * @throws InvalidKeySpecException 密钥字符串非法
     */
    public void setPublicKey(ACAlgorithm algorithm, String keyStr) throws InvalidKeySpecException {
        byte[] key_decoded = Base64Util.decodeToBytes(keyStr);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm.getName());
            X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(key_decoded);
            publicKey = keyFactory.generatePublic(pkSpec);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    /**
     * 检查公钥
     */
    private void checkPublicKey() {
        if(this.publicKey==null) {
            throw new IllegalArgumentException("public key is null");
        }
    }

    /**
     * 检查私钥
     */
    private void checkPrivateKey() {
        if(this.privateKey==null) {
            throw new IllegalArgumentException("private key is null");
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private void checkBufferSize(int MAX_SIZE) {
        if(bufferSize>MAX_SIZE)
            bufferSize = MAX_SIZE;
    }

}
