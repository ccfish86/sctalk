package com.blt.talk.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

/**
 * AES加密/解密类
 * <br />
 * 服务器给客户端发送时需要加密
 * <br>
 * 另外，图片和声音如果要存为服务器端文件时，可能需要处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class AESUtils {
    private static final String PWD = "12345678901234567890123456789012";

    protected static SecretKeySpec makeKey() throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException {

         SecretKeySpec keySpec = new SecretKeySpec(PWD.getBytes("UTF-8"),
         "AES");
        return keySpec;
    }

    /**
     * 加密
     * 
     * @param content 需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static byte[] encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            cipher.init(Cipher.ENCRYPT_MODE, makeKey());
            return cipher.doFinal(content.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     * 
     * @param content 待解密内容
     * @return
     */
    public static byte[] decrypt(byte[] content) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey());
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        
        byte[] btss = Base64.decode("3GzlndZ5c31wN4RopZUhfA==");
        byte[] deResult = decrypt(btss);  
        System.out.println("解密后：" + new String(deResult, Charset.forName("UTF8"))); 
    }
}