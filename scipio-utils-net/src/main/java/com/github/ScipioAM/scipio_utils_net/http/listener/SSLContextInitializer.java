package com.github.ScipioAM.scipio_utils_net.http.listener;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
@FunctionalInterface
public interface SSLContextInitializer {

    /**
     * 构建SSLContext
     * @param trustManagers 信任器
     * @return SSLContext
     * @throws NoSuchAlgorithmException Protocol被设置了非法的算法名称，想了解有哪些算法，请查看：<a href=
     *                  "https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext">Java
     *                  Cryptography Architecture Standard Algorithm Name
     *                  Documentation</a>
     * @throws KeyManagementException SSLContext初始化异常
     */
    SSLContext build(TrustManager[] trustManagers) throws Exception;

    /**
     * 默认SSLContext创建者
     */
    SSLContextInitializer DEFAULT = (trustManagers) -> {
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, trustManagers, new SecureRandom());
        return sslContext;
    };

    /**
     * Apache Http Client默认SSLContext创建者
     */
    ApacheSSLContextInitializer APACHE_DEFAULT = new ApacheSSLContextInitializer();

}
