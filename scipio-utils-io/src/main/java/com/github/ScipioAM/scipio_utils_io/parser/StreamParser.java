package com.github.ScipioAM.scipio_utils_io.parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * 流解析工具类
 * @author Alan Scipio
 * @date 2018/4/16
 */
public class StreamParser {

    private StreamParser() { }

    /**
     * readStream()   将输入流处理成字符串对象
     *
     * @param in 输入流
     * @return 处理好的字符串
     */
    public static String readStream(InputStream in) throws IOException {
        String content = null;
        if (in != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            in.close();
            content = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            baos.close();
        }
        return content;
    }

    /**
     * 将输入流读取成字节数组
     */
    public static byte[] readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b;
        try {
            int count = 0;
            byte[] buf = new byte[16384];
            while ((count = is.read(buf)) != -1) {
                if (count > 0) {
                    baos.write(buf, 0, count);
                }
            }
            b = baos.toByteArray();
        } finally {
            try {
                if (is != null)
                    is.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 解读经gzip压缩的网页源码
     * @param in 原始输入流
     * @return 解读好后的字符串
     */
    public static String readStreamFromGZIP(InputStream in) throws IOException {
        StringBuilder str = new StringBuilder();

        GZIPInputStream gin = new GZIPInputStream(in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(gin, StandardCharsets.UTF_8));
        String line = "";
        while ((line = reader.readLine()) != null) {
            str.append(line).append("\n");
        }
        reader.close();
        in.close();
        return str.toString();
    }

}
