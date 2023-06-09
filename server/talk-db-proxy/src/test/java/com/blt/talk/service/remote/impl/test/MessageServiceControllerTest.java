/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.service.remote.rest.MessageServiceController;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@SpringBootTest
public class MessageServiceControllerTest {

    @Autowired
    MessageServiceController messageService;
    
    @Test
    public void testGetMessageList() {
        
        BaseModel<List<MessageEntity>> res = messageService.getMessageList(1, 5, 999999, 50);
        
        System.out.println(res.getData());
    }
}
