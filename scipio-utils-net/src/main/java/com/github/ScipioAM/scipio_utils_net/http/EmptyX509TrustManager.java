package com.github.ScipioAM.scipio_utils_net.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * X509TrustManager的空实现
 * @author Alan Min
 * @date 2018/4/16
 */
public class EmptyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
