package com.chm006.sunflowerbible.fragment.test2;


import com.umpay.api.common.Base64;
import com.umpay.api.common.SunBase64;
import com.umpay.api.log.ILogger;
import com.umpay.api.log.LogManager;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author IceWee
 * @version 1.0
 * @date 2012-4-26
 */
public class RSAUtils {

    //联动支付
    private static ILogger log_ = LogManager.getLogger();

    //签名
    public static String sign(String plain, String privateKey) {
        log_.debug("签名：plain=" + plain);
        try {
            Signature ex = Signature.getInstance("SHA1withRSA");
            byte[] keyBytes = Base64.decode(privateKey.getBytes());
            ex.initSign(getPk(keyBytes));
            ex.update(plain.getBytes("gbk"));
            byte[] rex1 = ex.sign();
            String sign = new String(Base64.encode(rex1));
            log_.debug("签名：sign=" + sign);
            return sign;
        } catch (Exception var5) {
            RuntimeException rex = new RuntimeException(var5.getMessage());
            rex.setStackTrace(var5.getStackTrace());
            throw rex;
        }
    }

    private static PrivateKey getPk(byte[] b) {
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(b);
        RuntimeException rex;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
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
        } catch (Exception e) {
            rex = new RuntimeException();
            rex.setStackTrace(e.getStackTrace());
            throw rex;
        }
    }

    //验签
    public static boolean verify(String data, String publicKey, String sign) {
        log_.debug("验签：sign=" + sign);
        log_.debug("验签：plain=" + data);
        try {
            PublicKey publicK = KeyFactory.getInstance("RSA", "BC").generatePublic(new X509EncodedKeySpec(Base64.decode(publicKey.getBytes())));
            Signature rex1 = Signature.getInstance("SHA1withRSA");
            rex1.initVerify(publicK);
            rex1.update(data.getBytes("gbk"));
            boolean b = rex1.verify(Base64.decode(sign.getBytes()));
            log_.info("验证平台签名是否成功" + b);
            return b;
        } catch (Exception var6) {
            log_.info("解密异常" + var6);
            RuntimeException rex = new RuntimeException(var6.getMessage());
            rex.setStackTrace(var6.getStackTrace());
            throw rex;
        }
    }

    //公钥加密
    public static String encryptByPublicKey(String s, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        return SunBase64.encode(cipher.doFinal(s.getBytes("GBK"))).replace("\n", "");
    }

    //私钥解密
    public static byte[] decryptByPrivateKey(String ciphertext, String privateKey) throws Exception {
        byte[] data = Base64.decode(ciphertext.getBytes());
        byte[] keyBytes = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        return cipher.doFinal(data);
    }

}
