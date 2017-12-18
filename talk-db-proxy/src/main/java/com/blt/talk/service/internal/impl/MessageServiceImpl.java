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
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.util.SecurityUtils;
import com.blt.talk.service.internal.MessageService;
import com.blt.talk.service.internal.RelationShipService;
import com.blt.talk.service.jpa.entity.IMGroupMember;
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
import com.blt.talk.service.jpa.repository.IMGroupMemberRepository;
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
    private IMGroupMemberRepository groupMemberRepository;
    
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

            Long splt = relateId % 10;
            IMMessageEntity lastMessage = null;
            switch (splt.intValue()) {
                case 0:
                    SearchCriteria<IMMessage0> message0SearchCriteria = new SearchCriteria<>();
                    message0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message0SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable = new PageRequest(0, 1, sortMessage);
                    Page<IMMessage0> message0List = message0Repository.findAll(message0SearchCriteria, pageable);
                    if (message0List.hasContent()) {
                        lastMessage = message0List.getContent().get(0);
                    }
                    break;
                case 1:
                    SearchCriteria<IMMessage1> message1SearchCriteria = new SearchCriteria<>();
                    message1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message1SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable1 = new PageRequest(0, 1, sortMessage1);
                    Page<IMMessage1> message1List = message1Repository.findAll(message1SearchCriteria, pageable1);
                    if (message1List.hasContent()) {
                        lastMessage = message1List.getContent().get(0);
                    }
                    break;
                case 2:
                    SearchCriteria<IMMessage2> message2SearchCriteria = new SearchCriteria<>();
                    message2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message2SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable2 = new PageRequest(0, 1, sortMessage2);
                    Page<IMMessage2> message2List = message2Repository.findAll(message2SearchCriteria, pageable2);
                    if (message2List.hasContent()) {
                        lastMessage = message2List.getContent().get(0);
                    }
                    break;
                case 3:
                    SearchCriteria<IMMessage3> message3SearchCriteria = new SearchCriteria<>();
                    message3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message3SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable3 = new PageRequest(0, 1, sortMessage3);
                    Page<IMMessage3> message3List = message3Repository.findAll(message3SearchCriteria, pageable3);
                    if (message3List.hasContent()) {
                        lastMessage = message3List.getContent().get(0);
                    }
                    break;
                case 4:
                    SearchCriteria<IMMessage4> message4SearchCriteria = new SearchCriteria<>();
                    message4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message4SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable4 = new PageRequest(0, 1, sortMessage4);
                    Page<IMMessage4> message4List = message4Repository.findAll(message4SearchCriteria, pageable4);
                    if (message4List.hasContent()) {
                        lastMessage = message4List.getContent().get(0);
                    }
                    break;
                case 5:
                    SearchCriteria<IMMessage5> message5SearchCriteria = new SearchCriteria<>();
                    message5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message5SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable5 = new PageRequest(0, 1, sortMessage5);
                    Page<IMMessage5> message5List = message5Repository.findAll(message5SearchCriteria, pageable5);
                    if (message5List.hasContent()) {
                        lastMessage = message5List.getContent().get(0);
                    }
                    break;
                case 6:
                    SearchCriteria<IMMessage6> message6SearchCriteria = new SearchCriteria<>();
                    message6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message6SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable6 = new PageRequest(0, 1, sortMessage6);
                    Page<IMMessage6> message6List = message6Repository.findAll(message6SearchCriteria, pageable6);
                    if (message6List.hasContent()) {
                        lastMessage = message6List.getContent().get(0);
                    }
                    break;
                case 7:
                    SearchCriteria<IMMessage7> message7SearchCriteria = new SearchCriteria<>();
                    message7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message7SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable7 = new PageRequest(0, 1, sortMessage7);
                    Page<IMMessage7> message7List = message7Repository.findAll(message7SearchCriteria, pageable7);
                    if (message7List.hasContent()) {
                        lastMessage = message7List.getContent().get(0);
                    }
                    break;
                case 8:
                    SearchCriteria<IMMessage8> message8SearchCriteria = new SearchCriteria<>();
                    message8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message8SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable8 = new PageRequest(0, 1, sortMessage8);
                    Page<IMMessage8> message8List = message8Repository.findAll(message8SearchCriteria, pageable8);
                    if (message8List.hasContent()) {
                        lastMessage = message8List.getContent().get(0);
                    }
                    break;
                case 9:
                    SearchCriteria<IMMessage9> message9SearchCriteria = new SearchCriteria<>();
                    message9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                    message9SearchCriteria.add(JpaRestrictions.eq("relateId", relateId, false));
                    Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                    Pageable pageable9 = new PageRequest(0, 1, sortMessage9);
                    Page<IMMessage9> message9List = message9Repository.findAll(message9SearchCriteria, pageable9);
                    if (message9List.hasContent()) {
                        lastMessage = message9List.getContent().get(0);
                    }
                    break;
                default:
                    break;
            }

            if (lastMessage != null) {
                unreadEntity.setLaststMsgId(lastMessage.getMsgId());
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
        Sort sort = new Sort(Sort.Direction.DESC, "updated", "id");
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
                    Long splt = group.getGroupId() % 10;
                    IMGroupMessageEntity lastMessage = null;
                    switch (splt.intValue()) {
                        case 0:
                            SearchCriteria<IMGroupMessage0> groupMessage0SearchCriteria = new SearchCriteria<>();
                            groupMessage0SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage0SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable = new PageRequest(0, 1, sortMessage);
                            Page<IMGroupMessage0> groupMessage0List =
                                    groupMessage0Repository.findAll(groupMessage0SearchCriteria, pageable);
                            if (groupMessage0List.hasContent()) {
                                lastMessage = groupMessage0List.getContent().get(0);
                            }
                            break;
                        case 1:
                            SearchCriteria<IMGroupMessage1> groupMessage1SearchCriteria = new SearchCriteria<>();
                            groupMessage1SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage1SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage1 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable1 = new PageRequest(0, 1, sortMessage1);
                            Page<IMGroupMessage1> groupMessage1List =
                                    groupMessage1Repository.findAll(groupMessage1SearchCriteria, pageable1);
                            if (groupMessage1List.hasContent()) {
                                lastMessage = groupMessage1List.getContent().get(0);
                            }
                            break;
                        case 2:
                            SearchCriteria<IMGroupMessage2> groupMessage2SearchCriteria = new SearchCriteria<>();
                            groupMessage2SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage2SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage2 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable2 = new PageRequest(0, 1, sortMessage2);
                            Page<IMGroupMessage2> groupMessage2List =
                                    groupMessage2Repository.findAll(groupMessage2SearchCriteria, pageable2);
                            if (groupMessage2List.hasContent()) {
                                lastMessage = groupMessage2List.getContent().get(0);
                            }
                            break;
                        case 3:
                            SearchCriteria<IMGroupMessage3> groupMessage3SearchCriteria = new SearchCriteria<>();
                            groupMessage3SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage3SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage3 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable3 = new PageRequest(0, 1, sortMessage3);
                            Page<IMGroupMessage3> groupMessage3List =
                                    groupMessage3Repository.findAll(groupMessage3SearchCriteria, pageable3);
                            if (groupMessage3List.hasContent()) {
                                lastMessage = groupMessage3List.getContent().get(0);
                            }
                            break;
                        case 4:
                            SearchCriteria<IMGroupMessage4> groupMessage4SearchCriteria = new SearchCriteria<>();
                            groupMessage4SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage4SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage4 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable4 = new PageRequest(0, 1, sortMessage4);
                            Page<IMGroupMessage4> groupMessage4List =
                                    groupMessage4Repository.findAll(groupMessage4SearchCriteria, pageable4);
                            if (groupMessage4List.hasContent()) {
                                lastMessage = groupMessage4List.getContent().get(0);
                            }
                            break;
                        case 5:
                            SearchCriteria<IMGroupMessage5> groupMessage5SearchCriteria = new SearchCriteria<>();
                            groupMessage5SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage5SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage5 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable5 = new PageRequest(0, 1, sortMessage5);
                            Page<IMGroupMessage5> groupMessage5List =
                                    groupMessage5Repository.findAll(groupMessage5SearchCriteria, pageable5);
                            if (groupMessage5List.hasContent()) {
                                lastMessage = groupMessage5List.getContent().get(0);
                            }
                            break;
                        case 6:
                            SearchCriteria<IMGroupMessage6> groupMessage6SearchCriteria = new SearchCriteria<>();
                            groupMessage6SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage6SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage6 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable6 = new PageRequest(0, 1, sortMessage6);
                            Page<IMGroupMessage6> groupMessage6List =
                                    groupMessage6Repository.findAll(groupMessage6SearchCriteria, pageable6);
                            if (groupMessage6List.hasContent()) {
                                lastMessage = groupMessage6List.getContent().get(0);
                            }
                            break;
                        case 7:
                            SearchCriteria<IMGroupMessage7> groupMessage7SearchCriteria = new SearchCriteria<>();
                            groupMessage7SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage7SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage7 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable7 = new PageRequest(0, 1, sortMessage7);
                            Page<IMGroupMessage7> groupMessage7List =
                                    groupMessage7Repository.findAll(groupMessage7SearchCriteria, pageable7);
                            if (groupMessage7List.hasContent()) {
                                lastMessage = groupMessage7List.getContent().get(0);
                            }
                            break;
                        case 8:
                            SearchCriteria<IMGroupMessage8> groupMessage8SearchCriteria = new SearchCriteria<>();
                            groupMessage8SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage8SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage8 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable8 = new PageRequest(0, 1, sortMessage8);
                            Page<IMGroupMessage8> groupMessage8List =
                                    groupMessage8Repository.findAll(groupMessage8SearchCriteria, pageable8);
                            if (groupMessage8List.hasContent()) {
                                lastMessage = groupMessage8List.getContent().get(0);
                            }
                            break;
                        case 9:
                            SearchCriteria<IMGroupMessage9> groupMessage9SearchCriteria = new SearchCriteria<>();
                            groupMessage9SearchCriteria.add(JpaRestrictions.eq("groupId", group.getGroupId(), false));
                            groupMessage9SearchCriteria.add(JpaRestrictions.eq("status", DBConstant.DELETE_STATUS_OK, false));
                            Sort sortMessage9 = new Sort(Sort.Direction.DESC, "created", "id");
                            Pageable pageable9 = new PageRequest(0, 1, sortMessage9);
                            Page<IMGroupMessage9> groupMessage9List =
                                    groupMessage9Repository.findAll(groupMessage9SearchCriteria, pageable9);
                            if (groupMessage9List.hasContent()) {
                                lastMessage = groupMessage9List.getContent().get(0);
                            }
                            break;
                        default:
                            break;
                    }

                    UnreadEntity unreadEntity = new UnreadEntity();
                    unreadEntity.setPeerId(group.getGroupId());
                    unreadEntity.setSessionType(IMBaseDefine.SessionType.SESSION_TYPE_GROUP_VALUE);
                    unreadEntity.setLaststMsgType(lastMessage.getType());
                    unreadEntity.setUnReadCnt(unreadCount);
                    if (lastMessage != null) {
                        unreadEntity.setLaststMsgId(lastMessage.getMsgId());
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

}
