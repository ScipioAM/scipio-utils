package com.github.ScipioAM.scipio_utils_crypto;

import com.github.ScipioAM.scipio_utils_crypto.listener.OnProcessingListener;
import com.github.ScipioAM.scipio_utils_crypto.mode.*;
import com.github.ScipioAM.scipio_utils_io.Base64Util;
import com.github.ScipioAM.scipio_utils_io.FileUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

/**
 * Class: CryptoUtil
 * Description:
 * Author: Alan Min
 * Create Time: 2018/6/1
 */
public class CryptoUtil {

    private Charset charset;//字符集，默认UTF-8编码
    private String customSalt;//自定义的盐，为null但指定要加盐的话，就用内置的法则加盐

    private final SymmetricCrypto scObject;//对称加解密
    private final MessageDigestUtil mdObject;//信息摘要算法
    private final AsymmetricCrypto acObject;//非对称加密

    public CryptoUtil() {
        this(Charset.UTF_8);
    }

    public CryptoUtil(Charset charset) {
        this.charset = charset;
        scObject = new SymmetricCrypto();
        mdObject = new MessageDigestUtil();
        acObject = new AsymmetricCrypto();
        //解决java不支持AES/CBC/PKCS7Padding模式解密,采用第三方包的模式
        Security.addProvider(new BouncyCastleProvider());
    }

    //---------------------------------↓↓↓↓↓↓ 参数部分 ↓↓↓↓↓↓---------------------------------

