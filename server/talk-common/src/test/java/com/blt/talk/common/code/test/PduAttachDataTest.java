/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.code.test;

import org.junit.Assert;
import org.junit.Test;

import com.blt.talk.common.code.PduAttachData;
import com.blt.talk.common.constant.AttachType;
import com.google.protobuf.ByteString;

/**
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
public class PduAttachDataTest {

    @Test
    public void testAttach(){
        PduAttachData attachData = new PduAttachData(AttachType.HANDLE_AND_PDU,
                128L, 0);
        byte[] oginBytes = attachData.getBufferData();
        ByteString bytes = ByteString.copyFrom(oginBytes); 
        
        PduAttachData attachData2 = new PduAttachData(bytes);
        
        try {
            byte[] distBytes = attachData2.getBufferData();
            Assert.assertArrayEquals(oginBytes, distBytes);
        } catch (IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
    }
//
//    @Test
//    public void testAttach2(){
//
//        PduAttachData attachData = new PduAttachData(AttachType.HANDLE,
//                128L, 0);
//        try {
//            attachData.getBufferData();
//            Assert.assertTrue(true);
//        } catch (IndexOutOfBoundsException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//    }
}
