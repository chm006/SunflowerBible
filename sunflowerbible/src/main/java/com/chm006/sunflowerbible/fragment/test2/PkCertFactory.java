package com.chm006.sunflowerbible.fragment.test2;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenmin on 2017/1/23.
 */

public class PkCertFactory {
    private static X509Certificate umpCert;
    private static final Map pkMap = new HashMap();
    private static final String pkSuffix = ".mer.prikey.path";
    private static final String platCertPath = "plat.cert.path";

    static {
        try {
            byte[] ex = com.umpay.api.util.ProFileUtil.getFileByte("plat.cert.path");
            umpCert = getCert(ex);
        } catch (Exception var2) {
            RuntimeException rex = new RuntimeException(var2.getMessage());
            rex.setStackTrace(var2.getStackTrace());
            throw rex;
        }
    }

    public PkCertFactory() {
    }

    public static byte[] getCertByte() {
        Object b = null;

        try {
            byte[] b1 = com.umpay.api.util.ProFileUtil.getFileByte("plat.cert.path");
            return b1;
        } catch (Exception var3) {
            RuntimeException rex = new RuntimeException(var3.getMessage());
            rex.setStackTrace(var3.getStackTrace());
            throw rex;
        }
    }

    public static PrivateKey getPk(String merId) {
        if(pkMap.containsKey(merId)) {
            return (PrivateKey)pkMap.get(merId);
        } else {
            Map var1 = pkMap;
            synchronized(pkMap) {
                if(pkMap.containsKey(merId)) {
                    return (PrivateKey)pkMap.get(merId);
                } else {
                    PrivateKey var10000;
                    try {
                        byte[] ex = com.umpay.api.util.ProFileUtil.getFileByte(merId + ".mer.prikey.path");
                        PrivateKey rex1 = getPk(ex);
                        pkMap.put(merId, rex1);
                        var10000 = rex1;
                    } catch (Exception var4) {
                        RuntimeException rex = new RuntimeException(var4.getMessage());
                        rex.setStackTrace(var4.getStackTrace());
                        throw rex;
                    }

                    return var10000;
                }
            }
        }
    }

    public static X509Certificate getCert() {
        return umpCert;
    }

    private static X509Certificate getCert(byte[] b) {
        try {
            ByteArrayInputStream e = new ByteArrayInputStream(b);
            CertificateFactory rex1 = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate)rex1.generateCertificate(e);
            return x509Certificate;
        } catch (CertificateException var4) {
            RuntimeException rex = new RuntimeException();
            rex.setStackTrace(var4.getStackTrace());
            throw rex;
        }
    }

    private static PrivateKey getPk(byte[] b) {
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(b);

        RuntimeException rex;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pk = kf.generatePrivate(peks);
            return pk;
        } catch (NoSuchAlgorithmException var6) {
            rex = new RuntimeException();
            rex.setStackTrace(var6.getStackTrace());
            throw rex;
        } catch (InvalidKeySpecException var7) {
            rex = new RuntimeException();
            rex.setStackTrace(var7.getStackTrace());
            throw rex;
        }
    }
}
