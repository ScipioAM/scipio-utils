package com.github.ScipioAM.scipio_utils_image;

import com.github.ScipioAM.scipio_utils_image.param.BarcodeParam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class: BaseZXingUtil
 * Description:v2.5
 * Author: Alan Min
 * Createtime: 2018/6/8
 */
public abstract class AbstractZXingUtil {

    private boolean isFirstRead=true;//是否第一次解析条码

    /**
     * 生成条码的基础方法
     * @param param 生成条码的相关参数
     * barcodeType：生成条码类型（一维码还是二维码）
     * charset：字符集
     * qrErrorLevel：二维码容错等级（生成一维码时为空）
     * margin：边距
     * width：宽度
     * height：高度
     * content：条码内容
     */
    private BitMatrix createBitMatrix(BarcodeParam param) throws WriterException
    {
        //定义条码的参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, param.getCharset());//字符集
        hints.put(EncodeHintType.MARGIN, param.getMargin());//边距
        BarcodeFormat format = null;
        switch(param.getBarcodeType())
        {
            case CODE39_1D:
                format=BarcodeFormat.CODE_39;
                break;
            case CODE93_1D:
                format=BarcodeFormat.CODE_93;
                break;
            case CODE128_1D:
                format=BarcodeFormat.CODE_128;
                break;
            case QR_2D:
                hints.put(EncodeHintType.ERROR_CORRECTION,param.getQRErrorLevel());//容错级别
                format=BarcodeFormat.QR_CODE;
                break;
        }
        return new MultiFormatWriter().encode(param.getContent(), format,param.getWidth(),param.getHeight(),hints);
    }

    /**
     * 生成条码-字节输出流
     */
    protected ByteArrayOutputStream createCode_stream(BarcodeParam param)
            throws WriterException, IOException
    {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        BitMatrix bitMatrix=createBitMatrix(param);
        MatrixToImageWriter.writeToStream(bitMatrix,param.getImageType().getName(),bos);
        return bos;
    }

    /**
     * 生成条码-直接生成文件
     */
    protected void createCode_file(BarcodeParam param) throws WriterException, IOException
    {
        BitMatrix bitMatrix=createBitMatrix(param);
        MatrixToImageWriter.writeToPath(bitMatrix,param.getImageType().getName(),param.getBarcodeFile().toPath());
    }

    /**
     * 生成条码-BufferedImage
     */
    protected BufferedImage createCode_bufferedImage(  BarcodeParam param) throws WriterException
    {
        BitMatrix bitMatrix=createBitMatrix(param);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 绘制带logo的二维码码图片
     */
    protected void drawColorful2DCode(BarcodeParam param)
            throws WriterException, IOException
    {
        //条码图片对象
        BufferedImage barImg=toBufferedImage(createBitMatrix(param), param.getForeColor(), param.getBgColor());
        Graphics2D gs=barImg.createGraphics();//绘制操作对象
        //宽高比率
        int ratioWidth = barImg.getWidth()*2/10;
        int ratioHeight = barImg.getHeight()*2/10;
        if(param.getLogoFile()!=null)//logo文件存在，绘制logo
        {
            //载入logo
            Image logoImg = ImageIO.read(param.getLogoFile());
            //设定logo的宽高比率
            int logoWidth = Math.min(logoImg.getWidth(null), ratioWidth);
            int logoHeight = Math.min(logoImg.getHeight(null), ratioHeight);
            //logo距条码图片边界的外边距
            int x = (barImg.getWidth() - logoWidth) / 2;
            int y = (barImg.getHeight() - logoHeight) / 2;

            gs.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
            gs.setColor(Color.black);
            gs.setBackground(Color.WHITE);
            gs.dispose();
            logoImg.flush();//执行logo绘制
        }
        //写入文件
        if(!ImageIO.write(barImg,param.getImageType().getName(),param.getBarcodeFile())) {
            throw new IOException("write image to file has been failed!");
        }
    }

    /**
     * 解析条码-基础实现方法
     * @param bufferedImage：需要解析的文件
     * @param charset：字符集
     * @param tryAnotherRead:是否尝试另一种方式解析（只有自己调用自己的时候是true,其他地方调用肯定是false）
     */
    private Result baseReadCode(BufferedImage bufferedImage, String charset, boolean tryAnotherRead) throws IOException
    {
        BufferedImage tempImg=bufferedImage;
        MultiFormatReader reader=new MultiFormatReader();
        Result result;
        //定义需要解析的条码的参数
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, charset);//字符集

        if(tryAnotherRead) {
            tempImg=drawMonochromeImg(bufferedImage);//置为黑白图片 - 仅限白底条码图片
            isFirstRead=false;
        }
        LuminanceSource source=new BufferedImageLuminanceSource(tempImg);
        Binarizer binarizer=new HybridBinarizer(source);
        BinaryBitmap bbImg=new BinaryBitmap(binarizer);
        try {
            if(charset==null || charset.equals(""))
                result=reader.decode(bbImg);
            else
                result=reader.decode(bbImg,hints);
            System.out.println("===== Barcode read success =====");
        } catch (NotFoundException e) {
            if(isFirstRead) {
                System.out.println("===== Try to read barcode again by another way =====");
                result=baseReadCode(bufferedImage,charset,true);
            }
            else {
                System.out.println("===== Read barcode twice has failed =====");
                isFirstRead=true;//重置
                throw new IOException("read barcode failed!");
            }
        }
        return result;
    }

