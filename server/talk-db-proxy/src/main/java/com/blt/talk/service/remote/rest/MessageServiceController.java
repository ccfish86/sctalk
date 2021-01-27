/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.SessionType;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.param.ClearUserCountReq;
import com.blt.talk.common.param.GroupMessageSendReq;
import com.blt.talk.common.param.MessageSendReq;
import com.blt.talk.common.result.NormarCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.AudioInternalService;
import com.blt.talk.service.internal.MessageService;
import com.blt.talk.service.internal.RelationShipService;
import com.blt.talk.service.internal.SessionService;
import com.blt.talk.service.jpa.entity.IMGroup;
import com.blt.talk.service.jpa.entity.IMGroupMessage;
import com.blt.talk.service.jpa.entity.IMGroupMessageEntity;
import com.blt.talk.service.jpa.entity.IMMessage;
import com.blt.talk.service.jpa.entity.IMMessageEntity;
import com.blt.talk.service.jpa.repository.IMGroupMessageRepository;
import com.blt.talk.service.jpa.repository.IMGroupRepository;
import com.blt.talk.service.jpa.repository.IMMessageRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.redis.RedisKeys;

/**
 * 消息相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/message")
public class MessageServiceController {

    @Autowired
    private IMGroupRepository groupRepository;
    @Autowired
    private IMMessageRepository messageRepository;
    @Autowired
    private IMGroupMessageRepository groupMessageRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RelationShipService relationShipService;

    @Autowired
    private SessionService sessionService;
    @Autowired
    private AudioInternalService audioInternalService; 
    @Autowired
    private MessageService messageService;
    
    /**
     * 发送消息（群组消息 ）
     * @param messageSendReq 群组消息
     * @return 消息ID
     * @since  1.0
     */
    @PostMapping(path = "/groupMessage/add")
    @Transactional
    public BaseModel<Long> sendMessage(@RequestBody GroupMessageSendReq messageSendReq) {

        byte type = (byte) messageSendReq.getMsgType().getNumber();

        // 存储群消息（ID自增，计数）
        final String groupSetKey = RedisKeys.concat(RedisKeys.GROUP_INFO, messageSendReq.getGroupId(), RedisKeys.SETTING_INFO);

        final HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
        final long msgId = hashOptions.increment(groupSetKey, RedisKeys.GROUP_MESSAGE_ID, 1);
        String content;
        if (messageSendReq.getMsgType() == IMBaseDefine.MsgType.MSG_TYPE_GROUP_AUDIO) {
            
            long audioId = audioInternalService.saveAudioInfo(messageSendReq.getUserId(), messageSendReq.getGroupId(),
                    messageSendReq.getCreateTime(), messageSendReq.getContent());
            
            if (audioId == DBConstant.INVALIAD_VALUE) {
                // 录音保存失败
                return new BaseModel<Long>().setResult(NormarCmdResult.DFS_ERROR);
            }
            
            content = String.valueOf(audioId);
        } else {
            content = messageSendReq.getMsgContent();
        }
        IMGroupMessage groupMessageEntity = new IMGroupMessage();
        groupMessageEntity.setUserId(messageSendReq.getUserId());
        groupMessageEntity.setGroupId(messageSendReq.getGroupId());
        groupMessageEntity.setContent(content);
        groupMessageEntity.setCreated(messageSendReq.getCreateTime());
        groupMessageEntity.setMsgId(msgId);
        groupMessageEntity.setType(type);
        groupMessageEntity.setStatus(DBConstant.DELETE_STATUS_OK);
        groupMessageEntity.setUpdated(messageSendReq.getCreateTime());

        groupMessageRepository.save(groupMessageEntity);
        
        // 更新Session
        long sessionId = sessionService.getSessionId(messageSendReq.getUserId(), messageSendReq.getGroupId(),
                SessionType.SESSION_TYPE_GROUP_VALUE, false);
        if (sessionId == DBConstant.INVALIAD_VALUE) {
            sessionId = sessionService.addSession(messageSendReq.getUserId(), messageSendReq.getGroupId(),
                    SessionType.SESSION_TYPE_GROUP_VALUE);
        }
        
        sessionService.update(sessionId, messageSendReq.getCreateTime());

        // 更新最后消息时间
        IMGroup group = groupRepository.findOne(messageSendReq.getGroupId());
        group.setLastChated(CommonUtils.currentTimeSeconds());
        groupRepository.save(group);

        // 计数
        hashOptions.increment(groupSetKey, RedisKeys.COUNT, 1);
        
        // 未读消息
        final String userUnreadKey = RedisKeys.concat(RedisKeys.GROUP_UNREAD, messageSendReq.getUserId());
        hashOptions.increment(userUnreadKey, String.valueOf(messageSendReq.getGroupId()), 1);

        return new BaseModel<Long>() {
            {
                setData(msgId);
            }
        };
    }

    /**
     * 发送个人消息
     * @param messageSendReq 消息内容
     * @return 消息ID
     * @since  1.0
     */
    @PostMapping(path = "/message/add")
    @Transactional
    public BaseModel<Long> sendMessage(@RequestBody MessageSendReq messageSendReq) {

        byte type = (byte) messageSendReq.getMsgType().getNumber();
        
        // 处理relation_id
        final Long relateId =
                relationShipService.getRelationId(messageSendReq.getUserId(), messageSendReq.getToId(), true);
        
        // 保存关系信息（消息ID，）
        final HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
        final String relKey = RedisKeys.concat(RedisKeys.RELATION_INFO, relateId % 10000);
        Long msgId = hashOptions.increment(relKey, String.valueOf(relateId), 1);

        String content;
        if (messageSendReq.getMsgType() == IMBaseDefine.MsgType.MSG_TYPE_SINGLE_AUDIO) {
            long audioId = audioInternalService.saveAudioInfo(messageSendReq.getUserId(), messageSendReq.getToId(),
                    messageSendReq.getCreateTime(), messageSendReq.getContent());
            if (audioId == DBConstant.INVALIAD_VALUE) {
                // 录音保存失败
                return new BaseModel<Long>().setResult(NormarCmdResult.DFS_ERROR);
            }
            
            content = String.valueOf(audioId);
        } else {
            content = messageSendReq.getMsgContent();
        }
        
        IMMessage messageEntity = new IMMessage();
        messageEntity.setUserId(messageSendReq.getUserId());
        messageEntity.setToId(messageSendReq.getToId());
        messageEntity.setContent(content);
        messageEntity.setCreated(messageSendReq.getCreateTime());
        messageEntity.setMsgId(msgId);
        messageEntity.setRelateId(relateId);
        messageEntity.setType(type);
        messageEntity.setStatus(DBConstant.DELETE_STATUS_OK);
        messageEntity.setUpdated(messageSendReq.getCreateTime());

        messageRepository.save(messageEntity);
        
        // 更新Session
        long sessionId = sessionService.getSessionId(messageSendReq.getUserId(), messageSendReq.getToId(),
                SessionType.SESSION_TYPE_SINGLE_VALUE, false);
        if (sessionId == DBConstant.INVALIAD_VALUE) {
            sessionId = sessionService.addSession(messageSendReq.getUserId(), messageSendReq.getToId(),
                    SessionType.SESSION_TYPE_SINGLE_VALUE);
        }

        sessionService.update(sessionId, messageSendReq.getCreateTime());
        
        // 2019-7-9 update start
        // 参考\db_proxy_server\business\MessageContent.cpp sendMessage
        long peersessionId = sessionService.getSessionId(messageSendReq.getToId(), messageSendReq.getUserId(), 
                SessionType.SESSION_TYPE_SINGLE_VALUE, false);
        if (peersessionId == DBConstant.INVALIAD_VALUE) {
        	peersessionId = sessionService.addSession(messageSendReq.getToId(), messageSendReq.getUserId(),
                    SessionType.SESSION_TYPE_SINGLE_VALUE);
        }

        // 2019-7-9 update end
        sessionService.update(peersessionId, messageSendReq.getCreateTime());

        // 计数
        // 存储用户信息及未读信息
        final String useUreadrKey = RedisKeys.concat(RedisKeys.USER_UNREAD, messageSendReq.getToId());
        hashOptions.increment(useUreadrKey, String.valueOf(messageSendReq.getUserId()), 1);

        BaseModel<Long> messageIdRes = new BaseModel<>();
        messageIdRes.setData(msgId);
        return messageIdRes;
    }

    
    /**
     * 取未读消息
     * @param userId 用户ID
     * @return 未读消息列表
     * @since  1.0
     */
    @GetMapping(path = "/allUnreadCount")
    @Transactional
    public BaseModel<List<UnreadEntity>> getAllUnreadMsgCount(@RequestParam("userId") long userId) {

        BaseModel<List<UnreadEntity>> unreadRes = new BaseModel<>();

        List<UnreadEntity> userUnreadCount = messageService.getUnreadMsgCount(userId);
        List<UnreadEntity> userUnreadGroupCount = messageService.getUnreadGroupMsgCount(userId);

        List<UnreadEntity> unreadList = new ArrayList<>();
        if (userUnreadCount.size() > 0) {
            unreadList.addAll(userUnreadCount);
        }
        if (userUnreadGroupCount.size() > 0) {
            unreadList.addAll(userUnreadGroupCount);
        }

        unreadRes.setData(unreadList);

        return unreadRes;
    }

    /**
     * 消息用户计数
     * @param userCountReq 会话信息
     * @return 更新结果
     * @since  1.0
     */
    @PostMapping("/clearUserCounter")
    public BaseModel<?> clearUserCounter(@RequestBody ClearUserCountReq userCountReq) {

        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();

        if (userCountReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_SINGLE) {
            // Clear P2P msg Counter
            final String userKey = RedisKeys.concat(RedisKeys.USER_UNREAD, userCountReq.getUserId());
            hashOptions.delete(userKey, String.valueOf(userCountReq.getPeerId()));
        } else if (userCountReq.getSessionType() == IMBaseDefine.SessionType.SESSION_TYPE_GROUP) {
            // Clear Group msg Counter
            final String groupSetKey = RedisKeys.concat(RedisKeys.GROUP_INFO, userCountReq.getPeerId(), RedisKeys.SETTING_INFO);
            final String countValue = hashOptions.get(groupSetKey, RedisKeys.COUNT);

            final String userUnreadKey = RedisKeys.concat(RedisKeys.GROUP_UNREAD, userCountReq.getUserId());
            hashOptions.put(userUnreadKey, String.valueOf(userCountReq.getPeerId()), countValue);
        } else {
            logger.warn("参数不正: SessionType={}", userCountReq.getSessionType());
        }
        return null;
    }

    /**
     * 取消息内容列表
     * @param userId 用户ID
     * @param toId 对方ID
     * @param messageId 消息ID（查询起始）
     * @param messageCount 消息数
     * @return 消息内容列表
     * @since  1.0
     */
    @GetMapping("/message/messageList")
    @Transactional
    public BaseModel<List<MessageEntity>> getMessageList(@RequestParam("userId") long userId,
            @RequestParam("toId") long toId, @RequestParam("messageId") long messageId,
            @RequestParam("messageCount") int messageCount) {

        Long relateId = relationShipService.getRelationId(userId, toId, false);

        if (relateId == null || relateId == 0) {
            return new BaseModel<>();
        }
        Long splt = relateId % 10;
        List<MessageEntity> messageList = null;
        SearchCriteria<IMMessage> messageSearchCriteria = new SearchCriteria<>();
        messageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        messageSearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
        if (messageId > 0) {
        	//当消息ID参数指定了的时候
        	messageSearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
        }
        Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
        Pageable pageable = new PageRequest(0, messageCount, sortMessage);
        Page<IMMessage> messagePageList = messageRepository.findAll(messageSearchCriteria, pageable);
        if (messagePageList.hasContent()) {
            messageList = messageService.findMessageList(messagePageList.getContent());
        }
        
        BaseModel<List<MessageEntity>> messageListRes = new BaseModel<>();
        messageListRes.setData(messageList);
        return messageListRes;
    }

    /**
     * 取消息内容列表(群组)
     * @param userId 用户ID
     * @param toId 对方ID
     * @param messageId 消息ID（查询起始）
     * @param messageCount 消息数
     * @return 消息内容列表
     * @since  1.0
     */
    @GetMapping("/groupMessage/messageList")
    @Transactional
    public BaseModel<List<MessageEntity>> getGroupMessageList(@RequestParam("userId") long userId,
            @RequestParam("groupId") long groupId, @RequestParam("messageId") long messageId,
            @RequestParam("messageCount") int messageCount) {

        Long splt = groupId % 10;
        List<MessageEntity> messageList = null;

        SearchCriteria<IMGroupMessage> groupMessageSearchCriteria = new SearchCriteria<>();
        groupMessageSearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMessageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        if (messageId > 0) {
        	//当消息ID参数指定了的时候
        	groupMessageSearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
        }
        Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
        Pageable pageable = new PageRequest(0, messageCount, sortMessage);
        Page<IMGroupMessage> groupMessageList =
                groupMessageRepository.findAll(groupMessageSearchCriteria, pageable);
        if (groupMessageList.hasContent()) {
            messageList = messageService.findGroupMessageList(groupMessageList.getContent());
        }
        
        BaseModel<List<MessageEntity>> messageListRes = new BaseModel<>();
        messageListRes.setData(messageList);
        return messageListRes;
    }

    /**
     * 取最后一个消息ID
     * @param userId 用户不D
     * @param toId 对方ID
     * @return 消息ID
     * @since  1.0
     */
    @GetMapping(path = "/message/latestId")
    public BaseModel<Long> getLatestMessageId(@RequestParam("userId") long userId, @RequestParam("toId") long toId) {
        
        IMMessageEntity messageEntity = messageService.getLatestMessage(userId, toId);
        BaseModel<Long> messageIdRes = new BaseModel<>();
        
        if (messageEntity != null) {
            messageIdRes.setData(messageEntity.getMsgId());
        }
        return messageIdRes;
    }

    /**
     * 取最后一个消息ID
     * @param userId 用户不D
     * @param toId 对方ID
     * @return 消息ID
     * @since  1.0
     */
    @GetMapping(path = "/groupMessage/latestId")
    @Transactional
    public BaseModel<Long> getLatestGroupMessageId(@RequestParam("userId")long userId,
            @RequestParam("groupId") long groupId) {
        
        IMGroupMessageEntity message = messageService.getLatestGroupMessage(groupId);
        
        BaseModel<Long> messageIdRes = new BaseModel<>();
        if (message != null) {
            messageIdRes.setData(message.getMsgId());
        }
        return messageIdRes;
    }

    /**
     * 根据ID查取消息内容列表
     * @param userId 用户ID
     * @param toId 对方ID
     * @param msgIdList 消息ID列表
     * @return 消息内容列表
     * @since  1.0
     */
    @GetMapping(path = "/message/byId")
    @Transactional
    public BaseModel<List<MessageEntity>> getMessageById(@RequestParam("userId") long userId, @RequestParam("toId") long toId,
            @RequestParam("messageId") List<Long> msgIdList) {
        Long relateId = relationShipService.getRelationId(userId, toId, false);

        if (relateId == null || relateId == 0) {
            return new BaseModel<>();
        }
        List<MessageEntity> messageList = null;
        SearchCriteria<IMMessage> messageSearchCriteria = new SearchCriteria<>();
        messageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        messageSearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
        messageSearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
        Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
        List<IMMessage> messagePageList = messageRepository.findAll(messageSearchCriteria, sortMessage);
        if (!messagePageList.isEmpty()) {
            messageList = messageService.findMessageList(messagePageList);
        }

        BaseModel<List<MessageEntity>> messageListRes = new BaseModel<>();
        messageListRes.setData(messageList);
        
        return messageListRes;
    }

    /**
     * 根据ID查取消息内容列表(群组)
     * @param userId 用户ID
     * @param toId 对方ID
     * @param msgIdList 消息ID列表
     * @return 消息内容列表
     * @since  1.0
     */
    @GetMapping(path = "/groupMessage/byId")
    @Transactional
    public BaseModel<List<MessageEntity>> getGroupMessageById(@RequestParam("userId") long userId, @RequestParam("groupId") long groupId,
            @RequestParam("messageId") List<Long> msgIdList) {
        
        List<MessageEntity> messageList = null;

        SearchCriteria<IMGroupMessage> groupMessageSearchCriteria = new SearchCriteria<>();
        groupMessageSearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMessageSearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
        groupMessageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
        List<IMGroupMessage> groupMessageList =
                groupMessageRepository.findAll(groupMessageSearchCriteria, sortMessage);
        if (!groupMessageList.isEmpty()) {
            messageList = messageService.findGroupMessageList(groupMessageList);
        }
        
        BaseModel<List<MessageEntity>> messageIdRes = new BaseModel<>();
        
        if (messageList != null && !messageList.isEmpty()) {
            messageIdRes.setData(messageList);
        }
        return messageIdRes;
    }
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
