/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.util.test;

import org.junit.jupiter.api.Test;

import com.blt.talk.common.util.AESUtils;
import com.blt.talk.common.util.SecurityUtils;
import com.google.protobuf.ByteString;

/**
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
public class SecurityUtilsTest {

    @Test
    public void testDe() {
        String msg = "brMcxcmVDxcd1IAs1z4LsXWXrZFfV+UBo37vcxL/NUY=";
        ByteString b = ByteString.copyFromUtf8(msg);
        byte[] o = b.toByteArray();
        
        byte[] r = SecurityUtils.getInstance().DecryptMsg(o);
        byte[] a =SecurityUtils.getInstance().EncryptMsg(new String(r));
        
        //System.out.println(new String(r));
//        Assert.assertArrayEquals(o, a);
    }
    
    @Test
    public void testDe2() {
        byte[] xb = new byte[]{85, 120, -26, -18, -99, -119, 120, -103, -40, 29, 0, -99, 23, -32, 68, -34, 117, -105, -83, -111, 95, 87, -27, 1, -93, 126, -17, 115, 18, -1, 53, 70};
        byte[] rs = AESUtils.decrypt(xb);
        System.out.println(new String(rs));
    }
}
