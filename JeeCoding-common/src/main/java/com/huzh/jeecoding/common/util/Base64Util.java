package com.huzh.jeecoding.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author huzh
 * @description: 用于token转码
 * @date 2020/5/8 19:33
 */
public class Base64Util {

    /**
     * 加密JDK1.8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 解密JDK1.8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }
}
