/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.param.ClearUserCountReq;
import com.blt.talk.common.param.GroupMessageSendReq;
import com.blt.talk.common.param.MessageSendReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public interface MessageService {

    BaseModel<Long> sendMessage(GroupMessageSendReq messageSendReq);

    BaseModel<Long> sendMessage(MessageSendReq messageSendReq);

    BaseModel<List<UnreadEntity>> getUnreadMsgCount(long userId);

    BaseModel<List<UnreadEntity>> getUnreadGroupMsgCount(long userId);

    BaseModel<?> clearUserCounter(ClearUserCountReq userCountReq);

    BaseModel<List<MessageEntity>> getMessageList(long userId, long toId, long messageId, int messageCount);

    BaseModel<List<MessageEntity>> getGroupMessageList(long userId, long groupId, long messageId, int messageCount);

    BaseModel<Long> getLatestMessageId(long userId, long toId);

    BaseModel<Long> getLatestGroupMessageId(long userId, long groupId);

    BaseModel<List<MessageEntity>> getMessageById(long userId, long toId, List<Long> msgIdList);

    BaseModel<List<MessageEntity>> getGroupMessageById(long userId, long groupId, List<Long> msgIdList);
}
