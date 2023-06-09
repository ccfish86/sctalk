/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.param.ClearUserCountReq;
import com.blt.talk.common.param.GroupMessageSendReq;
import com.blt.talk.common.param.MessageSendReq;

/**
 * 消息处理Service
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient(name = "talk-db-server", contextId = "message")
public interface MessageService {

    /**
     * 保存消息并返回消息ID
     * 
     * @param messageSendReq 消息内容
     * @return 消息ID
     * @since  1.0
     */
    @PostMapping(path = "/message/message/add")
    BaseModel<Long> sendMessage(MessageSendReq messageSendReq);
    
    /**
     * 保存消息并返回消息ID
     * 
     * @param messageSendReq 消息内容
     * @return 消息ID
     * @since  1.0
     */
    @PostMapping(path = "/message/groupMessage/add")
    BaseModel<Long> sendMessage(GroupMessageSendReq messageSendReq);
    
    @GetMapping(path = "/message/allUnreadCount")
    BaseModel<List<UnreadEntity>> getAllUnreadMsgCount(@RequestParam("userId") long userId);
    
    @PostMapping(path = "/message/clearUserCounter")
    BaseModel<?> clearUserCounter(@RequestBody ClearUserCountReq userCountReq);
    
    @GetMapping(path = "/message/message/messageList")
    BaseModel<List<MessageEntity>> getMessageList(@RequestParam("userId") long userId, @RequestParam("toId") long toId,
            @RequestParam("messageId") long messageId, @RequestParam("messageCount") int messageCount);
    
    @GetMapping(path = "/message/groupMessage/messageList")
    BaseModel<List<MessageEntity>> getGroupMessageList(@RequestParam("userId")long userId,
            @RequestParam("groupId") long groupId,  @RequestParam("messageId") long messageId, @RequestParam("messageCount") int messageCount);

    @GetMapping(path = "/message/message/latestId")
    BaseModel<Long> getLatestMessageId(@RequestParam("userId")long userId, @RequestParam("toId") long toId);
    
    @GetMapping(path = "/message/groupMessage/latestId")
    BaseModel<Long> getLatestGroupMessageId(@RequestParam("userId")long userId, @RequestParam("groupId") long groupId);
    
    @GetMapping(path = "/message/message/byId")
    BaseModel<List<MessageEntity>> getMessageById(@RequestParam("userId") long userId, @RequestParam("toId") long toId,
            @RequestParam("messageId") List<Long> msgIdList);
    
    @GetMapping(path = "/message/groupMessage/byId")
    BaseModel<List<MessageEntity>> getGroupMessageById(@RequestParam("userId") long userId, @RequestParam("groupId") long groupId,
            @RequestParam("messageId") List<Long> msgIdList);
}
