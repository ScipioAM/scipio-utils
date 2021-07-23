package com.github.ScipioAM.scipio_utils_image;

import com.github.ScipioAM.scipio_utils_image.param.BarcodeParam;
import com.github.ScipioAM.scipio_utils_image.param.BarcodeType;
import com.github.ScipioAM.scipio_utils_io.Base64Util;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.awt.*;
import java.io.*;

/**
 * Class: ZXingUtil
 * Description:
 * Author: Alan Min
 * Createtime: 2018/6/8
 */
public class ZXingUtil extends AbstractZXingUtil {

    //************************解析条码（一维码或二维码）************************
    /**
     * 解析字节数组形式的条码
     * @param bytes 条码数据
     * @param charset 条码内容的字符集
     * @param isBase64 是否是Base64编码的数据
     * @return 条码内容
     */
    public String readBytesBarcode(byte[] bytes, String charset, boolean isBase64)
            throws IOException {
        byte[] decodeBytes;
        if(isBase64)
            decodeBytes= Base64Util.decodeToBytes(bytes);
        else
            decodeBytes=bytes;
        InputStream is=new ByteArrayInputStream(decodeBytes);
        Result result=readCode(is,charset);
        return result.getText();
    }

    /**
     * 解析编码成Base64字符串的条码
     * @param base64Str 条码内容（base64编码）
     * @param charset 条码内容的字符集
     * @return 条码内容
     */
    public String readBase64Barcode(String base64Str, String charset) throws IOException {
        byte[] decodeBytes=Base64Util.decodeToBytes(base64Str.getBytes());
        InputStream is=new ByteArrayInputStream(decodeBytes);
        Result result=readCode(is,charset);
        return result.getText();
    }

    /**
     * 解析文件形式的条码
     * @param filePathName 条码文件的全路径
     * @param charset 条码内容的字符集
     * @return 条码内容
     */
    public String readFileBarcode(String filePathName, String charset) throws IOException {
        File file=new File(filePathName);
        if(file.exists()) {
            Result result=readCode(file,charset);
            return result.getText();
        }
        else {
            throw new FileNotFoundException("Error:cant found the file!");
        }
    }

    /**
     * 解析输入流形式的条码
     */
    public String readStreamBarcode(InputStream is,String charset) throws IOException {
        Result result=readCode(is,charset);
        return result.getText();
    }

    //************************生成二维码************************

    /**
     * 生成二维码 - 快速创建版
     * 默认长宽是200，边距是1，格式是png，字符集是utf-8
     * @param fileName 输出文件全名（包括路径）
     * @param content 条码内容
     */
    public void createQRCode(String fileName, String content)
            throws IOException, WriterException
    {
        BarcodeParam param=BarcodeParam.buildDefaultParam(BarcodeType.QR_2D,content);
        param.setBarcodeFile(fileName);
        createCode_file(param);
    }

    /**
     * 生成二维码 - 自定义参数
     * @param param 生成的相关参数，具体如下：
     * barcodeFile：要生成的条码文件全路径
     * content：二维码的内容
     * imageType：二维码的图片编码格式
     * qrErrorLevel：二维码纠错级别
     * margin：生成图片的内边距
     * width：生成图片的宽
     * height：生成图片的高
     */
    public void createQRCode(BarcodeParam param) throws IOException, WriterException
    {
        //参数检查（参数非法就抛异常）
        checkBarcodeParam(param,false,false,true,true,true);

        param.setBarcodeType(BarcodeType.QR_2D);
        createCode_file(param);//生成二维码图片
    }

    /**
     * 生成二维码并转为Base64字符串 - 快速创建版
     */
    public String createQRtoBase64Str(  String content) throws IOException, WriterException
    {
        BarcodeParam param = BarcodeParam.buildDefaultParam(BarcodeType.QR_2D,content);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToStr(bytes);
    }

