package com.github.ScipioAM.scipio_utils_crypto.utils;

/**
 * 16进制相关工具类
 *
 * @author Alan Scipio
 * @since 2022/6/4
 */
public class HexUtil {

    private HexUtil() {
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hexStr 16进制字符串，例如"0x067209a194b6ab5482af9c937c264eaa"或“067209a194b6ab5482af9c937c264eaa”
     * @return 字节数组
     */
    public static byte[] hexStr2Bytes(String hexStr) {
        if (hexStr.contains("0x")) {
            hexStr = hexStr.substring(2);
        }
        byte[] result = new byte[hexStr.length() / 2];
        char[] chars = hexStr.toCharArray();
        for (int i = 0, j = 0; i < result.length; i++) {
            result[i] = (byte) (hexChar2Byte(chars[j++]) << 4 | hexChar2Byte(chars[j++]));
        }
        return result;
    }

    /**
     * 16进制字符转字节
     *
     * @param c 16进制字符（0-9，a-f），无视大小写
     */
    public static int hexChar2Byte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 0x0A);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 0x0a);
        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    /**
     * 字节数组转16进制字符串 - 格式化方式
     */
    public static String byte2HexByFormat(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * 字节数组转16进制字符串 - 位运算方式
     */
    public static String byte2HexByBitwise(byte[] bytes) {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)// 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
        {
            char[] tempArr = new char[2];
            tempArr[0] = hexDigits[(b >>> 4) & 15];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
            tempArr[1] = hexDigits[b & 15];// 取字节中低 4 位的数字转换
            sb.append(tempArr);
        }
        return sb.toString();
    }

}
