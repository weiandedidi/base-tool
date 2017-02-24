package com.lsh.base.common.utils;

/**
 * Created by fuhao on 16/4/23.
 */
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils {

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decode(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encode(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }





}
