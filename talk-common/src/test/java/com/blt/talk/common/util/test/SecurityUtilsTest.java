/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.util.test;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertArrayEquals(o, a);
    }
}