    /**
     * 设置对称加解密的密钥
     *
     * @param userSeed  用户密钥（该项为空则随机生成一个密钥）
     * @param algorithm 对称加解密的算法
     */
    public CryptoUtil setSymmetricKey(String userSeed, SCAlgorithm algorithm) {
        try {
            scObject.setSecretKey(userSeed, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CryptoUtil setSymmetricKey(SCAlgorithm algorithm) {
        try {
            scObject.setSecretKey((String) null, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CryptoUtil setSymmetricKey(byte[] keyBytes, SCAlgorithm algorithm) {
        scObject.setSecretKey(keyBytes, algorithm);
        return this;
    }

    /**
     * 生成非对称的公钥和私钥
     *
     * @param algorithm 非对称的算法
     * @param userSeed  用户密钥种子（该项为空则随机生成一个密钥）
     */
    public CryptoUtil generateKeyPair(ACAlgorithm algorithm, String userSeed) {
        try {
            acObject.generateKeyPair(algorithm, userSeed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CryptoUtil generateKeyPair(ACAlgorithm algorithm) {
        try {
            acObject.generateKeyPair(algorithm, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 从字符串还原私钥
     *
     * @param algorithm 私钥字符串原本的算法
     * @param keyStr    私钥字符串
     */
    public void setPrivateKey(ACAlgorithm algorithm, String keyStr) throws InvalidKeySpecException {
        acObject.setPrivateKey(algorithm, keyStr);
    }

    /**
     * 从字符串还原公钥
     *
     * @param algorithm 公钥字符串原本的算法
     * @param keyStr    公钥字符串
     */
    public void setPublicKey(ACAlgorithm algorithm, String keyStr) throws InvalidKeySpecException {
        acObject.setPublicKey(algorithm, keyStr);
    }

    /**
     * 获取对称加密的密钥对象
     */
    public SecretKeySpec getSymmetricKey() {
        return scObject.getSecretKey();
    }

    /**
     * 获取非对称的公钥
     */
    public PublicKey getPublicKey() {
        return acObject.getPublicKey();
    }

    /**
     * 获取非对称的公钥编码后的字符串文本
     */
    public String getPublicKeyStr() {
        return Base64Util.encodeToStr(acObject.getPublicKey().getEncoded());
    }

    /**
     * 获取非对称的私钥
     */
    public PrivateKey getPrivateKey() {
        return acObject.getPrivateKey();
    }

    /**
     * 获取非对称的私钥编码后的字符串文本
     */
    public String getPrivateKeyStr() {
        return Base64Util.encodeToStr(acObject.getPrivateKey().getEncoded());
    }

    public String getCustomSalt() {
        return customSalt;
    }

    public CryptoUtil setCustomSalt(String customSalt) {
        this.customSalt = customSalt;
        mdObject.setCustomSalt(customSalt);
        scObject.setCustomSalt(customSalt);
        return this;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * 设置流处理时的监听器
     *
     * @param processingListener 流处理时的监听器
     */
    public CryptoUtil setProcessingListener(OnProcessingListener processingListener) {
        scObject.setProcessingListener(processingListener);
        acObject.setProcessingListener(processingListener);
        return this;
    }

    //---------------------------------↓↓↓↓↓↓ 对称加解密部分 ↓↓↓↓↓↓---------------------------------

    /**
     * 对称算法的字符串加密
     *
     * @param algorithm 对称算法
     * @param content   明文
     * @param userSeed  用户密钥
     * @param aps       算法需要用到的参数
     * @return 密文（base64编码）
     */
    public String encryptStr_symmetric(SCAlgorithm algorithm, String content, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        return scObject.strCrypt(algorithm, userSeed, content, charset, true, aps);
    }

    public String encryptStr_symmetric(SCAlgorithm algorithm, String content, String userSeed) throws Exception {
        return encryptStr_symmetric(algorithm, content, userSeed, null);
    }

    /**
     * 对称算法的字符串加密（随机密钥）
     */
    public String encryptStr_symmetric(SCAlgorithm algorithm, String content) throws Exception {
        return encryptStr_symmetric(algorithm, content, null, null);
    }

    /**
     * 对称算法的字节加密
     */
    public byte[] encryptBytes_symmetric(SCAlgorithm algorithm, byte[] content, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        return scObject.bytesCrypt(true, algorithm, userSeed, content, aps);
    }

    public byte[] encryptBytes_symmetric(SCAlgorithm algorithm, byte[] content, String userSeed) throws Exception {
        return encryptBytes_symmetric(algorithm, content, userSeed, null);
    }

    public byte[] encryptBytes_symmetric(SCAlgorithm algorithm, byte[] content) throws Exception {
        return encryptBytes_symmetric(algorithm, content, null, null);
    }

    /**
     * 对称算法的流加密
     * 从输入流读取数据进行加密，再由输出流输出
     */
    public void encryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        scObject.streamCrypt(true, algorithm, userSeed, in, out, aps);
    }

    public void encryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out, String userSeed) throws Exception {
        encryptStream_symmetric(algorithm, in, out, userSeed, null);
    }

    public void encryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out) throws Exception {
        encryptStream_symmetric(algorithm, in, out, null, null);
    }

    /**
     * AES字符串加密
     *
     * @param content  明文
     * @param userSeed 用户密钥
     * @return 密文（base64编码）
     */
    public String encryptStrAES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.AES, userSeed, content, charset, true, null);
    }

    /**
     * AES字符串加密（随机密钥）
     */
    public String encryptStrAES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.AES, null, content, charset, true, null);
    }

    /**
     * AES字节加密
     */
    public byte[] encryptBytesAES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.AES, userSeed, content, null);
    }

    public byte[] encryptBytesAES(byte[] content) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.AES, null, content, null);
    }

    /**
     * AES流加密
     * 从输入流读取数据进行加密，再由输出流输出
     */
    public void encryptStreamAES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.AES, userSeed, in, out, null);
    }

