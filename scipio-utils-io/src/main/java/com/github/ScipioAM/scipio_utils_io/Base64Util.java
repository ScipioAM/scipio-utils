package com.github.ScipioAM.scipio_utils_io;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Base64;

/**
 * Base64工具类
 * @author Alan Min
 * @since 1.0.0
 * @date 2018/5/4
 */
public class Base64Util {

    private Base64Util(){}

    public static String encodeToStr(String str) {
        byte[] bt = str.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bt);
    }

    public static String encodeToStr(byte[] srcBts) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(srcBts);
    }

    public static byte[] encodeToBytes(String str) {
        byte[] bt = str.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(bt);
    }

    public static byte[] encodeToBytes(byte[] srcBts) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(srcBts);
    }

    public static byte[] decodeToBytes(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(str);
    }

    public static byte[] decodeToBytes(byte[] srcBts) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(srcBts);
    }

    public static String decodeToStr(String charset,String str) throws UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bts=decoder.decode(str);
        return new String(bts,charset);
    }

    public static String decodeToStr(String charset,byte[] srcBts) throws UnsupportedEncodingException {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bts=decoder.decode(srcBts);
        return new String(bts,charset);
    }

    public static void decodeToFile(String base64,File outputFile) throws IOException {
        byte[] codeBts = decodeToBytes(base64);
        FileUtil.writeBytes(outputFile, codeBts);
    }

    /**
     * 读取文件并编为Base64字符串
     * @param file 要读取的文件
     * @return 文件的base64字符串
     * @throws IOException 读取失败
     */
    public static String getEncodeFromFile(File file) throws IOException {
        if(!file.exists() || file.isDirectory()) {
            throw new NoSuchFileException("file[] not exists! or is a directory");
        }

        String result;
        InputStream in = null;
        byte[] buffer = new byte[1024];
        try {
            in = new FileInputStream(file);
            StringBuilder data = new StringBuilder();
            while(in.read(buffer) != -1) {
                data.append(new String(buffer));
            }
            in.close();
            result = encodeToStr(data.toString());
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getEncodeFromFile(String fileFullPath) throws IOException {
        return getEncodeFromFile(new File(fileFullPath));
    }

}
