package net.lab1024.smartadmin.service.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Slf4j
public class SmartAesUtil {


    /**
     * 加密返回的数据转换成 String 类型
     *
     * @param content 明文
     * @param key     秘钥
     * @param iv      初始化向量是16位长度的字符串
     * @return
     */
    public static String encrypt(String content, String key, String iv) {
        return base64ToString(AES_CBC_Encrypt(content.getBytes(), key.getBytes(), iv.getBytes()));
    }

    /**
     * 将解密返回的数据转换成 String 类型
     *
     * @param content Base64编码的密文
     * @param key     秘钥
     * @param iv      初始化向量是16位长度的字符串
     * @return
     */
    public static String decrypt(String content, String key, String iv) {
        return new String(AES_CBC_Decrypt(stringToBase64(content), key.getBytes(), iv.getBytes()));
    }

    private static byte[] AES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    private static byte[] AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    /**
     * 字符串装换成 Base64
     */

    public static byte[] stringToBase64(String key) {
        return Base64.getDecoder().decode(key.getBytes());
    }

    /**
     * Base64装换成字符串
     */
    public static String base64ToString(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String args[]) throws Exception {

        String content = "明文 123 abc";
        String KEY = "c4ca4238a0b923820dcc509a6f75849b";
        String IV = KEY.substring(0, 16);
        ;
        //加密
        String encrypted = encrypt(content, KEY, IV);
        //解密
        String decrypted = decrypt(encrypted, KEY, IV);

        System.out.println("加密前：" + content);

        System.out.println("加密后：" + encrypted);

        System.out.println("解密后：" + decrypted);
    }


}
