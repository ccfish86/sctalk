/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.projection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.blt.talk.service.internal.SequenceService;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SequnceServiceTest {

    @Autowired
    private SequenceService sequenceService;
    
    @Test
    public void testAddAndGet() {

        try {
            sequenceService.addAndGetLong("ssssss", 11);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
