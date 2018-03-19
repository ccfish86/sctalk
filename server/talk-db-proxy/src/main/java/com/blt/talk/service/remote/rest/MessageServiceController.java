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
import org.springframework.util.Base64Utils;
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
import com.blt.talk.common.result.ResultEnum;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.AudioInternalService;
import com.blt.talk.service.internal.MessageService;
import com.blt.talk.service.internal.RelationShipService;
import com.blt.talk.service.internal.SessionService;
import com.blt.talk.service.jpa.entity.IMGroup;
import com.blt.talk.service.jpa.entity.IMGroupMessage0;
import com.blt.talk.service.jpa.entity.IMGroupMessage1;
import com.blt.talk.service.jpa.entity.IMGroupMessage2;
import com.blt.talk.service.jpa.entity.IMGroupMessage3;
import com.blt.talk.service.jpa.entity.IMGroupMessage4;
import com.blt.talk.service.jpa.entity.IMGroupMessage5;
import com.blt.talk.service.jpa.entity.IMGroupMessage6;
import com.blt.talk.service.jpa.entity.IMGroupMessage7;
import com.blt.talk.service.jpa.entity.IMGroupMessage8;
import com.blt.talk.service.jpa.entity.IMGroupMessage9;
import com.blt.talk.service.jpa.entity.IMGroupMessageEntity;
import com.blt.talk.service.jpa.entity.IMMessage0;
import com.blt.talk.service.jpa.entity.IMMessage1;
import com.blt.talk.service.jpa.entity.IMMessage2;
import com.blt.talk.service.jpa.entity.IMMessage3;
import com.blt.talk.service.jpa.entity.IMMessage4;
import com.blt.talk.service.jpa.entity.IMMessage5;
import com.blt.talk.service.jpa.entity.IMMessage6;
import com.blt.talk.service.jpa.entity.IMMessage7;
import com.blt.talk.service.jpa.entity.IMMessage8;
import com.blt.talk.service.jpa.entity.IMMessage9;
import com.blt.talk.service.jpa.entity.IMMessageEntity;
import com.blt.talk.service.jpa.repository.IMGroupMessage0Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage1Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage2Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage3Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage4Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage5Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage6Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage7Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage8Repository;
import com.blt.talk.service.jpa.repository.IMGroupMessage9Repository;
import com.blt.talk.service.jpa.repository.IMGroupRepository;
import com.blt.talk.service.jpa.repository.IMMessage0Repository;
import com.blt.talk.service.jpa.repository.IMMessage1Repository;
import com.blt.talk.service.jpa.repository.IMMessage2Repository;
import com.blt.talk.service.jpa.repository.IMMessage3Repository;
import com.blt.talk.service.jpa.repository.IMMessage4Repository;
import com.blt.talk.service.jpa.repository.IMMessage5Repository;
import com.blt.talk.service.jpa.repository.IMMessage6Repository;
import com.blt.talk.service.jpa.repository.IMMessage7Repository;
import com.blt.talk.service.jpa.repository.IMMessage8Repository;
import com.blt.talk.service.jpa.repository.IMMessage9Repository;
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
    private IMMessage0Repository message0Repository;
    @Autowired
    private IMMessage1Repository message1Repository;
    @Autowired
    private IMMessage2Repository message2Repository;
    @Autowired
    private IMMessage3Repository message3Repository;
    @Autowired
    private IMMessage4Repository message4Repository;
    @Autowired
    private IMMessage5Repository message5Repository;
    @Autowired
    private IMMessage6Repository message6Repository;
    @Autowired
    private IMMessage7Repository message7Repository;
    @Autowired
    private IMMessage8Repository message8Repository;
    @Autowired
    private IMMessage9Repository message9Repository;

    @Autowired
    private IMGroupMessage0Repository groupMessage0Repository;
    @Autowired
    private IMGroupMessage1Repository groupMessage1Repository;
    @Autowired
    private IMGroupMessage2Repository groupMessage2Repository;
    @Autowired
    private IMGroupMessage3Repository groupMessage3Repository;
    @Autowired
    private IMGroupMessage4Repository groupMessage4Repository;
    @Autowired
    private IMGroupMessage5Repository groupMessage5Repository;
    @Autowired
    private IMGroupMessage6Repository groupMessage6Repository;
    @Autowired
    private IMGroupMessage7Repository groupMessage7Repository;
    @Autowired
    private IMGroupMessage8Repository groupMessage8Repository;
    @Autowired
    private IMGroupMessage9Repository groupMessage9Repository;

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
        IMGroupMessageEntity groupMessageEntity = IMGroupMessageEntity.getInstance(messageSendReq.getGroupId());
        groupMessageEntity.setUserId(messageSendReq.getUserId());
        groupMessageEntity.setGroupId(messageSendReq.getGroupId());
        groupMessageEntity.setContent(content);
        groupMessageEntity.setCreated(messageSendReq.getCreateTime());
        groupMessageEntity.setMsgId(msgId);
        groupMessageEntity.setType(type);
        groupMessageEntity.setStatus(DBConstant.DELETE_STATUS_OK);
        groupMessageEntity.setUpdated(messageSendReq.getCreateTime());

        Long splt = messageSendReq.getGroupId() % 10;
        switch (splt.intValue()) {
            case 0:
                groupMessage0Repository.save((IMGroupMessage0) groupMessageEntity);
                break;
            case 1:
                groupMessage1Repository.save((IMGroupMessage1) groupMessageEntity);
                break;
            case 2:
                groupMessage2Repository.save((IMGroupMessage2) groupMessageEntity);
                break;
            case 3:
                groupMessage3Repository.save((IMGroupMessage3) groupMessageEntity);
                break;
            case 4:
                groupMessage4Repository.save((IMGroupMessage4) groupMessageEntity);
                break;
            case 5:
                groupMessage5Repository.save((IMGroupMessage5) groupMessageEntity);
                break;
            case 6:
                groupMessage6Repository.save((IMGroupMessage6) groupMessageEntity);
                break;
            case 7:
                groupMessage7Repository.save((IMGroupMessage7) groupMessageEntity);
                break;
            case 8:
                groupMessage8Repository.save((IMGroupMessage8) groupMessageEntity);
                break;
            case 9:
                groupMessage9Repository.save((IMGroupMessage9) groupMessageEntity);
                break;
            default:
                break;
        }
        
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
        
        IMMessageEntity messageEntity = IMMessageEntity.getInstance(relateId);
        messageEntity.setUserId(messageSendReq.getUserId());
        messageEntity.setToId(messageSendReq.getToId());
        messageEntity.setContent(content);
        messageEntity.setCreated(messageSendReq.getCreateTime());
        messageEntity.setMsgId(msgId);
        messageEntity.setRelateId(relateId);
        messageEntity.setType(type);
        messageEntity.setStatus(DBConstant.DELETE_STATUS_OK);
        messageEntity.setUpdated(messageSendReq.getCreateTime());

        Long splt = relateId % 10;
        switch (splt.intValue()) {
            case 0:
                message0Repository.save((IMMessage0) messageEntity);
                break;
            case 1:
                message1Repository.save((IMMessage1) messageEntity);
                break;
            case 2:
                message2Repository.save((IMMessage2) messageEntity);
                break;
            case 3:
                message3Repository.save((IMMessage3) messageEntity);
                break;
            case 4:
                message4Repository.save((IMMessage4) messageEntity);
                break;
            case 5:
                message5Repository.save((IMMessage5) messageEntity);
                break;
            case 6:
                message6Repository.save((IMMessage6) messageEntity);
                break;
            case 7:
                message7Repository.save((IMMessage7) messageEntity);
                break;
            case 8:
                message8Repository.save((IMMessage8) messageEntity);
                break;
            case 9:
                message9Repository.save((IMMessage9) messageEntity);
                break;
            default:
                break;
        }
        
        // 更新Session
        long sessionId = sessionService.getSessionId(messageSendReq.getUserId(), messageSendReq.getToId(),
                SessionType.SESSION_TYPE_SINGLE_VALUE, false);
        if (sessionId == DBConstant.INVALIAD_VALUE) {
            sessionId = sessionService.addSession(messageSendReq.getUserId(), messageSendReq.getToId(),
                    SessionType.SESSION_TYPE_SINGLE_VALUE);
        }
        
        sessionService.update(sessionId, messageSendReq.getCreateTime());

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
        switch (splt.intValue()) {
            case 0:
                SearchCriteria<IMMessage0> message0SearchCriteria = new SearchCriteria<>();
                message0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message0SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message0SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable = new PageRequest(0, messageCount, sortMessage);
                Page<IMMessage0> message0List = message0Repository.findAll(message0SearchCriteria, pageable);
                if (message0List.hasContent()) {
                    messageList = messageService.findMessageList(message0List.getContent());
                }
                break;
            case 1:
                SearchCriteria<IMMessage1> message1SearchCriteria = new SearchCriteria<>();
                message1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message1SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message1SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable1 = new PageRequest(0, messageCount, sortMessage1);
                Page<IMMessage1> message1List = message1Repository.findAll(message1SearchCriteria, pageable1);
                if (message1List.hasContent()) {
                    messageList = messageService.findMessageList(message1List.getContent());
                }
                break;
            case 2:
                SearchCriteria<IMMessage2> message2SearchCriteria = new SearchCriteria<>();
                message2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message2SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message2SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable2 = new PageRequest(0, messageCount, sortMessage2);
                Page<IMMessage2> message2List = message2Repository.findAll(message2SearchCriteria, pageable2);
                if (message2List.hasContent()) {
                    messageList = messageService.findMessageList(message2List.getContent());
                }
                break;
            case 3:
                SearchCriteria<IMMessage3> message3SearchCriteria = new SearchCriteria<>();
                message3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message3SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message3SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable3 = new PageRequest(0, messageCount, sortMessage3);
                Page<IMMessage3> message3List = message3Repository.findAll(message3SearchCriteria, pageable3);
                if (message3List.hasContent()) {
                    messageList = messageService.findMessageList(message3List.getContent());
                }
                break;
            case 4:
                SearchCriteria<IMMessage4> message4SearchCriteria = new SearchCriteria<>();
                message4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message4SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message4SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable4 = new PageRequest(0, messageCount, sortMessage4);
                Page<IMMessage4> message4List = message4Repository.findAll(message4SearchCriteria, pageable4);
                if (message4List.hasContent()) {
                    messageList = messageService.findMessageList(message4List.getContent());
                }
                break;
            case 5:
                SearchCriteria<IMMessage5> message5SearchCriteria = new SearchCriteria<>();
                message5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message5SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message5SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable5 = new PageRequest(0, messageCount, sortMessage5);
                Page<IMMessage5> message5List = message5Repository.findAll(message5SearchCriteria, pageable5);
                if (message5List.hasContent()) {
                    messageList = messageService.findMessageList(message5List.getContent());
                }
                break;
            case 6:
                SearchCriteria<IMMessage6> message6SearchCriteria = new SearchCriteria<>();
                message6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message6SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message6SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable6 = new PageRequest(0, messageCount, sortMessage6);
                Page<IMMessage6> message6List = message6Repository.findAll(message6SearchCriteria, pageable6);
                if (message6List.hasContent()) {
                    messageList = messageService.findMessageList(message6List.getContent());
                }
                break;
            case 7:
                SearchCriteria<IMMessage7> message7SearchCriteria = new SearchCriteria<>();
                message7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message7SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message7SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable7 = new PageRequest(0, messageCount, sortMessage7);
                Page<IMMessage7> message7List = message7Repository.findAll(message7SearchCriteria, pageable7);
                if (message7List.hasContent()) {
                    messageList = messageService.findMessageList(message7List.getContent());
                }
                break;
            case 8:
                SearchCriteria<IMMessage8> message8SearchCriteria = new SearchCriteria<>();
                message8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message8SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message8SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable8 = new PageRequest(0, messageCount, sortMessage8);
                Page<IMMessage8> message8List = message8Repository.findAll(message8SearchCriteria, pageable8);
                if (message8List.hasContent()) {
                    messageList = messageService.findMessageList(message8List.getContent());
                }
                break;
            case 9:
                SearchCriteria<IMMessage9> message9SearchCriteria = new SearchCriteria<>();
                message9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message9SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message9SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable9 = new PageRequest(0, messageCount, sortMessage9);
                Page<IMMessage9> message9List = message9Repository.findAll(message9SearchCriteria, pageable9);
                if (message9List.hasContent()) {
                    messageList = messageService.findMessageList(message9List.getContent());
                }
                break;
            default:
                break;
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

        switch (splt.intValue()) {
            case 0:
                SearchCriteria<IMGroupMessage0> groupMessage0SearchCriteria = new SearchCriteria<>();
                groupMessage0SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage0SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable = new PageRequest(0, messageCount, sortMessage);
                Page<IMGroupMessage0> groupMessage0List =
                        groupMessage0Repository.findAll(groupMessage0SearchCriteria, pageable);
                if (groupMessage0List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage0List.getContent());
                }
                break;
            case 1:
                SearchCriteria<IMGroupMessage1> groupMessage1SearchCriteria = new SearchCriteria<>();
                groupMessage1SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage1SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable1 = new PageRequest(0, messageCount, sortMessage1);
                Page<IMGroupMessage1> groupMessage1List =
                        groupMessage1Repository.findAll(groupMessage1SearchCriteria, pageable1);
                if (groupMessage1List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage1List.getContent());
                }
                break;
            case 2:
                SearchCriteria<IMGroupMessage2> groupMessage2SearchCriteria = new SearchCriteria<>();
                groupMessage2SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage2SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable2 = new PageRequest(0, messageCount, sortMessage2);
                Page<IMGroupMessage2> groupMessage2List =
                        groupMessage2Repository.findAll(groupMessage2SearchCriteria, pageable2);
                if (groupMessage2List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage2List.getContent());
                }
                break;
            case 3:
                SearchCriteria<IMGroupMessage3> groupMessage3SearchCriteria = new SearchCriteria<>();
                groupMessage3SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage3SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable3 = new PageRequest(0, messageCount, sortMessage3);
                Page<IMGroupMessage3> groupMessage3List =
                        groupMessage3Repository.findAll(groupMessage3SearchCriteria, pageable3);
                if (groupMessage3List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage3List.getContent());
                }
                break;
            case 4:
                SearchCriteria<IMGroupMessage4> groupMessage4SearchCriteria = new SearchCriteria<>();
                groupMessage4SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage4SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable4 = new PageRequest(0, messageCount, sortMessage4);
                Page<IMGroupMessage4> groupMessage4List =
                        groupMessage4Repository.findAll(groupMessage4SearchCriteria, pageable4);
                if (groupMessage4List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage4List.getContent());
                }
                break;
            case 5:
                SearchCriteria<IMGroupMessage5> groupMessage5SearchCriteria = new SearchCriteria<>();
                groupMessage5SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage5SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable5 = new PageRequest(0, messageCount, sortMessage5);
                Page<IMGroupMessage5> groupMessage5List =
                        groupMessage5Repository.findAll(groupMessage5SearchCriteria, pageable5);
                if (groupMessage5List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage5List.getContent());
                }
                break;
            case 6:
                SearchCriteria<IMGroupMessage6> groupMessage6SearchCriteria = new SearchCriteria<>();
                groupMessage6SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage6SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable6 = new PageRequest(0, messageCount, sortMessage6);
                Page<IMGroupMessage6> groupMessage6List =
                        groupMessage6Repository.findAll(groupMessage6SearchCriteria, pageable6);
                if (groupMessage6List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage6List.getContent());
                }
                break;
            case 7:
                SearchCriteria<IMGroupMessage7> groupMessage7SearchCriteria = new SearchCriteria<>();
                groupMessage7SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage7SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable7 = new PageRequest(0, messageCount, sortMessage7);
                Page<IMGroupMessage7> groupMessage7List =
                        groupMessage7Repository.findAll(groupMessage7SearchCriteria, pageable7);
                if (groupMessage7List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage7List.getContent());
                }
                break;
            case 8:
                SearchCriteria<IMGroupMessage8> groupMessage8SearchCriteria = new SearchCriteria<>();
                groupMessage8SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage8SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable8 = new PageRequest(0, messageCount, sortMessage8);
                Page<IMGroupMessage8> groupMessage8List =
                        groupMessage8Repository.findAll(groupMessage8SearchCriteria, pageable8);
                if (groupMessage8List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage8List.getContent());
                }
                break;
            case 9:
                SearchCriteria<IMGroupMessage9> groupMessage9SearchCriteria = new SearchCriteria<>();
                groupMessage9SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                groupMessage9SearchCriteria.add(JpaRestrictions.lte("msgId", messageId, false));
                Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                Pageable pageable9 = new PageRequest(0, messageCount, sortMessage9);
                Page<IMGroupMessage9> groupMessage9List =
                        groupMessage9Repository.findAll(groupMessage9SearchCriteria, pageable9);
                if (groupMessage9List.hasContent()) {
                    messageList = messageService.findGroupMessageList(groupMessage9List.getContent());
                }
                break;
            default:
                break;
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
        Long splt = relateId % 10;
        List<MessageEntity> messageList = null;
        switch (splt.intValue()) {
            case 0:
                SearchCriteria<IMMessage0> message0SearchCriteria = new SearchCriteria<>();
                message0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message0SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message0SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage0> message0List = message0Repository.findAll(message0SearchCriteria, sortMessage);
                if (!message0List.isEmpty()) {
                    messageList = messageService.findMessageList(message0List);
                }
                break;
            case 1:
                SearchCriteria<IMMessage1> message1SearchCriteria = new SearchCriteria<>();
                message1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message1SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message1SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage1> message1List = message1Repository.findAll(message1SearchCriteria, sortMessage1);
                if (!message1List.isEmpty()) {
                    messageList = messageService.findMessageList(message1List);
                }
                break;
            case 2:
                SearchCriteria<IMMessage2> message2SearchCriteria = new SearchCriteria<>();
                message2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message2SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message2SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage2> message2List = message2Repository.findAll(message2SearchCriteria, sortMessage2);
                if (!message2List.isEmpty()) {
                    messageList = messageService.findMessageList(message2List);
                }
                break;
            case 3:
                SearchCriteria<IMMessage3> message3SearchCriteria = new SearchCriteria<>();
                message3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message3SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message3SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage3> message3List = message3Repository.findAll(message3SearchCriteria, sortMessage3);
                if (!message3List.isEmpty()) {
                    messageList = messageService.findMessageList(message3List);
                }
                break;
            case 4:
                SearchCriteria<IMMessage4> message4SearchCriteria = new SearchCriteria<>();
                message4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message4SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message4SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage4> message4List = message4Repository.findAll(message4SearchCriteria, sortMessage4);
                if (!message4List.isEmpty()) {
                    messageList = messageService.findMessageList(message4List);
                }
                break;
            case 5:
                SearchCriteria<IMMessage5> message5SearchCriteria = new SearchCriteria<>();
                message5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message5SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message5SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage5> message5List = message5Repository.findAll(message5SearchCriteria, sortMessage5);
                if (!message5List.isEmpty()) {
                    messageList = messageService.findMessageList(message5List);
                }
                break;
            case 6:
                SearchCriteria<IMMessage6> message6SearchCriteria = new SearchCriteria<>();
                message6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message6SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message6SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage6> message6List = message6Repository.findAll(message6SearchCriteria, sortMessage6);
                if (!message6List.isEmpty()) {
                    messageList = messageService.findMessageList(message6List);
                }
                break;
            case 7:
                SearchCriteria<IMMessage7> message7SearchCriteria = new SearchCriteria<>();
                message7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message7SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message7SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage7> message7List = message7Repository.findAll(message7SearchCriteria, sortMessage7);
                if (!message7List.isEmpty()) {
                    messageList = messageService.findMessageList(message7List);
                }
                break;
            case 8:
                SearchCriteria<IMMessage8> message8SearchCriteria = new SearchCriteria<>();
                message8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message8SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message8SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage8> message8List = message8Repository.findAll(message8SearchCriteria, sortMessage8);
                if (!message8List.isEmpty()) {
                    messageList = messageService.findMessageList(message8List);
                }
                break;
            case 9:
                SearchCriteria<IMMessage9> message9SearchCriteria = new SearchCriteria<>();
                message9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                message9SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                message9SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMMessage9> message9List = message9Repository.findAll(message9SearchCriteria, sortMessage9);
                if (!message9List.isEmpty()) {
                    messageList = messageService.findMessageList(message9List);
                }
                break;
            default:
                break;
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
        
        Long splt = groupId % 10;
        List<MessageEntity> messageList = null;

        switch (splt.intValue()) {
            case 0:
                SearchCriteria<IMGroupMessage0> groupMessage0SearchCriteria = new SearchCriteria<>();
                groupMessage0SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage0SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage0> groupMessage0List =
                        groupMessage0Repository.findAll(groupMessage0SearchCriteria, sortMessage);
                if (!groupMessage0List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage0List);
                }
                break;
            case 1:
                SearchCriteria<IMGroupMessage1> groupMessage1SearchCriteria = new SearchCriteria<>();
                groupMessage1SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage1SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage1> groupMessage1List =
                        groupMessage1Repository.findAll(groupMessage1SearchCriteria, sortMessage1);
                if (!groupMessage1List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage1List);
                }
                break;
            case 2:
                SearchCriteria<IMGroupMessage2> groupMessage2SearchCriteria = new SearchCriteria<>();
                groupMessage2SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage2SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage2> groupMessage2List =
                        groupMessage2Repository.findAll(groupMessage2SearchCriteria, sortMessage2);
                if (!groupMessage2List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage2List);
                }
                break;
            case 3:
                SearchCriteria<IMGroupMessage3> groupMessage3SearchCriteria = new SearchCriteria<>();
                groupMessage3SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage3SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage3> groupMessage3List =
                        groupMessage3Repository.findAll(groupMessage3SearchCriteria, sortMessage3);
                if (!groupMessage3List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage3List);
                }
                break;
            case 4:
                SearchCriteria<IMGroupMessage4> groupMessage4SearchCriteria = new SearchCriteria<>();
                groupMessage4SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage4SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage4> groupMessage4List =
                        groupMessage4Repository.findAll(groupMessage4SearchCriteria, sortMessage4);
                if (!groupMessage4List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage4List);
                }
                break;
            case 5:
                SearchCriteria<IMGroupMessage5> groupMessage5SearchCriteria = new SearchCriteria<>();
                groupMessage5SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage5SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage5> groupMessage5List =
                        groupMessage5Repository.findAll(groupMessage5SearchCriteria, sortMessage5);
                if (!groupMessage5List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage5List);
                }
                break;
            case 6:
                SearchCriteria<IMGroupMessage6> groupMessage6SearchCriteria = new SearchCriteria<>();
                groupMessage6SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage6SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage6> groupMessage6List =
                        groupMessage6Repository.findAll(groupMessage6SearchCriteria, sortMessage6);
                if (!groupMessage6List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage6List);
                }
                break;
            case 7:
                SearchCriteria<IMGroupMessage7> groupMessage7SearchCriteria = new SearchCriteria<>();
                groupMessage7SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage7SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage7> groupMessage7List =
                        groupMessage7Repository.findAll(groupMessage7SearchCriteria, sortMessage7);
                if (!groupMessage7List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage7List);
                }
                break;
            case 8:
                SearchCriteria<IMGroupMessage8> groupMessage8SearchCriteria = new SearchCriteria<>();
                groupMessage8SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage8SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage8> groupMessage8List =
                        groupMessage8Repository.findAll(groupMessage8SearchCriteria, sortMessage8);
                if (!groupMessage8List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage8List);
                }
                break;
            case 9:
                SearchCriteria<IMGroupMessage9> groupMessage9SearchCriteria = new SearchCriteria<>();
                groupMessage9SearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
                groupMessage9SearchCriteria.add(JpaRestrictions.in("msgId", msgIdList, false));
                groupMessage9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                List<IMGroupMessage9> groupMessage9List =
                        groupMessage9Repository.findAll(groupMessage9SearchCriteria, sortMessage9);
                if (!groupMessage9List.isEmpty()) {
                    messageList = messageService.findGroupMessageList(groupMessage9List);
                }
                break;
            default:
                break;
        }
        
        BaseModel<List<MessageEntity>> messageIdRes = new BaseModel<>();
        
        if (messageList != null && !messageList.isEmpty()) {
            messageIdRes.setData(messageList);
        }
        return messageIdRes;
    }
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
