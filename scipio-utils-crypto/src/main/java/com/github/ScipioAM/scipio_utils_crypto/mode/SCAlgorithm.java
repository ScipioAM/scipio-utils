package com.github.ScipioAM.scipio_utils_crypto.mode;

/**
 * Enum:Algorithm
 * Description: 对称加密的算法
 * Author: Alan Min
 * Create Date:2020/9/22
 */
public enum SCAlgorithm {

    AES("AES"),
    AES_CBC_PKCS7PADDING("AES/CBC/PKCS7Padding"),
    DES("DES"),
    DESEDE("DESede");//3DES

    private final String name;

    SCAlgorithm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
