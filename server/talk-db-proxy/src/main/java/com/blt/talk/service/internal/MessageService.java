/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Base64Utils;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.service.jpa.entity.IMGroupMessageEntity;
import com.blt.talk.service.jpa.entity.IMMessageEntity;

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
    
    /**
     * 最后一条消息
     * @param userId 用户ID
     * @param toUserId 用户ID
     * @return 消息（最后一条）
     * @since  1.0
     */
    IMMessageEntity getLatestMessage(long userId, long toUserId);
    /**
     * 最后一条消息
     * @param groupId 群ID
     * @return 消息（最后一条）
     * @since  1.0
     */
    IMGroupMessageEntity getLatestGroupMessage(long groupId);
    
    List<MessageEntity> findGroupMessageList(List<? extends IMGroupMessageEntity> messageList);

    List<MessageEntity> findMessageList(List<? extends IMMessageEntity> messageList);
}