    public void encryptStreamAES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.AES, null, in, out, null);
    }

    /**
     * DES字符串加密
     */
    public String encryptStrDES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DES, userSeed, content, charset, true, null);
    }

    public String encryptStrDES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DES, null, content, charset, true, null);
    }

    /**
     * DES字节加密
     */
    public byte[] encryptBytesDES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.DES, userSeed, content, null);
    }

    public byte[] encryptBytesDES(byte[] content) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.DES, null, content, null);
    }

    /**
     * DES流加密
     */
    public void encryptStreamDES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.DES, userSeed, in, out, null);
    }

    public void encryptStreamDES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.DES, null, in, out, null);
    }

    /**
     * 3DES字符串加密
     */
    public String encryptStr3DES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DESEDE, userSeed, content, charset, true, null);
    }

    public String encryptStr3DES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DESEDE, null, content, charset, true, null);
    }

    /**
     * 3DES字节加密
     */
    public byte[] encryptBytes3DES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.DESEDE, userSeed, content, null);
    }

    public byte[] encryptBytes3DES(byte[] content) throws Exception {
        return scObject.bytesCrypt(true, SCAlgorithm.DESEDE, null, content, null);
    }

    /**
     * 3DES流加密
     */
    public void encryptStream3DES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.DESEDE, userSeed, in, out, null);
    }

    public void encryptStream3DES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(true, SCAlgorithm.DESEDE, null, in, out, null);
    }

    /**
     * 对称算法的字符串解密
     *
     * @param algorithm 对称算法
     * @param content   明文
     * @param userSeed  用户密钥
     * @param aps       算法需要用到的参数
     * @return 密文（base64编码）
     */
    public String decryptStr_symmetric(SCAlgorithm algorithm, String content, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        return scObject.strCrypt(algorithm, userSeed, content, charset, false, aps);
    }

    public String decryptStr_symmetric(SCAlgorithm algorithm, String content, String userSeed) throws Exception {
        return scObject.strCrypt(algorithm, userSeed, content, charset, false, null);
    }

    /**
     * 对称算法的字符串加密（随机密钥）
     */
    public String decryptStr_symmetric(SCAlgorithm algorithm, String content) throws Exception {
        return scObject.strCrypt(algorithm, null, content, charset, false, null);
    }

    /**
     * 对称算法的字节解密
     */
    public byte[] decryptBytes_symmetric(SCAlgorithm algorithm, byte[] content, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        return scObject.bytesCrypt(false, algorithm, userSeed, content, aps);
    }

    public byte[] decryptBytes_symmetric(SCAlgorithm algorithm, byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(false, algorithm, userSeed, content, null);
    }

    public byte[] decryptBytes_symmetric(SCAlgorithm algorithm, byte[] content) throws Exception {
        return scObject.bytesCrypt(false, algorithm, null, content, null);
    }

    /**
     * 对称算法的流解密
     */
    public void decryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out, String userSeed, AlgorithmParameterSpec aps) throws Exception {
        scObject.streamCrypt(false, algorithm, userSeed, in, out, aps);
    }

    public void decryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(false, algorithm, userSeed, in, out, null);
    }

    public void decryptStream_symmetric(SCAlgorithm algorithm, InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(false, algorithm, null, in, out, null);
    }

    /**
     * AES字符串解密
     *
     * @param content  密文（base64编码）
     * @param userSeed 用户密钥
     * @return 明文
     */
    public String decryptStrAES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.AES, userSeed, content, charset, false, null);
    }

    /**
     * AES字符串解密（随机密钥）
     */
    public String decryptStrAES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.AES, null, content, charset, false, null);
    }

    /**
     * AES字节解密
     */
    public byte[] decryptBytesAES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.AES, userSeed, content, null);
    }

    public byte[] decryptBytesAES(byte[] content) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.AES, null, content, null);
    }

    /**
     * AES流解密
     */
    public void decryptStreamAES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.AES, userSeed, in, out, null);
    }

    public void decryptStreamAES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.AES, null, in, out, null);
    }

    /**
     * DES字符串解密
     */
    public String decryptStrDES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DES, userSeed, content, charset, false, null);
    }

    public String decryptStrDES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DES, null, content, charset, false, null);
    }

    /**
     * DES字节解密
     */
    public byte[] decryptBytesDES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.DES, userSeed, content, null);
    }

    public byte[] decryptBytesDES(byte[] content) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.DES, null, content, null);
    }

    /**
     * DES流解密
     */
    public void decryptStreamDES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.DES, userSeed, in, out, null);
    }

    public void decryptStreamDES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.DES, null, in, out, null);
    }

    /**
     * 3DES字符串解密
     */
    public String decryptStr3DES(String content, String userSeed) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DESEDE, userSeed, content, charset, false, null);
    }

    public String decryptStr3DES(String content) throws Exception {
        return scObject.strCrypt(SCAlgorithm.DESEDE, null, content, charset, false, null);
    }

    /**
     * 3DES字节解密
     */
    public byte[] decryptBytes3DES(byte[] content, String userSeed) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.DESEDE, userSeed, content, null);
    }

    public byte[] decryptBytes3DES(byte[] content) throws Exception {
        return scObject.bytesCrypt(false, SCAlgorithm.DESEDE, null, content, null);
    }

    /**
     * 3DES流解密
     */
    public void decryptStream3DES(InputStream in, OutputStream out, String userSeed) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.DESEDE, userSeed, in, out, null);
    }

    public void decryptStream3DES(InputStream in, OutputStream out) throws Exception {
        scObject.streamCrypt(false, SCAlgorithm.DESEDE, null, in, out, null);
    }

    //---------------------------------↓↓↓↓↓↓ 信息摘要算法部分 ↓↓↓↓↓↓---------------------------------

    /**
     * MD5字符串加密
     *
     * @param content     明文
     * @param level       加盐层级
     * @param convertType 字节数组转16进制字符串的转换方式（位运算方式或StringBuilder格式化方式）
     * @return 密文
     */
    public String md5(String content, SaltLevel level, ConvertType convertType) {
        return mdObject.encode(MDAlgorithm.MD5, content, convertType, level);
    }

    /**
     * MD5字符串加密 - 默认位运算转换方式
     */
    public String md5(String content, SaltLevel level) {
        return mdObject.encode(MDAlgorithm.MD5, content, ConvertType.BYTE2HEX_BITWISE, level);
    }

    /**
     * MD5字符串加密 - 默认不加盐或加一层盐，以及位运算转换方式
     */
    public String md5(String content) {
        return mdObject.encode(MDAlgorithm.MD5, content, ConvertType.BYTE2HEX_BITWISE, mdObject.getDefaultSaltLevel());
    }

    /**
     * SHA-1字符串加密
     */
    public String sha_1(String content, SaltLevel level, ConvertType convertType) {
        return mdObject.encode(MDAlgorithm.SHA_1, content, convertType, level);
    }

    public String sha_1(String content, SaltLevel level) {
        return mdObject.encode(MDAlgorithm.SHA_1, content, ConvertType.BYTE2HEX_BITWISE, level);
    }

    public String sha_1(String content) {
        return mdObject.encode(MDAlgorithm.SHA_1, content, ConvertType.BYTE2HEX_BITWISE, mdObject.getDefaultSaltLevel());
    }

    /**
     * SHA-256字符串加密
     */
    public String sha_256(String content, SaltLevel level, ConvertType convertType) {
        return mdObject.encode(MDAlgorithm.SHA_256, content, convertType, level);
    }

    public String sha_256(String content, SaltLevel level) {
        return mdObject.encode(MDAlgorithm.SHA_256, content, ConvertType.BYTE2HEX_BITWISE, level);
    }

    public String sha_256(String content) {
        return mdObject.encode(MDAlgorithm.SHA_256, content, ConvertType.BYTE2HEX_BITWISE, mdObject.getDefaultSaltLevel());
    }

    //---------------------------------↓↓↓↓↓↓ 非对称加解密部分 ↓↓↓↓↓↓---------------------------------

    /**
     * RSA字符串加密
     *
     * @param content 明文
     * @return 密文
     */
    public String encryptStrRSA(String content) throws Exception {
        return acObject.strCrypt(true, ACAlgorithm.RSA, content, charset);
    }

    /**
     * RSA字符串解密
     */
    public String decryptStrRSA(String content) throws Exception {
        return acObject.strCrypt(false, ACAlgorithm.RSA, content, charset);
    }

    /**
     * RSA字节加密
     *
     * @param content 明文
     * @return 密文
     */
    public byte[] encryptBytesRSA(byte[] content) throws Exception {
        return acObject.bytesCrypt(true, ACAlgorithm.RSA, content);
    }

    /**
     * RSA字节解密
     */
    public byte[] decryptStrRSA(byte[] content) throws Exception {
        return acObject.bytesCrypt(false, ACAlgorithm.RSA, content);
    }

    /**
     * RSA流加密
     *
     * @param in  明文输入
     * @param out 密文输出
     */
    public void encryptStreamRSA(InputStream in, OutputStream out) throws Exception {
        acObject.streamCrypt(true, ACAlgorithm.RSA, in, out);
    }

    /**
     * RSA流加密
     *
     * @param in  密文输入
     * @param out 明文输出
     */
    public void decryptStreamRSA(InputStream in, OutputStream out) throws Exception {
        acObject.streamCrypt(false, ACAlgorithm.RSA, in, out);
    }

    //---------------------------------↓↓↓↓↓↓ 其他功能部分 ↓↓↓↓↓↓---------------------------------

    /**
     * 输出对称加密的密钥（base64编码）到本地文件
     *
     * @param path     路径
     * @param fileName 文件名
     * @return 是否成功
     */
    public boolean outputSymmetricKeyFile(String path, String fileName) {
        boolean isSuccess;
        try {
            String filePath = path + fileName + ".key";//加上文件后缀
            String key_encode = Base64Util.encodeToStr(scObject.getSecretKey().getEncoded());
            FileUtil.out2LocalFile(filePath, key_encode.getBytes());
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 从本地文件读取对称加密的密钥（base64编码）
     *
     * @param filePath  文件全路径
     * @param algorithm 该密钥属于什么对称加密的算法
     */
    public void getSymmetricKeyFromFile(String filePath, SCAlgorithm algorithm) throws IOException {
        String content_encode = FileUtil.readStrFromFile(filePath, charset.getName());
        byte[] content_bytes = Base64Util.decodeToBytes(content_encode);
        setSymmetricKey(content_bytes, algorithm);
    }

    /**
     * 输出非对称加密的公钥和私钥（base64编码）到本地文件
     *
     * @param publicKeyName  公钥文件名
     * @param privateKeyName 私钥文件名
     * @param path           路径
     * @return 是否成功
     */
    public boolean outputKeyPairFile(String path, String publicKeyName, String privateKeyName) {
        String privateKeyPath = path + File.separator + privateKeyName;
        String publicKeyPath = path + File.separator + publicKeyName;
        boolean isSuccess;
        try {
            String privateKey_encode = Base64Util.encodeToStr(acObject.getPrivateKey().getEncoded());
            String publicKey_encode = Base64Util.encodeToStr(acObject.getPublicKey().getEncoded());
            FileUtil.out2LocalFile(privateKeyPath, privateKey_encode.getBytes());
            FileUtil.out2LocalFile(publicKeyPath, publicKey_encode.getBytes());
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    public boolean outputKeyPairFile(String path) {
        return outputKeyPairFile(path, "privateKey", "publicKey.pub");
    }

    /**
     * 从本地文件读取非对称加密的私钥
     *
     * @param filePath  文件全路径
     * @param algorithm 密钥属于什么非对称加密的算法
     * @throws IOException             读取文件出错
     * @throws InvalidKeySpecException 密钥字符串非法
     */
    public void getPrivateKeyFromFile(String filePath, ACAlgorithm algorithm) throws IOException, InvalidKeySpecException {
        String content_encode = FileUtil.readStrFromFile(filePath, charset.getName());
        setPrivateKey(algorithm, content_encode);
    }

    /**
     * 从本地文件读取非对称加密的公钥
     *
     * @param filePath  文件全路径
     * @param algorithm 密钥属于什么非对称加密的算法
     * @throws IOException             读取文件出错
     * @throws InvalidKeySpecException 密钥字符串非法
     */
    public void getPublicKeyFromFile(String filePath, ACAlgorithm algorithm) throws IOException, InvalidKeySpecException {
        String content_encode = FileUtil.readStrFromFile(filePath, charset.getName());
        setPublicKey(algorithm, content_encode);
    }

    /**
     * 计算对称加密的密文，字节总长
     * 因为采用块加密，每块16字节
     *
     * @param srcFile 加密的文件
     * @return 加密后的byte长度
     */
    public long getSymmetricEncryptFileSize(File srcFile) {
        return scObject.getEncryptFileSize(srcFile);
    }

    /**
     * 获取默认加盐层数
     */
    public SaltLevel getDefaultSaleLevel() {
        return mdObject.getDefaultSaltLevel();
    }

}
