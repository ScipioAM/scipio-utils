package com.github.ScipioAM.scipio_utils_net.http.listener;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.SSLContext;

/**
 * Apache Http Client的SSL连接工厂创建者
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
@FunctionalInterface
public interface ApacheSSLFactoryInitializer {

    /**
     * 构建SSL连接工厂
     * @param context SSL上下文对象
     * @return SSL连接工厂
     */
    SSLConnectionSocketFactory build(SSLContext context);

    /**
     * 默认SSL连接工厂创建者
     */
    ApacheSSLFactoryInitializer DEFAULT = (context) -> new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);

}
