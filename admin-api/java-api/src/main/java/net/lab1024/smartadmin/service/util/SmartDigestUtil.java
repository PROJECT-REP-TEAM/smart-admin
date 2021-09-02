package net.lab1024.smartadmin.service.util;

import org.apache.commons.codec.digest.DigestUtils;

public class SmartDigestUtil extends DigestUtils {

    /**
     * md5 加盐加密
     *
     * @param salt
     * @param str
     * @return
     */
    public static String encrypt(String salt, String str) {
        return SmartDigestUtil.md5Hex(salt + str);
    }

    /**
     * md5 加密
     *
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        return SmartDigestUtil.md5Hex(str);
    }
}
