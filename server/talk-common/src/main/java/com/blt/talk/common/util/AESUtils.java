package com.blt.talk.common.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static SecretKeySpec keySpec;

    /** AES/ECB/NoPadding:为兼容c++，手动补位（最后4byte补长度）, AES/ECB/ZeroBytePadding */
    private static final String PADDING = "AES/ECB/NoPadding";
    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

    protected static SecretKeySpec makeKey() throws NoSuchAlgorithmException {

        if (keySpec != null) {
            return keySpec;
        } else {
            synchronized (SecretKeySpec.class) {
                final byte[] pwdBytes =  PWD.getBytes(StandardCharsets.UTF_8);
                keySpec = new SecretKeySpec(pwdBytes,
                        "AES");
               return keySpec;
            }
        }
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static byte[] encrypt(byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, makeKey());
            return cipher.doFinal(content);
        } catch (InvalidKeyException ie) {
            logger.error("当前的JDK，不支持128以上密钥。", ie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 加密
     * 
     * @param content 需要加密的内容
     * @return
     */
    public static byte[] encrypt(String content) {
        return encrypt(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解密
     * 
     * @param content 待解密内容
     * @return
     */
    public static byte[] decrypt(byte[] content) {

        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, makeKey());
            return cipher.doFinal(content);
        } catch (InvalidKeyException ie) {
            logger.error("当前的JDK，不支持128以上密钥。", ie);
        } catch (Exception e) {
            logger.warn("数据无法解密，{}", content);
            logger.error("解密异常", e);
        }
        return null;
    }

}