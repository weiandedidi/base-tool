package com.lsh.base.common.utils;

/**
 * Created by fuhao on 16/4/21.
 */
import sun.misc.BASE64Encoder;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA安全编码组件
 *
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public  class RSACoder extends Coder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data
     *            加密数据
     * @param privateKey
     *            私钥
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data
     *            加密数据
     * @param publicKey
     *            公钥
     * @param sign
     *            数字签名
     *
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }



    public static String  sign(String inputStr,String privateKey)throws Exception{
        byte[] data = inputStr.getBytes();

        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);
        String encodeInputStr =(new BASE64Encoder()).encodeBuffer(encodedData);
        System.err.println("加密后: " + encodeInputStr);
        return encodeInputStr;
    }

    public static String design(String inputStr,String publicKey)throws Exception{
        // 对密钥解密
        byte[] encodedData = decryptBASE64(inputStr);
        byte[] decodedData = RSACoder
                .decryptByPublicKey(encodedData, publicKey);

        String outputStr = new String(decodedData);
        System.err.println( "解密后: " + outputStr);
        return outputStr;
    }

    public static void main(String args[]) throws Exception{
        Map<String, Object> keyMap = RSACoder.initKey();

        // String publicKey = RSACoder.getPublicKey(keyMap);
        // String privateKey = RSACoder.getPrivateKey(keyMap);
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClFO0dyjBmNwI2GgGvXb7De1MiYsOl2wss67k4\n" +
                "shrjWDgfipYuQYRTKw+WqtxB26cSR6G8IsU3bAANFvqeJWcbM0gjAZlgBCzGcZKcTcynqD4JzzA+\n" +
                "BbF3aEnmBvUa9BIPCtbr4r12czWuPIRZf0DpW8l4gzzcgrU+5mu1b8hCXQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKUU7R3KMGY3AjYaAa9dvsN7UyJi\n" +
                "w6XbCyzruTiyGuNYOB+Kli5BhFMrD5aq3EHbpxJHobwixTdsAA0W+p4lZxszSCMBmWAELMZxkpxN\n" +
                "zKeoPgnPMD4FsXdoSeYG9Rr0Eg8K1uvivXZzNa48hFl/QOlbyXiDPNyCtT7ma7VvyEJdAgMBAAEC\n" +
                "gYEAkaX65tOjDCvweYdMgUr2qkY+vRHiiIzQOVvo2YraikcljT6a7kjD1RUi9c3d8wU3TLuCiRks\n" +
                "Y4+YI/RoPevGIRkhY+rIIgn2h06uZGLIcrkCHPuqM+57goyQRSAOFyyystyr/iUFJAA0ewa5A2Tq\n" +
                "RkJW/OXEZK6yoPhUvNfz/sECQQD8VgOyuWtDDka5MQa60Spa1ItJrbw/LsdweA6aVd3KqUMeI9M9\n" +
                "iSz0Zc7Xx2xamJAVwL51O6TNVJNOjUT9f97JAkEAp3qRzKXLW6HYRXcmVd86LUZC2++U6FRNM6OK\n" +
                "lMyvWWldf+pCAF3/Uml/Th9tGonAFV8kzerJWMcPMjn09HGs9QJAGKo3tlKVf3p3w0ZdiAlGAJbh\n" +
                "/pOy4OIrsHyrwL4/7b1ZyCpsTYmJEWKaM1FU8A4Vl2q9syvfUgrAU19PrQ/AsQJAGixbkMHwT/ex\n" +
                "FtoSEaV7MAwk8r40ZUKXdhR+2dZLKQwAYrc3bVtDRZvbG1hp8pXIgQ6Hb+7bXJZvV4gbMgB56QJB\n" +
                "ALo8j+2vJLZ3smFzoUWoVTP6SMUSf7IQDrAy0cQH+0qdi4LJdAwmdC0XEF9slg8gpMOGK2UQWF7W\n" +
                "JY/CD35t+wA=";
        System.err.println( "publicKey: " + publicKey);
        System.err.println( "privateKey: " + privateKey);

        String inputStr = "sign中文";
        String encodeStr = sign(inputStr,privateKey);
        String designStr = design(encodeStr, publicKey);


    }
}

