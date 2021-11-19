package com.github.ScipioAM.scipio_utils_crypto.utils;

import com.github.ScipioAM.scipio_utils_crypto.CryptoUtil;
import com.github.ScipioAM.scipio_utils_crypto.mode.ConvertType;
import com.github.ScipioAM.scipio_utils_crypto.mode.SaltLevel;

/**
 * MD5加密快捷静态工具类
 * @author Alan Scipio
 * @since 1.0.9
 * @date 2021/11/19
 */
public class MD5Util {

    private static final CryptoUtil cryptoUtil = new CryptoUtil();

    /**
     * md5加密
     * @param plaintext 明文
     * @return 密文
     */
    public static String md5(String plaintext) {
        return cryptoUtil.md5(plaintext);
    }

    /**
     * md5加密
     * @param plaintext 明文
     * @param salt 盐
     * @param saltLevel 加盐层级
     * @param convertType 密文字节转字符串的方式
     * @return 密文
     */
    public static String md5(String plaintext, String salt, SaltLevel saltLevel, ConvertType convertType) {
        String md5 = cryptoUtil.setCustomSalt(salt)
                .md5(plaintext,saltLevel, convertType);
        cryptoUtil.setCustomSalt(null);
        return md5;
    }

    /**
     * md5加密
     * @param plaintext 明文
     * @param salt 盐
     * @param saltLevel 加盐层级
     * @return 密文
     */
    public static String md5(String plaintext, String salt, SaltLevel saltLevel) {
        return md5(plaintext, salt, saltLevel, ConvertType.BYTE2HEX_BITWISE);
    }

    /**
     * md5加密
     * @param plaintext 明文
     * @param salt 盐
     * @return 密文
     */
    public static String md5(String plaintext, String salt) {
        return md5(plaintext,salt,cryptoUtil.getDefaultSaleLevel(),ConvertType.BYTE2HEX_BITWISE);
    }

}