    /**
     * 从File对象中解析条码内容
     * @param codeFile 条码文件对象
     * @param charset 字符集
     */
    protected Result readCode(File codeFile, String charset) throws IOException {
        BufferedImage bfImg=ImageIO.read(codeFile);
        return baseReadCode(bfImg,charset,false);
    }

    /**
     * 从输入流中解析条码内容
     * @param is 输入流对象
     * @param charset 字符集
     */
    protected Result readCode(InputStream is, String charset) throws IOException {
        BufferedImage bfImg=ImageIO.read(is);
        return baseReadCode(bfImg,charset,false);
    }

    //*****************************工具方法*****************************
    //为条码图片设置本身的颜色(前景色、背景色)
    private BufferedImage toBufferedImage(BitMatrix matrix,Color foreColor,Color bgColor)
    {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int fColor=foreColor.getRGB();
        int bColor=bgColor.getRGB();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                image.setRGB(x, y, matrix.get(x, y) ? fColor : bColor);
            }
        }
        return image;
    }

    //将原图片画成黑白图片 - 仅限白底的条码图片，不然全黑
    private BufferedImage drawMonochromeImg(BufferedImage orignalImg)
    {
        int width = orignalImg.getWidth();
        int height = orignalImg.getHeight();
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                int rgb=orignalImg.getRGB(x,y);
                if(rgb!=Color.WHITE.getRGB())
                {
                   if(rgb!=Color.BLACK.getRGB())//不是白色，也不是黑色，写成黑色
                       bImg.setRGB(x, y, Color.BLACK.getRGB());
                   else
                       bImg.setRGB(x, y,rgb);
                }
                else//是白色就原样写回去
                    bImg.setRGB(x, y,rgb);
            }//end of inside for
        }//end of outside for
        return bImg;
    }

    /**
     * 检查参数是否合法，如果有不合法的就抛异常
     * @param param 待检测的对象
     * @param chkBase64 是否检查base64
     * @param chkCodeType 是否检查条码类型
     * @param chkWidthHeight 是否检查宽高
     * @param chkContent 是否检查内容
     */
    protected void checkBarcodeParam(BarcodeParam param, boolean chkBase64, boolean chkCodeType,
                                     boolean chkWidthHeight, boolean chkContent, boolean chkFile)
            throws IllegalArgumentException
    {
        StringBuilder errMsg=new StringBuilder("Illegal argument: ");
        if(param==null)
        {
            errMsg.append("param object");
        }
        else
        {
            if(chkContent)
            {
                if(param.getContent()==null || param.getContent().equals(""))
                    errMsg.append("content,");
            }
            if(chkWidthHeight)
            {
                if(param.getWidth()==null)
                    errMsg.append("width,");
                if(param.getHeight()==null)
                    errMsg.append("height,");
            }
            if(chkBase64)
            {
                if(param.getBase64Str()==null || param.getBase64Str().equals(""))
                    errMsg.append("base64Str,");
            }
            if(chkCodeType)
            {
                if(param.getBarcodeType()==null)
                    errMsg.append("codeType,");
            }
            if(chkFile)
            {
                if(param.checkBarcodeFile())
                {
                    errMsg.append("barcodeFile,");
                }
            }
        }

        if(errMsg.length()>18)
        {
            errMsg.deleteCharAt(errMsg.length()-1);
            throw new IllegalArgumentException(errMsg.toString());
        }
    }

}
