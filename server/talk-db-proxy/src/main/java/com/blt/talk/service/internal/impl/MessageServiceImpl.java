/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.util.SecurityUtils;
import com.blt.talk.service.internal.AudioInternalService;
import com.blt.talk.service.internal.MessageService;
import com.blt.talk.service.internal.RelationShipService;
import com.blt.talk.service.jpa.entity.IMGroupMember;
import com.blt.talk.service.jpa.entity.IMGroupMessage;
import com.blt.talk.service.jpa.entity.IMGroupMessageEntity;
import com.blt.talk.service.jpa.entity.IMMessage;
import com.blt.talk.service.jpa.entity.IMMessageEntity;
import com.blt.talk.service.jpa.repository.IMGroupMemberRepository;
import com.blt.talk.service.jpa.repository.IMGroupMessageRepository;
import com.blt.talk.service.jpa.repository.IMMessageRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.redis.RedisKeys;

/**
 * 消息处理
 * 
 * @author 李春生
 * @version 1.0
 * @since  1.0
 */
@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private RelationShipService relationShipService;
    
    @Autowired
    private IMMessageRepository messageRepository;

    @Autowired
    private IMGroupMessageRepository groupMessageRepository;
    @Autowired
    private IMGroupMemberRepository groupMemberRepository;
    @Autowired
    private AudioInternalService audioInternalService;
    
    @Override
    @Transactional
    public List<UnreadEntity> getUnreadMsgCount(long userId) {

        List<UnreadEntity> unreadList = new ArrayList<>();

        // 查询未读件数
        final String userUnreadKey = RedisKeys.concat(RedisKeys.USER_UNREAD, userId);
        // String unreadKey = "unread_" + userId;
        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
        Map<String, String> mapUnread = hashOptions.entries(userUnreadKey);

        for (Entry<String, String> uread : mapUnread.entrySet()) {
            
            Long fromUserId = Long.valueOf(uread.getKey());
            Long relateId = relationShipService.getRelationId(userId, fromUserId, false);

            // 查询
            UnreadEntity unreadEntity = new UnreadEntity();
            unreadEntity.setPeerId(fromUserId);
            unreadEntity.setSessionType(IMBaseDefine.SessionType.SESSION_TYPE_SINGLE_VALUE);
            unreadEntity.setUnReadCnt(Integer.valueOf(uread.getValue()));

            IMMessageEntity lastMessage = null;
            SearchCriteria<IMMessage> messageSearchCriteria = new SearchCriteria<>();
            messageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
            messageSearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
            Sort sortMessage = Sort.by(Sort.Direction.DESC, "created", "id");
            Pageable pageable = PageRequest.of(0, 1, sortMessage);
            Page<IMMessage> messageList = messageRepository.findAll(messageSearchCriteria, pageable);
            if (messageList.hasContent()) {
                lastMessage = messageList.getContent().get(0);
            }

            if (lastMessage != null) {
                unreadEntity.setLaststMsgId(lastMessage.getMsgId());
                unreadEntity.setLaststMsgType(lastMessage.getType());
                unreadEntity.setLatestMsgFromUserId(lastMessage.getUserId());
                if (lastMessage.getType() == IMBaseDefine.MsgType.MSG_TYPE_SINGLE_TEXT_VALUE) {
                    unreadEntity.setLatestMsgData(lastMessage.getContent());
                } else if (lastMessage.getType() == IMBaseDefine.MsgType.MSG_TYPE_SINGLE_AUDIO_VALUE) {
                    // "[语音]"加密后的字符串
                    byte[] content = SecurityUtils.getInstance().EncryptMsg("[语音]");
                    unreadEntity.setLatestMsgData(Base64Utils.encodeToString(content));
                } else {
                    // 其他
                    unreadEntity.setLatestMsgData(lastMessage.getContent());
                }
            }
            unreadList.add(unreadEntity);
        }

        return unreadList;
    }

    @Override
    @Transactional
    public List<UnreadEntity> getUnreadGroupMsgCount(long userId) {
        // 查询GroupID
        SearchCriteria<IMGroupMember> groupMemberSearchCriteria = new SearchCriteria<>();
        groupMemberSearchCriteria.add(JpaRestrictions.eq("userId", userId, false));
        groupMemberSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        Sort sort = Sort.by(Sort.Direction.DESC, "updated", "id");
        List<IMGroupMember> groupList = groupMemberRepository.findAll(groupMemberSearchCriteria, sort);

        List<UnreadEntity> unreadList = new ArrayList<>();

        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
        if (!groupList.isEmpty()) {
            for (IMGroupMember group : groupList) {
                
                final String groupSetKey = RedisKeys.concat(RedisKeys.GROUP_INFO, group.getGroupId(), RedisKeys.SETTING_INFO);
                final String userUnreadKey = RedisKeys.concat(RedisKeys.GROUP_UNREAD, userId);
                
                // 取群消息数和用户已读数
                String groupCount = hashOptions.get(groupSetKey, RedisKeys.COUNT);
                if (groupCount == null) {
                    continue;
                }
                String userCount =
                        hashOptions.get(userUnreadKey, String.valueOf(group.getGroupId()));
                
                Integer unreadCount = userCount != null ? Integer.valueOf(groupCount) - Integer.valueOf(userCount)
                        : Integer.valueOf(groupCount);
                
                if (unreadCount > 0) {

                    // 取最后一条记录的消息
                    IMGroupMessageEntity lastMessage = null;
                    
                    SearchCriteria<IMGroupMessage> groupMessageSearchCriteria = new SearchCriteria<>();
                    groupMessageSearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                    groupMessageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    Sort sortMessage = Sort.by(Sort.Direction.DESC, "created", "id");
                    Pageable pageable = PageRequest.of(0, 1, sortMessage);
                    Page<IMGroupMessage> groupMessageList =
                            groupMessageRepository.findAll(groupMessageSearchCriteria, pageable);
                    if (groupMessageList.hasContent()) {
                        lastMessage = groupMessageList.getContent().get(0);
                    }

                    UnreadEntity unreadEntity = new UnreadEntity();
                    unreadEntity.setPeerId(group.getGroupId());
                    unreadEntity.setSessionType(IMBaseDefine.SessionType.SESSION_TYPE_GROUP_VALUE);
                    unreadEntity.setLaststMsgType(lastMessage.getType());
                    unreadEntity.setUnReadCnt(unreadCount);
                    if (lastMessage != null) {
                        unreadEntity.setLaststMsgId(lastMessage.getMsgId());
                        unreadEntity.setLaststMsgType(lastMessage.getType());
                        unreadEntity.setLatestMsgFromUserId(lastMessage.getUserId());
                        if (lastMessage.getType() == IMBaseDefine.MsgType.MSG_TYPE_GROUP_TEXT_VALUE) {
                            unreadEntity.setLatestMsgData(lastMessage.getContent());
                        } else if (lastMessage.getType() == IMBaseDefine.MsgType.MSG_TYPE_GROUP_AUDIO_VALUE) {
                            // "[语音]"加密后的字符串
                            byte[] content = SecurityUtils.getInstance().EncryptMsg("[语音]");
                            unreadEntity.setLatestMsgData(Base64Utils.encodeToString(content));
                        } else {
                            // 其他
                            unreadEntity.setLatestMsgData(lastMessage.getContent());
                        }
                    }
                    unreadList.add(unreadEntity);
                }
            }
        }

        return unreadList;
    }
    
    @Override
    @Transactional
    public IMGroupMessageEntity getLatestGroupMessage(long groupId) {

        List<? extends IMGroupMessageEntity> messageList = null;

        SearchCriteria<IMGroupMessage> groupMessageSearchCriteria = new SearchCriteria<>();
        groupMessageSearchCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMessageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        Sort sortMessage = Sort.by(Sort.Direction.DESC, "created", "id");
        Pageable pageable = PageRequest.of(0, 1, sortMessage);
        Page<IMGroupMessage> groupMessageList =
                groupMessageRepository.findAll(groupMessageSearchCriteria, pageable);
        if (groupMessageList.hasContent()) {
            messageList = groupMessageList.getContent();
        }
        
        if (messageList != null && !messageList.isEmpty()) {
            return messageList.get(0);
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.MessageService#getLatestMessage(long, long)
     */
    @Override
    @Transactional
    public IMMessageEntity getLatestMessage(long userId, long toUserId) {
        Long relateId = relationShipService.getRelationId(userId, toUserId, false);

        if (relateId == null || relateId == 0) {
            return null;
        }
        List<? extends IMMessageEntity> messageList = null;
        SearchCriteria<IMMessage> messageSearchCriteria = new SearchCriteria<>();
        messageSearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
        messageSearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
        Sort sortMessage = Sort.by(Sort.Direction.DESC, "created", "id");
        Pageable pageable = PageRequest.of(0, 1, sortMessage);
        Page<IMMessage> messagePageList = messageRepository.findAll(messageSearchCriteria, pageable);
        if (messagePageList.hasContent()) {
            messageList = messagePageList.getContent();
        }
        
        if (messageList != null && !messageList.isEmpty()) {
            return messageList.get(0);
        }
        return null;
    }

    public List<MessageEntity> findGroupMessageList(List<? extends IMGroupMessageEntity> messageList) {
        List<MessageEntity> resultList = new ArrayList<>();
        for (IMGroupMessageEntity message : messageList) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(message.getId());
            messageEntity.setMsgId(message.getMsgId());
            if (message.getType() == IMBaseDefine.MsgType.MSG_TYPE_GROUP_AUDIO_VALUE) {
                // 语音Base64
                byte[] audioData = audioInternalService.readAudioInfo(Long.valueOf(message.getContent()));
                messageEntity.setContent(Base64Utils.encodeToString(audioData));
            } else {
                messageEntity.setContent(message.getContent());
            }
            messageEntity.setFromId(message.getUserId());
            messageEntity.setCreated(message.getCreated());
            messageEntity.setStatus(message.getStatus());
            messageEntity.setMsgType(message.getType());

            resultList.add(messageEntity);
        }
        return resultList;
    }

    public List<MessageEntity> findMessageList(List<? extends IMMessageEntity> messageList) {
        List<MessageEntity> resultList = new ArrayList<>();
        for (IMMessageEntity message : messageList) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setId(message.getId());
            messageEntity.setMsgId(message.getMsgId());
            if (message.getType() == IMBaseDefine.MsgType.MSG_TYPE_SINGLE_AUDIO_VALUE) {
                // 语音Base64
                byte[] audioData = audioInternalService.readAudioInfo(Long.valueOf(message.getContent()));
                messageEntity.setContent(Base64Utils.encodeToString(audioData));
            } else {
                messageEntity.setContent(message.getContent());
            }
            messageEntity.setFromId(message.getUserId());
            messageEntity.setCreated(message.getCreated());
            messageEntity.setStatus(message.getStatus());
            messageEntity.setMsgType(message.getType());
            resultList.add(messageEntity);
        }
        return resultList;
    }
}
