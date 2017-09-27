package com.blt.talk.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Base64;

/**
 * 消息加密/解密类 <br>
 * 需要jni支持，或者把相关加密/解密处理在此实现
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class SecurityUtils {

    /**
     * 解码
     * @param byteMsg base64格式的byte数组
     * @return 解码后的byte数组
     * @since  1.0
     */
    public byte[] DecryptMsg(byte[] byteMsg) {

        if (byteMsg == null || byteMsg.length == 0) {
            return new byte[]{};
        }
        byte[] msg = Base64.decode(byteMsg);
        byte[] pDecData = AESUtils.decrypt(msg);
        if (pDecData == null || pDecData.length < 4) {
            return new byte[]{};
        }
        byte[] lenbytes = Arrays.copyOfRange(pDecData, pDecData.length - 4, pDecData.length);
        int nInLen = CommonUtils.byteArray2int(lenbytes);

        return Arrays.copyOf(pDecData, nInLen);
    }
    
    /**
     * 解码
     * @param strMsg base64格式的字符串
     * @return 解码后的byte数组
     * @since  1.0
     */
    public byte[] DecryptMsg(String strMsg) {

        if (strMsg == null || strMsg.isEmpty()) {
            return new byte[]{};
        }

        byte[] msg = Base64.decode(strMsg);
        byte[] pDecData = AESUtils.decrypt(msg);
        if (pDecData == null || pDecData.length < 4) {
            return new byte[]{};
        }
        byte[] lenbytes = Arrays.copyOfRange(pDecData, pDecData.length - 4, pDecData.length);
        int nInLen = CommonUtils.byteArray2int(lenbytes);

        return Arrays.copyOf(pDecData, nInLen);
    }

    /**
     * 加密
     * @param strMsg 明文
     * @return 加密后的byte数组(base64)
     * @since  1.0
     */
    public byte[] EncryptMsg(String strMsg) {
        try {
            byte[] pInData = strMsg.getBytes("utf8");
            int nInLen = pInData.length;
            int nRemain = nInLen % 16;
            int nBlocks = (nInLen + 15) / 16;

            if (nRemain > 12 || nRemain == 0) {
                nBlocks += 1;
            }
            int nEncryptLen = nBlocks * 16;

            byte[] pData = Arrays.copyOf(pInData, nEncryptLen);

            byte[] lenbytes = CommonUtils.intToBytes(nInLen);

            for (int i = 0; i < 4; i++) {
                pData[nEncryptLen + i - 4] = lenbytes[i];
            }
            byte[] encrypted = AESUtils.encrypt(pData);
            return Base64.encode(encrypted);
        } catch (UnsupportedEncodingException e) {
            return new byte[]{};
        }
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
