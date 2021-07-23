package com.github.ScipioAM.scipio_utils_crypto.mode;

/**
 * Class: Charset
 * Description:
 * Author: Alan Min
 * Create Date: 2020/9/27
 */
public enum  Charset {

    UTF_8("utf-8"),
    GB2312("gb2312"),
    GBK("gbk");

    private final String name;

    Charset(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
