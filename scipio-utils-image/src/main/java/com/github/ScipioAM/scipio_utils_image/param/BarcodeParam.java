package com.github.ScipioAM.scipio_utils_image.param;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class: BarcodeConfig
 * Description:v1.2
 *  二维码模块的配置类
 * Author: Alan Min
 * Createtime: 2018/6/14
 */
public class BarcodeParam {

    //******************* 字符集 *******************
    public static final String CHARSET_UTF8="utf-8";
    public static final String CHARSET_GB2312="gb2312";
    public static final String CHARSET_GBK="gbk";

    //宽度，默认二维码是200，一维码是200
    private static final int WIDTH_1D_DEFAULT=200;
    private static final int WIDTH_2D_DEFAULT=200;
    //高度，默认二维码是200，一维码是50
    private static final int HEIGHT_1D_DEFAULT=50;
    private static final int HEIGHT_2D_DEFAULT=200;
    //图片边距，默认二维码是1，一维码是0
    private static final int MARGIN_1D_DEFAULT=0;
    private static final int MARGIN_2D_DEFAULT=1;

    private String charset;//字符集
    private ImageType imageType;//图片格式
    private BarcodeType barcodeType;//条码类型

    private Integer margin;//内边距
    private Integer width;//宽度
    private Integer height;//长度
    private ErrorCorrectionLevel qrErrorLevel;//二维码纠错级别
    private String content;//条码内容

//    private String filePath;//文件路径
//    private String fileName;//文件全名（含后缀）
    private File barcodeFile;//读取或创建的条码图片
    private String base64Str;//条码图片转成的base64字符串

//    private String logoPath;//logo图片的全路径（含文件名和文件后缀）
    private File logoFile;//logo图片
    private Color foreColor;//前景色
    private Color bgColor;//背景色

    /**
     * 创建默认参数
     * @param barcodeType 条码具体类型
     * @param content 条码内容
     */
    public static BarcodeParam buildDefaultParam(BarcodeType barcodeType, String content)
    {
        boolean is2DCode = (barcodeType==BarcodeType.QR_2D);//是否为二维码
        BarcodeParam param=new BarcodeParam();
        param.setCharsetDefault();
        param.setImageTypeDefault();
        param.setMarginDefault(is2DCode);
        param.setWidthDefault(is2DCode);
        param.setHeightDefault(is2DCode);
        param.setContent(content);
        param.setBarcodeType(barcodeType);
        if(is2DCode) {
            param.setQRErrorLevelDefault();
        }
        return param;
    }

    public Color getForeColor() {
        return foreColor;
    }

    public void setForeColor(Color foreColor) {
        this.foreColor = foreColor;
    }

    public void setForeColorDefault() {
        this.foreColor = Color.BLACK;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBgColorDefault() {
        this.bgColor = Color.WHITE;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setCharsetDefault() {
        this.charset = CHARSET_UTF8;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public void setImageTypeDefault() {
        this.imageType = ImageType.PNG;
    }

    public BarcodeType getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(BarcodeType barcodeType) {
        this.barcodeType = barcodeType;
    }

    public Integer getMargin() {
        if(margin==null)
            margin=0;
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public void setMarginDefault(boolean is2DCode) {
        if(is2DCode)
            this.margin=MARGIN_2D_DEFAULT;
        else
            this.margin=MARGIN_1D_DEFAULT;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setWidthDefault(boolean is2DCode) {
        if(is2DCode)
            this.width=WIDTH_2D_DEFAULT;
        else
            this.width=WIDTH_1D_DEFAULT;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setHeightDefault(boolean is2DCode) {
        if(is2DCode)
            this.height=HEIGHT_2D_DEFAULT;
        else
            this.height=HEIGHT_1D_DEFAULT;
    }

    public ErrorCorrectionLevel getQRErrorLevel() {
        return qrErrorLevel;
    }

    public void setQRErrorLevel(ErrorCorrectionLevel errorLevel) {
        this.qrErrorLevel = errorLevel;
    }

    public void setQRErrorLevelDefault() {
        this.qrErrorLevel = ErrorCorrectionLevel.Q;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getBarcodeFile() {
        return barcodeFile;
    }

    public void setBarcodeFile(File barcodeFile) {
        this.barcodeFile = barcodeFile;
    }

    /**
     * 设置输出的文件
     * @param filePath 文件路径
     * @param fileName 文件名（不带后缀）
     * @param imageType 输出的图片类型
     */
    public void setBarcodeFile(String filePath, String fileName, ImageType imageType) {
        File file = new File(filePath+File.separator+fileName+"."+imageType.getName());
        if(file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        setBarcodeFile(file);
    }

    public void setBarcodeFile(String fullFileName) {
        File file = new File(fullFileName);
        if(file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        setBarcodeFile(file);
    }

    /**
     * 检查输出的条码文件
     * 如果没有就创建
     * @return 返回true代表文件存在，返回false代表参数为空或创建失败
     */
    public boolean checkBarcodeFile() {
        if(barcodeFile==null) {
            return false;
        }

        boolean isValid = true;
        if(!barcodeFile.exists()) {
            try {
                barcodeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                isValid = false;
            }
        }
        return isValid;
    }

    public File getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    public String getBase64Str() {
        return base64Str;
    }

    public void setBase64Str(String base64Str) {
        this.base64Str = base64Str;
    }

}
