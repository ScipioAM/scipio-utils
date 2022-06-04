package com.github.ScipioAM.scipio_utils_crypto;

import java.io.*;
import java.util.Random;

/**
 * Class: FileSimpleCrypto
 * Description: 文件的简单加解密
 * Author: Alan Min
 * Create Date: 2020/9/28
 */
public class SimpleFileCrypto extends AbstractCrypto {

    private int key = 1;

    /**
     * 生成随机密钥
     *
     * @param bound 范围
     */
    public SimpleFileCrypto generateRandomKey(int bound) {
        Random random = new Random();
        this.key = random.nextInt(bound);
        return this;
    }

    /**
     * 生成随机密钥（100以内）
     */
    public SimpleFileCrypto generateRandomKey() {
        generateRandomKey(1000);
        return this;
    }

    /**
     * 直接指定密钥值
     */
    public SimpleFileCrypto setKey(int key) {
        this.key = key;
        return this;
    }

    public int getKey() {
        return key;
    }

    //---------------------------------------------------------------------------------

    /**
     * 加密或解密
     *
     * @param srcFile 源文件
     * @param encFile 处理过的文件
     */
    private void doCrypt(File srcFile, File encFile, String cryName) throws IOException {
        //参数检查
        checkFiles(srcFile, encFile, cryName);
        //设置缓冲池大小
        setBufferSize(srcFile.length());
        try (FileInputStream in = new FileInputStream(srcFile); FileOutputStream out = new FileOutputStream(encFile)) {
            int count;//每次循环读取的字节数
            long readCount = 0L;//读取的字节总数
            byte[] buffer = new byte[bufferSize];
            while ((count = in.read(buffer)) > -1) {
                buffer[0] = (byte) (buffer[0] ^ key);
                out.write(buffer, 0, count);//逐字节块加密（异或运算）
                out.flush();
                readCount += count;
                if (processingListener != null) {
                    processingListener.onProcess(readCount);
                }
            }
        }
    }

    /**
     * 加密
     *
     * @param srcFile 源文件
     * @param encFile 加密文件
     */
    public void encrypt(File srcFile, File encFile) throws IOException {
        doCrypt(srcFile, encFile, "encrypt");
    }

    public void encrypt(String srcFile, String encFile) throws IOException {
        encrypt(new File(srcFile), new File(encFile));
    }

    /**
     * 解密
     *
     * @param encFile 加密文件
     * @param decFile 解密文件
     */
    public void decrypt(File encFile, File decFile) throws IOException {
        doCrypt(encFile, decFile, "decrypt");
    }

    public void decrypt(String encFile, String decFile) throws IOException {
        encrypt(new File(encFile), new File(decFile));
    }


    //---------------------------------------------------------------------------------

    /**
     * 检查文件参数，如果不对就抛异常
     *
     * @param srcFile 源文件，必须存在且不能是一个目录
     * @param cryFile 加解密文件，必须不存在不能是一个目录
     */
    private void checkFiles(File srcFile, File cryFile, String cryName) throws IOException, IllegalArgumentException {
        //源文件检查
        if (!srcFile.exists()) {
            throw new FileNotFoundException("source file not found!");
        } else if (srcFile.isDirectory()) {
            throw new IllegalArgumentException("source file can not be a directory");
        }
        //加解密文件检查
        if (cryFile.isDirectory()) {
            throw new IllegalArgumentException(cryName + " file can not be a directory");
        }
        //创建加解密文件
        if (!cryFile.exists()) {
            //确保父目录存在
            File parentFile = cryFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            //开始创建
            cryFile.createNewFile();
        }
    }

}
