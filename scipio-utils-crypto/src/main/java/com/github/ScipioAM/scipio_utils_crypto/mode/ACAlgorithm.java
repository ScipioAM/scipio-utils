package com.github.ScipioAM.scipio_utils_crypto.mode;

/**
 * Enum:ACAlgorithm
 * Description: 非对称加密的算法
 * Author: Alan Min
 * Create Date:2020/9/27
 */
public enum ACAlgorithm implements CryptoAlgorithm {

    RSA("RSA"),
    DSA("DSA");

    private final String name;

    ACAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
