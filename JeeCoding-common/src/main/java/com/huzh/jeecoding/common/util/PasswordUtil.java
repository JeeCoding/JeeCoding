package com.huzh.jeecoding.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author huzh
 * @description:
 * @date 2020/5/8 13:59
 */
public class PasswordUtil {

    public static String getEncryptedPwd(String password) throws UnsupportedEncodingException {
        // 拿到一个随机数组，作为盐
        byte[] salt = HashUtils.genSalt(HashUtils.SALT_LENGTH);
        byte[] digest = HashUtils.digest(password.getBytes(HashUtils.UTF_8), HashUtils.ALGORITHM_SHA1, salt);
        byte[] pwd = new byte[salt.length + digest.length];

        System.arraycopy(salt, 0, pwd, 0, HashUtils.SALT_LENGTH);
        System.arraycopy(digest, 0, pwd, HashUtils.SALT_LENGTH, digest.length);
        return new String(HashUtils.encodeHex(pwd));
    }

    /**
     * 密码验证
     *
     * @param password 用户输入密码
     * @param dbPasswd 数据库保存的密码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean validPasswd(String password, String dbPasswd)
            throws Exception {
        byte[] pwIndb = HashUtils.decodeHex(dbPasswd);
        byte[] salt = new byte[HashUtils.SALT_LENGTH];
        //将密码中属于salt部分复制到salt数组中
        System.arraycopy(pwIndb, 0, salt, 0, HashUtils.SALT_LENGTH);

        // 声明一个对象接收数据库中的口令消息摘要
        byte[] digestIndb = new byte[pwIndb.length - HashUtils.SALT_LENGTH];
        // 获得数据库中口令的摘要
        System.arraycopy(pwIndb, HashUtils.SALT_LENGTH, digestIndb, 0, digestIndb.length);

        //生成的摘要数据
        byte[] digest = HashUtils.digest(password.getBytes(HashUtils.UTF_8), HashUtils.ALGORITHM_SHA1, salt);
        // 比较根据输入口令生成的消息摘要和数据库中的口令摘要是否相同
        if (Arrays.equals(digest, digestIndb)) {
            return true;// 口令匹配相同
        } else {
            return false;
        }
    }
}
