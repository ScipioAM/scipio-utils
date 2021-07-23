package com.github.ScipioAM.scipio_utils_io;

import java.io.*;
import java.util.Base64;

/**
 * Class: Base64Util
 * Description: v1.3
 * Author: Alan Min
 * Createtime: 2018/5/4
 */
public class Base64Util {

    private Base64Util(){}

    public static String encodeToStr(String str)
    {
        byte[] bt=str.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bt);
    }

    public static String encodeToStr(byte[] srcBts)
    {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(srcBts);
    }

    public static byte[] encodeToBytes(String str)
    {
        byte[] bt=str.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(bt);
    }

    public static byte[] encodeToBytes(byte[] srcBts)
    {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(srcBts);
    }

    public static byte[] decodeToBytes(String str)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(str);
    }

    public static byte[] decodeToBytes(byte[] srcBts)
    {
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

    public static void decode2File(String base64,File outputFile) throws IOException {
        byte[] codeBts = decodeToBytes(base64);
        FileUtil.writeBytes(outputFile, codeBts);
    }

	//读取文件并编为Base64字符串
    public static String getEncodeFromFile(String filePath)
    {
        String result=null;
        InputStream in ;
        byte[] data ;
        File file=new File(filePath);
        if( !file.exists() || file.isDirectory() )
        {
            return null;
        }
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            result=encodeToStr(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
