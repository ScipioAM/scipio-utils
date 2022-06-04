package com.github.ScipioAM.scipio_utils_crypto;

import com.github.ScipioAM.scipio_utils_crypto.mode.ConvertType;
import com.github.ScipioAM.scipio_utils_crypto.mode.CryptoAlgorithm;
import com.github.ScipioAM.scipio_utils_crypto.mode.MDAlgorithm;
import com.github.ScipioAM.scipio_utils_crypto.mode.SaltLevel;
import com.github.ScipioAM.scipio_utils_crypto.utils.HexUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class: MessageDigestUtil
 * Description: 信息摘要算法
 * Author: Alan Min
 * Create Time: 2018/6/1
 */
public class MessageDigestUtil extends AbstractCrypto {

    /**
     * 加密
     *
     * @param algorithm   指定信息摘要的算法
     * @param content     要加密的内容
     * @param convertType 字节数组转16进制字符串的转换方式（位运算方式或StringBuilder格式化方式）
     * @param saltLevel   加盐的层数
     */
    public String encode(CryptoAlgorithm algorithm, String content, ConvertType convertType, SaltLevel saltLevel) {
        String result, saltContent;
        switch (saltLevel) {
            case LEVEL_1:
                saltContent = getSaltContent(content);
                result = baseEncode(algorithm, saltContent, convertType);
                break;
            case LEVEL_2:
                saltContent = getSaltContent(content);
                String tempResult = baseEncode(algorithm, saltContent, convertType);//第一层加密
                result = baseEncode(algorithm, getSaltContent(tempResult), convertType);
                break;
            case NONE:
            default:
                result = baseEncode(algorithm, content, convertType);
        }
        return result;
    }

    /**
     * 获取文件的总和校验值
     *
     * @throws IOException 找不到文件，读取文件流出错，关闭文件流出错
     */
    public String getFileChecksum(File file, CryptoAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm.getName());
        byte[] buffer = new byte[1024];
        int numRead;
        try (FileInputStream fis = new FileInputStream(file)) {
            while ((numRead = fis.read(buffer)) > 0) {
                md.update(buffer, 0, numRead);
            }
        }
        return HexUtil.byte2HexByBitwise(md.digest());
    }

    /**
     * 获取文件的md5总和校验值
     */
    public String getMd5Checksum(File file) throws IOException, NoSuchAlgorithmException {
        return getFileChecksum(file, MDAlgorithm.MD5);
    }

    /**
     * 比较两个文件的校验码
     *
     * @return 返回true代表一致
     * @throws IOException 找不到文件，读取文件流出错，关闭文件流出错
     */
    public boolean compareFileChecksum(File file0, File file1, CryptoAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        String file0Md5 = getFileChecksum(file0, algorithm);
        String file1Md5 = getFileChecksum(file1, algorithm);
        if (file0Md5 != null) {
            return (file0Md5.equals(file1Md5));
        } else {
            return false;
        }
    }

    /**
     * 比较两个文件的MD5校验码
     */
    public boolean compareFileMd5(File file0, File file1) throws IOException, NoSuchAlgorithmException {
        return compareFileChecksum(file0, file1, MDAlgorithm.MD5);
    }

    /**
     * 检查文件与给定的校验码是否一致
     *
     * @return 返回true代表一致
     * @throws IOException 找不到文件，读取文件流出错，关闭文件流出错
     */
    public boolean compareFileChecksum(File file, String anotherChecksum, CryptoAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        String fileChecksum = getFileChecksum(file, algorithm);
        if (fileChecksum != null) {
            return (fileChecksum.equals(anotherChecksum));
        } else {
            return false;
        }
    }

    /**
     * 检查文件与给定的md5校验码是否一致
     */
    public boolean compareFileMd5(File file, String anotherMd5) throws IOException, NoSuchAlgorithmException {
        return compareFileChecksum(file, anotherMd5, MDAlgorithm.MD5);
    }

    /**
     * 加密核心实现方法
     *
     * @param algorithm   加密算法
     * @param content     需要加密的内容
     * @param convertType 转换方式（位运算方式或StringBuilder格式化方式）
     */
    private String baseEncode(CryptoAlgorithm algorithm, String content, ConvertType convertType) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getName());
            md.update(content.getBytes());// 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
            byte[] byte_encoded = md.digest();//完成哈希计算，得到加密后的字节数组

            if (convertType == ConvertType.BYTE2HEX_BITWISE) {
                result = HexUtil.byte2HexByBitwise(byte_encoded);
            } else {
                result = HexUtil.byte2HexByFormat(byte_encoded);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

}
