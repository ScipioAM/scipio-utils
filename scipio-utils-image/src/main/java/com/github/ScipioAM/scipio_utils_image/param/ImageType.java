package com.github.ScipioAM.scipio_utils_image.param;

/**
 * Class: ImageType
 * Description: 图片类型
 * Author: Alan Min
 * Create Date: 2020/9/21
 */
public enum ImageType {

    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    BMP("bmp");

    private final String name;

    ImageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
