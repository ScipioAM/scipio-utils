package com.github.ScipioAM.scipio_utils_common;

import java.util.*;

/**
 * 随机工具类
 *
 * @author Alan Scipio
 * @since 2022/6/9
 */
public class RandomUtil {

    private final static char[] BASE_CHARS = {
            //开始下标：0
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            //开始下标：26
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            //开始下标：52
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            //开始下标：62, 共16个
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', ',', '.', '|'
    };

    //============================================ ↓↓↓↓↓↓ UUID ↓↓↓↓↓↓ ============================================

    /**
     * 获取UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取UUID（不带横杠）
     */
    public static String getUUID_noDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //============================================ ↓↓↓↓↓↓ int ↓↓↓↓↓↓ ============================================

    /**
     * 随机整数
     */
    public static int getInt() {
        return new Random().nextInt();
    }

    /**
     * 随机整数（指定范围）
     *
     * @param max 上限
     * @param min 下限
     */
    public static int getIntWithRange(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return min + (int) (Math.random() * (max - min));
    }

    /**
     * 随机整数（固定位数）
     *
     * @param fixedLength 固定位数
     */
    public static int getIntWithFixedLength(int fixedLength) {
        if (fixedLength <= 0) {
            throw new IllegalArgumentException("fixedLength must be positive");
        }
        StringBuilder s = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < fixedLength; i++) {
            s.append(random.nextInt(10));
        }
        return Integer.parseInt(s.toString());
    }

    //============================================ ↓↓↓↓↓↓ 字符串 ↓↓↓↓↓↓ ============================================

    /**
     * 随机字符串
     *
     * @param length  字符串长度
     * @param numbers 是否有数字
     * @param lower   是否有小写字母
     * @param upper   是否有大写字母
     * @param special 是否有特殊符号
     */
    public static String getString(int length, boolean numbers, boolean lower, boolean upper, boolean special) {
        if (length <= 0) {
            throw new IllegalArgumentException("string length must be positive");
        }
        final int LOWER = 1, UPPER = 2, NUMBER = 3, SPECIAL = 4;
        List<Integer> kindIndexArr = new ArrayList<>();
        if (numbers) {
            kindIndexArr.add(NUMBER);
        }
        if (lower) {
            kindIndexArr.add(LOWER);
        }
        if (upper) {
            kindIndexArr.add(UPPER);
        }
        if (special) {
            kindIndexArr.add(SPECIAL);
        }
        if (kindIndexArr.size() == 0) {
            throw new IllegalArgumentException("none kind of strings have been chosen");
        }
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int kindIndex = kindIndexArr.get(random.nextInt(kindIndexArr.size()));
            int charIndex = 0;
            switch (kindIndex) {
                case LOWER: //小写
                    charIndex = random.nextInt(26);
                    break;
                case UPPER: //大写
                    charIndex = random.nextInt(26) + 26;
                    break;
                case NUMBER: //数字
                    charIndex = random.nextInt(10) + 52;
                    break;
                case SPECIAL: //特殊字符
                    charIndex = random.nextInt(16) + 62;
                    break;
            }
            s.append(BASE_CHARS[charIndex]);
        }
        return s.toString();
    }

    /**
     * 随机字符串（没有特殊字符）
     *
     * @param length 字符串长度
     */
    public static String getString(int length) {
        return getString(length, true, true, true, false);
    }

    /**
     * 随机字符串（没有特殊字符）
     *
     * @param length  字符串长度
     * @param special 是否加入特殊字符
     */
    public static String getStringWithSpecialChar(int length, boolean special) {
        return getString(length, true, true, true, special);
    }

    /**
     * 随机字符串（没有数字）
     *
     * @param length  字符串长度
     * @param special 是否加入特殊字符
     */
    public static String getStringWithoutNum(int length, boolean special) {
        return getString(length, false, true, true, special);
    }

    /**
     * 随机字符串（根据自定义基础字符池）
     *
     * @param getNumFromPool 为了组成这一个字符池，从池子里拿的次数
     * @param pool           自定义基础字符池
     */
    public static String getStringByCustom(int getNumFromPool, Collection<String> pool) {
        if (pool == null || pool.size() == 0) {
            throw new IllegalArgumentException("method argument 'Collection<String> pool' can not be empty");
        }
        //确定池子的类型
        String[] arr = null;
        List<String> poolList = null;
        if (pool instanceof List) {
            poolList = (List<String>) pool;
        } else {
            arr = new String[pool.size()];
            pool.toArray(arr);
        }
        //开始构建
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < getNumFromPool; i++) {
            int index = random.nextInt(pool.size());
            if (poolList != null) {
                result.append(poolList.get(index));
            } else {
                result.append(arr[index]);
            }
        }
        return result.toString();
    }

    public static String getStringByCustom(Collection<String> pool) {
        return getStringByCustom(1, pool);
    }

    /**
     * 随机字符串（根据自定义基础字符池）
     *
     * @param length 随机字符串的长度
     * @param pool   自定义基础字符池
     */
    public static String getStringByCustom(int length, char[] pool) {
        if (pool == null || pool.length == 0) {
            throw new IllegalArgumentException("method argument 'Collection<String> pool' can not be empty");
        }
        //开始构建
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(pool.length);
            result.append(pool[index]);
        }
        return result.toString();
    }

    public static String getStringByCustom(int length, String pool) {
        return getStringByCustom(length, pool.toCharArray());
    }

    //============================================ ↓↓↓↓↓↓ long ↓↓↓↓↓↓ ============================================

    /**
     * 随机长整型
     */
    public static long getLong() {
        return new Random().nextLong();
    }

    /**
     * 随机长整型（指定范围）
     *
     * @param max 上限
     * @param min 下限
     */
    public static long getIntWithRange(long min, long max) {
        if (min > max) {
            long temp = min;
            min = max;
            max = temp;
        }
        return min + (long) (Math.random() * (max - min));
    }

    /**
     * 随机长整型（固定位数）
     *
     * @param fixedLength 固定位数
     */
    public static long getIntWithFixedLength(long fixedLength) {
        if (fixedLength <= 0) {
            throw new IllegalArgumentException("fixedLength must be positive");
        }
        StringBuilder s = new StringBuilder();
        Random random = new Random();
        for (long i = 0; i < fixedLength; i++) {
            s.append(random.nextInt(10));
        }
        return Integer.parseInt(s.toString());
    }

    //============================================ ↓↓↓↓↓↓ 字节数组 ↓↓↓↓↓↓ ============================================

    /**
     * 获取随机字节数组
     */
    public static byte[] getBytes(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }

    /**
     * 获取随机字节数组
     */
    public static byte[] getBytes() {
        Random random = new Random();
        byte[] bytes = new byte[random.nextInt(100) + 1];
        random.nextBytes(bytes);
        return bytes;
    }

    //============================================ ↓↓↓↓↓↓ 布尔值 ↓↓↓↓↓↓ ============================================

    /**
     * 获取随机布尔值
     */
    public static boolean getBoolean() {
        return new Random().nextBoolean();
    }

    //============================================ ↓↓↓↓↓↓ 浮点数 ↓↓↓↓↓↓ ============================================

    /**
     * 随机双精度浮点数
     */
    public static double getDouble() {
        return new Random().nextDouble();
    }

    /**
     * 随机双精度浮点数（指定范围）
     *
     * @param max 上限
     * @param min 下限
     */
    public static double getDoubleWithRange(double min, double max) {
        if (min > max) {
            double temp = min;
            min = max;
            max = temp;
        }
        return min + (Math.random() * (max - min));
    }

    /**
     * 随机单精度浮点数
     */
    public static float getFloat() {
        return new Random().nextFloat();
    }

    /**
     * 随机单精度浮点数（指定范围）
     *
     * @param max 上限
     * @param min 下限
     */
    public static float getFloatWithRange(float min, float max) {
        if (min > max) {
            float temp = min;
            min = max;
            max = temp;
        }
        return (float) (min + (Math.random() * (max - min)));
    }

}
