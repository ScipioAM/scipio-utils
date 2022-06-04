package com.github.ScipioAM.scipio_utils_crypto.listener;

import com.github.ScipioAM.scipio_utils_crypto.AbstractCrypto;
import com.github.ScipioAM.scipio_utils_crypto.mode.CryptoAlgorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 非对称加密的密钥对生成
 *
 * @author Alan Scipio
 * @since 2022/6/4
 */
public interface ACKeyGenerator {

    /**
     * 生成非对称密钥对
     *
     * @param algorithm 加解密算法
     * @param userSeed  用户设定的明文密钥
     * @return 非对称密钥对
     * @throws NoSuchAlgorithmException 未知的加解密算法
     */
    KeyPair generateKey(CryptoAlgorithm algorithm, String userSeed) throws NoSuchAlgorithmException;

    /**
     * 默认实现
     */
    ACKeyGenerator DEFAULT = (algorithm, userSeed) -> {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(algorithm.getName());
        SecureRandom secureRandom = AbstractCrypto.getSecureRandom(userSeed);
        //初始化密钥生成器
        //初始化密钥生成器
        String algorithmName = algorithm.getName().toUpperCase();
        switch (algorithmName) {
            case "RSA":
                keygen.initialize(1024, secureRandom);
                break;
            case "DSA":
                keygen.initialize(512, secureRandom);
                break;
            default:
                throw new IllegalArgumentException("Can not init javax.crypto.KeyGenerator for algorithm[" + algorithmName + "]");
        }
        return keygen.generateKeyPair();
    };

}
