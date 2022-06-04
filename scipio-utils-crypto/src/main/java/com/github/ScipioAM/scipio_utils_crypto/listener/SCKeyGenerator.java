package com.github.ScipioAM.scipio_utils_crypto.listener;

import com.github.ScipioAM.scipio_utils_crypto.AbstractCrypto;
import com.github.ScipioAM.scipio_utils_crypto.mode.CryptoAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 对称加密的密钥生成
 *
 * @author Alan Scipio
 * @since 2022/6/4
 */
public interface SCKeyGenerator {

    /**
     * 生成对称密钥
     *
     * @param algorithm 加解密算法
     * @param userSeed  用户设定的明文密钥
     * @return 对称密钥
     * @throws NoSuchAlgorithmException 未知的加解密算法
     */
    SecretKey generateKey(CryptoAlgorithm algorithm, String userSeed) throws NoSuchAlgorithmException;

    /**
     * 默认实现
     */
    SCKeyGenerator DEFAULT = (algorithm, userSeed) -> {
        KeyGenerator keygen;//构造密钥生成器
        try {
            keygen = KeyGenerator.getInstance(algorithm.getName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        SecureRandom secureRandom = AbstractCrypto.getSecureRandom(userSeed);
        //初始化密钥生成器
        String algorithmName = algorithm.getName().toUpperCase();
        switch (algorithmName) {
            case "AES":
                keygen.init(128, secureRandom);
                break;
            case "DESEDE":
                keygen.init(168, secureRandom);
                break;
            case "DES":
                keygen.init(56, secureRandom);
                break;
            default:
                throw new IllegalArgumentException("Can not init javax.crypto.KeyGenerator for algorithm[" + algorithmName + "]");
        }
        //生成密钥
        return keygen.generateKey();
    };

}
