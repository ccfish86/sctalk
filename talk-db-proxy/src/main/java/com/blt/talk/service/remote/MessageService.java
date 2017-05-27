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
 * 消息相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public interface MessageService {

    /**
     * 发送消息（群组消息 ）
     * @param messageSendReq 群组消息
     * @return 消息ID
     * @since  1.0
     */
    BaseModel<Long> sendMessage(GroupMessageSendReq messageSendReq);

    /**
     * 发送个人消息
     * @param messageSendReq 消息内容
     * @return 消息ID
     * @since  1.0
     */
    BaseModel<Long> sendMessage(MessageSendReq messageSendReq);

    /**
     * 取未读消息
     * @param userId 用户ID
     * @return 未读消息列表
     * @since  1.0
     */
    BaseModel<List<UnreadEntity>> getUnreadMsgCount(long userId);

    /**
     * 取未读消息（群组）
     * @param userId 用户ID
     * @return 未读消息列表
     * @since  1.0
     */
    BaseModel<List<UnreadEntity>> getUnreadGroupMsgCount(long userId);

    /**
     * 消息用户计数
     * @param userCountReq 会话信息
     * @return 更新结果
     * @since  1.0
     */
    BaseModel<?> clearUserCounter(ClearUserCountReq userCountReq);

    /**
     * 取消息内容列表
     * @param userId 用户ID
     * @param toId 对方ID
     * @param messageId 消息ID（查询起始）
     * @param messageCount 消息数
     * @return 消息内容列表
     * @since  1.0
     */
    BaseModel<List<MessageEntity>> getMessageList(long userId, long toId, long messageId, int messageCount);
    
    /**
     * 取消息内容列表(群组)
     * @param userId 用户ID
     * @param toId 对方ID
     * @param messageId 消息ID（查询起始）
     * @param messageCount 消息数
     * @return 消息内容列表
     * @since  1.0
     */
    BaseModel<List<MessageEntity>> getGroupMessageList(long userId, long groupId, long messageId, int messageCount);

    /**
     * 取最后一个消息ID
     * @param userId 用户不D
     * @param toId 对方ID
     * @return 消息ID
     * @since  1.0
     */
    BaseModel<Long> getLatestMessageId(long userId, long toId);

    /**
     * 取最后一个消息ID
     * @param userId 用户不D
     * @param toId 对方ID
     * @return 消息ID
     * @since  1.0
     */
    BaseModel<Long> getLatestGroupMessageId(long userId, long groupId);

    /**
     * 根据ID查取消息内容列表
     * @param userId 用户ID
     * @param toId 对方ID
     * @param msgIdList 消息ID列表
     * @return 消息内容列表
     * @since  1.0
     */
    BaseModel<List<MessageEntity>> getMessageById(long userId, long toId, List<Long> msgIdList);

    /**
     * 根据ID查取消息内容列表(群组)
     * @param userId 用户ID
     * @param toId 对方ID
     * @param msgIdList 消息ID列表
     * @return 消息内容列表
     * @since  1.0
     */
    BaseModel<List<MessageEntity>> getGroupMessageById(long userId, long groupId, List<Long> msgIdList);
}
