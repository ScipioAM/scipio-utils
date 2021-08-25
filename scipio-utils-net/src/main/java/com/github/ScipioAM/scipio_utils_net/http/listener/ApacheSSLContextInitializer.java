package com.github.ScipioAM.scipio_utils_net.http.listener;

import com.github.ScipioAM.scipio_utils_common.StringUtil;
import com.github.ScipioAM.scipio_utils_net.http.bean.SSLCertificateFile;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alan Scipio
 * @since 1.0.1-p1
 * @date 2021/8/23
 */
public class ApacheSSLContextInitializer implements SSLContextInitializer{

    /** 寻找jre目录下的证书库 */
    private boolean loadDefaultCerts = false;

    /** 要加载的SSL证书（为null则可能尝试寻找jre的证书库，找不到就按默认创建者执行） */
    private List<SSLCertificateFile> certs = new ArrayList<>();

    public ApacheSSLContextInitializer() {}

    public ApacheSSLContextInitializer(File trustedCert) {
        if(trustedCert != null) {
            certs.add(new SSLCertificateFile(trustedCert));
        }
        else {
            throw new NullPointerException("constructor`s argument is null");
        }
    }

    public ApacheSSLContextInitializer(File trustedCert, String password) {
        if(trustedCert != null) {
            certs.add(new SSLCertificateFile(trustedCert,password));
        }
        else {
            throw new NullPointerException("constructor`s argument[java.io.File] is null");
        }
    }

    public ApacheSSLContextInitializer(SSLCertificateFile... certs) {
        if(certs != null && certs.length > 0) {
            this.certs.addAll(Arrays.asList(certs));
        }
        else {
            throw new NullPointerException("constructor argument is null");
        }
    }

    //==================================================================================================================

    @Override
    public SSLContext build(TrustManager[] trustManagers) throws Exception {
        if(certs == null || certs.size() <= 0) {
            //先尝试寻找jre路径下自带的证书，找不到就按
            if(loadDefaultCerts) {
                try {
                    loadDefaultJreCert();
                    return buildContextByBuilder();
                } catch (Exception e) {
                    System.err.println("Find default Certificate from jre_home failed, use SSLContextInitializer.DEFAULT,\nGetting Exception when found: " + e);
                    return DEFAULT.build(trustManagers); //执行默认创建者的操作
                }
            }
            else {
                return DEFAULT.build(trustManagers); //执行默认创建者的操作
            }
        }
        else {
            return buildContextByBuilder();
        }
    }//end of build()

    private SSLContext buildContextByBuilder() throws Exception{
        SSLContextBuilder builder = SSLContexts.custom()
                .setProtocol("SSLv3")
                .setSecureRandom(new SecureRandom());
        for(SSLCertificateFile cert : certs) {
            if(!cert.exists()) {
                throw new NoSuchFileException("no such trusted certificate file: " + cert.getFileAbsolutePath());
            }
            char[] passwordArr = cert.getPasswordArr();
            builder.loadTrustMaterial(cert.getFile(),passwordArr,cert.getTrustStrategy());
        }
        return builder.build();
    }

    //==================================================================================================================

    /**
     * 尝试寻找jre的lib/security目录下的自带证书库：cacerts
     */
    public void loadDefaultJreCert() throws Exception {
        String javaHome = System.getProperty("java.home");
        String dir = javaHome + File.separator + "lib" + File.separator + "security" + File.separator;
        //JRE自带证书库
        File certsLib = new File(dir + "cacerts");
        if(!certsLib.exists()) {
            throw new FileNotFoundException("JRE default SSL Certificates Lib not found: " + certsLib.getAbsolutePath());
        }
        System.out.println("Load default JRE Certificate file success: " + certsLib.getAbsolutePath());
        certs.add(new SSLCertificateFile(certsLib,"changeit"));//原始默认密码
    }

    //==================================================================================================================

    public boolean isLoadDefaultCerts() {
        return loadDefaultCerts;
    }

    public ApacheSSLContextInitializer setLoadDefaultCerts(boolean loadDefaultCerts) {
        this.loadDefaultCerts = loadDefaultCerts;
        return this;
    }

    public List<SSLCertificateFile> getCerts() {
        return certs;
    }

    public ApacheSSLContextInitializer setCerts(List<SSLCertificateFile> certs) {
        this.certs = certs;
        return this;
    }

    public ApacheSSLContextInitializer addCert(SSLCertificateFile cert) {
        if(cert == null) {
            throw new NullPointerException("argument is null");
        }
        this.certs.add(cert);
        return this;
    }

    public ApacheSSLContextInitializer addCert(File file) {
        if(file == null) {
            throw new NullPointerException("argument[java.io.File] is null");
        }
        return addCert(new SSLCertificateFile(file));
    }

    public ApacheSSLContextInitializer addCert(File file, String password) {
        if(file == null) {
            throw new NullPointerException("argument[java.io.File] is null");
        }
        return addCert(new SSLCertificateFile(file,password));
    }

    public ApacheSSLContextInitializer addCert(String file) {
        if(StringUtil.isNull(file)) {
            throw new NullPointerException("argument is empty");
        }
        return addCert(new SSLCertificateFile(file));
    }

    public ApacheSSLContextInitializer addCert(String file, String password) {
        if(StringUtil.isNull(file)) {
            throw new NullPointerException("argument[java.lang.String] is empty");
        }
        return addCert(new SSLCertificateFile(new File(file),password));
    }

}
