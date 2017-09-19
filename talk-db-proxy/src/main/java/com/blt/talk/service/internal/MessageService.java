/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

import java.util.List;

import com.blt.talk.common.model.entity.UnreadEntity;

/**
 * 消息处理
 * 
 * @author 李春生
 * @version 1.0
 * @since  1.0
 */
public interface MessageService {

    
    /**
     * 取未读消息
     * @param userId 用户ID
     * @return 未读消息列表
     * @since  1.0
     */
    
    List<UnreadEntity> getUnreadMsgCount(long userId);
    
    /**
     * 取未读消息（群组）
     * @param userId 用户ID
     * @return 未读消息列表
     * @since  1.0
     */
    
    List<UnreadEntity> getUnreadGroupMsgCount(long userId); 
    
}
