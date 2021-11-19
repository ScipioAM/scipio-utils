package com.github.ScipioAM.scipio_utils_crypto.utils;

import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;

/**
 * AES加解密快捷静态工具类
 * @author Alan Scipio
 * @since 1.0.9
 * @date 2021/11/19
 */
public class AESUtil {

    private static final CryptoUtil cryptoUtil = new CryptoUtil();

    public static String encrypt(String plaintext, String key) throws Exception {
        return cryptoUtil.encryptStrAES(plaintext, key);
    }

    public static String decrypt(String ciphertext, String key) throws Exception {
        return cryptoUtil.decryptStrAES(ciphertext, key);
    }

    public static byte[] encryptBytes(String plaintext, String key) throws Exception {
        return cryptoUtil.encryptBytesAES(plaintext.getBytes(), key);
    }

    public static byte[] decryptBytes(String ciphertext, String key) throws Exception {
        return cryptoUtil.decryptBytesAES(ciphertext.getBytes(), key);
    }

    public static String encryptFromBytes(byte[] plaintext, String key) throws Exception {
        byte[] bytes = cryptoUtil.encryptBytesAES(plaintext, key);
        return new String(bytes);
    }

    public static String decryptFromBytes(byte[] ciphertext, String key) throws Exception {
        byte[] bytes = cryptoUtil.decryptBytesAES(ciphertext, key);
        return new String(bytes);
    }

    public static String encryptFromBytes(byte[] plaintext, String key, String charset) throws Exception {
        byte[] bytes = cryptoUtil.encryptBytesAES(plaintext, key);
        return new String(bytes,charset);
    }

    public static String decryptFromBytes(byte[] ciphertext, String key, String charset) throws Exception {
        byte[] bytes = cryptoUtil.decryptBytesAES(ciphertext, key);
        return new String(bytes,charset);
    }

}
