package com.blt.talk.common.util;

import java.util.Base64;

/**
 * 消息加密/解密类 <br>
 * 需要jni支持，或者把相关加密/解密处理在此实现
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class SecurityUtils {

    public byte[] DecryptMsg(String strMsg) {
        
        byte[] msg = Base64.getDecoder().decode(strMsg);
        return AESUtils.decrypt(msg);
    }

    public byte[] EncryptMsg(String strMsg)  {
        byte[] encrypted =  AESUtils.encrypt(strMsg);

        return encrypted;
    }
    
    private static SecurityUtils m_pInstance;

    public static SecurityUtils getInstance() {
        synchronized (SecurityUtils.class) {
            if (m_pInstance == null) {
                m_pInstance = new SecurityUtils();
            }
            
            return m_pInstance;
        }
    }


}