    /**
     * 生成二维码并转为Base64字符串 - 自定义参数版
     * content：二维码内容
     * imageType：二维码的图片格式
     * qrErrorLevel：二维码纠错级别
     * margin：图片内边距
     * width：图片宽
     * height：图片高
     */
    public String createQRtoBase64Str(BarcodeParam param) throws IOException, WriterException
    {
        checkBarcodeParam(param,false,false,true,true,false);
        param.setBarcodeType(BarcodeType.QR_2D);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToStr(bytes);
    }

    //生成二维码并转为Base64的字节数组 - 快速创建版
    public byte[] createQRtoBase64Bytes(String content) throws IOException, WriterException
    {
        BarcodeParam param=BarcodeParam.buildDefaultParam(BarcodeType.QR_2D,content);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToBytes(bytes);
    }

    //生成二维码并转为Base64的字节数组 - 自定义参数版
    public byte[] createQRtoBase64Bytes(  BarcodeParam param) throws IOException, WriterException
    {
        checkBarcodeParam(param,false,false,false,true,false);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToBytes(bytes);
    }

    /**
     * 绘制彩色带logo二维码 - 自定义参数版
     * @param param 自定义参数，具体如下：
     * logoFile：logo图片（可为空）
     * foreColor：前景色
     * bgColor：背景色
     * content：二维码内容
     * imageType：二维码的图片格式
     * qrErrorLevel：二维码纠错级别
     * margin：图片内边距
     * width：图片宽
     * height：图片高
     */
    public void createQRWithColor(BarcodeParam param)
            throws IOException, WriterException
    {
        checkBarcodeParam(param,false,false, true,true,true);
        param.setBarcodeType(BarcodeType.QR_2D);
        drawColorful2DCode(param);
    }

    /**
     * 绘制彩色带logo二维码 - 快速创建版
     * @param fileName 输出文件，包括路径的文件全名
     * @param content 二维码内容
     * @param foreColor 前景色
     * @param bgColor 背景色
     * @param logoFile logo文件（可为空）
     */
    public void createQRWithColor(String fileName, String content, Color foreColor, Color bgColor, File logoFile)
            throws IOException, WriterException
    {
        BarcodeParam param=BarcodeParam.buildDefaultParam(BarcodeType.QR_2D,content);
        param.setForeColor(foreColor);
        param.setBgColor(bgColor);
        param.setLogoFile(logoFile);
        param.setBarcodeFile(fileName);
        drawColorful2DCode(param);
    }

    //************************生成一维码************************

    /**
     *生成一维码（快速创建版）
     * 默认宽200，高50，边距0。默认code128
     */
    public void create1DCode(String fileName, String content)
            throws IOException, WriterException
    {
        BarcodeParam param= BarcodeParam.buildDefaultParam(BarcodeType.CODE128_1D, content);
        param.setBarcodeFile(fileName);
        createCode_file(param);
    }

    /**
     * 生成一维码
     * param barcodeFile：要输出的文件
     * param content：一维码内容
     * param imageType：图片格式
     * param barcodeType：一维码类型(code128或code93等等)
     * param margin：图片内边距
     * param width：图片宽
     * param height：图片高
     */
    public void create1DCode(BarcodeParam param) throws IOException, WriterException
    {
        checkBarcodeParam(param,false,true, true,true,true);
        createCode_file(param);
    }

    /**
     * 生成一维码并转为Base64字符串(快速创建版,，默认code128)
     */
    public String create1DtoBase64Str(String content) throws IOException, WriterException
    {
        BarcodeParam param= BarcodeParam.buildDefaultParam(BarcodeType.CODE128_1D,content);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToStr(bytes);
    }

    /**
     * 生成一维码并转为Base64字符串(快速创建版 - 可选择codeType版)
     */
    public String create1DtoBase64Str(String content, BarcodeType codeType) throws IOException, WriterException
    {
        BarcodeParam param= BarcodeParam.buildDefaultParam(codeType,content);
        byte[] bytes=createCode_stream(param).toByteArray();
        return Base64Util.encodeToStr(bytes);
    }

}