/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.GroupInternalService;
import com.blt.talk.service.internal.SessionService;
import com.blt.talk.service.jpa.entity.IMGroupMember;
import com.blt.talk.service.jpa.repository.IMGroupMemberRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Service
public class GroupInternalServiceImpl implements GroupInternalService {

    @Autowired
    private IMGroupMemberRepository groupMemberRepository;
    
    @Autowired
    private SessionService sessionService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.GroupInternalService#insertNewMember(long, long, java.util.List)
     */
    @Override
    public List<Long> insertNewMember(long userId, long groupId, List<Long> members) {

        // 追加更新群组成员
        int time = CommonUtils.currentTimeSeconds();

        // 查询已有成员
        SearchCriteria<IMGroupMember> groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMemeberCriteria.add(JpaRestrictions.in("userId", members, false));

        List<IMGroupMember> groupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        List<Long> userIdForInsert;
        if (groupMembers.isEmpty()) {
            userIdForInsert = members;
        } else {
            userIdForInsert = new ArrayList<>();
            userIdForInsert = members.stream().filter(id -> {
                for (IMGroupMember groupMember : groupMembers) {
                    if (groupMember.getId() == id) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
            groupMembers.forEach(groupMember -> {
                groupMember.setStatus(DBConstant.GROUP_MODIFY_TYPE_ADD);
                groupMember.setUpdated(time);
            });
        }

        // 追加
        userIdForInsert.forEach(member -> {
            IMGroupMember groupMember = new IMGroupMember();
            groupMember.setGroupId(groupId);
            groupMember.setStatus(DBConstant.GROUP_MODIFY_TYPE_ADD);
            groupMember.setUserId(member);
            groupMember.setCreated(time);
            groupMember.setUpdated(time);
            groupMembers.add(groupMember);
        });

        groupMemberRepository.save(groupMembers);

        groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMemeberCriteria.add(JpaRestrictions.eq("status", DBConstant.GROUP_MODIFY_TYPE_ADD, false));
        List<IMGroupMember> allGroupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        // Redis组
        Map<String, String> memberHash = new HashMap<>();
        
        List<Long> allGroupUsers = new ArrayList<>();
        allGroupMembers.forEach(member -> {
            memberHash.put(member.getUserId().toString(), String.valueOf(member.getUpdated()));
            allGroupUsers.add(member.getUserId());
        });
        
        String key = "group_member_" + groupId;
        HashOperations<String, String, String> groupMemberHash = redisTemplate.opsForHash();
        groupMemberHash.putAll(key, memberHash);

        // 返回新成员
        return allGroupUsers;
    }

    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.GroupInternalService#removeMember(long, long, java.util.List)
     */
    @Override
    public List<Long> removeMember(long userId, long groupId, List<Long> members) {

        int time = CommonUtils.currentTimeSeconds();

        // 查询已有成员
        SearchCriteria<IMGroupMember> groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMemeberCriteria.add(JpaRestrictions.in("userId", members, false));

        List<IMGroupMember> groupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        // 更新为删除状态
        groupMembers.forEach(memeber -> {
            memeber.setStatus(DBConstant.GROUP_MODIFY_TYPE_DEL);
            memeber.setUpdated(time);
        });
        groupMemberRepository.save(groupMembers);

        groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", groupId, false));
        groupMemeberCriteria.add(JpaRestrictions.eq("status", DBConstant.GROUP_MODIFY_TYPE_ADD, false));
        List<IMGroupMember> allGroupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        List<Long> allGroupUsers = new ArrayList<>();
        allGroupMembers.forEach(member -> {
            allGroupUsers.add(member.getUserId());
        });
        
        // 从Redis里删除成员
        // 从IMCurrentSession里删除会话
        String key = "group_member_" + groupId;
        HashOperations<String, Long, Integer> groupMemberHash = redisTemplate.opsForHash();
        // CGroupModel#removeSession(nGroupId, setUserId);
        for (Long member: members) {
            groupMemberHash.delete(key, member.toString());
            long sessionId = sessionService.getSessionId(member, groupId, IMBaseDefine.SessionType.SESSION_TYPE_GROUP.getNumber(), false);
            sessionService.remove(sessionId);
        }
        
        // 返回新成员
        return allGroupUsers;
    }

    @Override
    public boolean isValidate(long groupId) {
        return redisTemplate.hasKey("group_member_" + groupId);
    }
}
