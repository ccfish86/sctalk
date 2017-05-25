/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupInsertNewMemberReq;
import com.blt.talk.common.param.GroupRemoveMemberReq;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.jpa.entity.IMGroup;
import com.blt.talk.service.jpa.entity.IMGroupMember;
import com.blt.talk.service.jpa.repository.IMGroupMemberRepository;
import com.blt.talk.service.jpa.repository.IMGroupRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.remote.GroupService;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/group")
public class GroupServiceController implements GroupService {

    @Autowired
    private IMGroupRepository groupRepository;
    @Autowired
    private IMGroupMemberRepository groupMemberRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    

    @GetMapping(path = "/normalList")
    @Override
    public BaseModel<List<GroupEntity>> normalList(@RequestParam("userId") long userId) {

        BaseModel<List<GroupEntity>> groupRes = new BaseModel<>();
        List<IMGroup> groups = groupRepository.findByUserId(userId);
        if (groups.isEmpty()) {
            return groupRes;
        }

        List<GroupEntity> resData = new ArrayList<>();
        groupRes.setData(resData);
        groups.forEach(group -> {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setId(Long.valueOf(group.getId()));
            groupEntity.setAvatar(group.getAvatar());
            groupEntity.setCreated(group.getCreated());
            groupEntity.setCreatorId(group.getCreator());
            groupEntity.setGroupType(group.getType());
            groupEntity.setStatus(group.getStatus());
            groupEntity.setMainName(group.getName());
            groupEntity.setVersion(group.getVersion());
            resData.add(groupEntity);
        });

        return groupRes;
    }

    /**
     * 追加新成员，并显示最新的用户
     * 
     * @param newMemberReq
     * @return
     * @since 1.0
     */
    @Override
    @PostMapping(path = "insertNewMember")
    public BaseModel<List<Long>> insertNewMember(@RequestBody GroupInsertNewMemberReq newMemberReq) {

        // 追加更新群组成员
        byte status = 0;
        int time = CommonUtils.currentTimeSeconds();
        List<Long> userIds = newMemberReq.getUserIds();

        // 查询已有成员
        SearchCriteria<IMGroupMember> groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", newMemberReq.getGroupId(), false));
        groupMemeberCriteria.add(JpaRestrictions.in("userId", userIds, false));

        List<IMGroupMember> groupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        List<Long> userIdForInsert;
        if (groupMembers.isEmpty()) {
            userIdForInsert = userIds;
        } else {
            userIdForInsert = new ArrayList<>();
            userIdForInsert = userIds.stream().filter(id -> {
                for (IMGroupMember groupMember : groupMembers) {
                    if (groupMember.getId() == id) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
            groupMembers.forEach(groupMember -> {
                groupMember.setStatus(status);
                groupMember.setUpdated(time);
            });
        }

        // 追加
        userIdForInsert.forEach(userId -> {
            IMGroupMember groupMember = new IMGroupMember();
            groupMember.setGroupId(newMemberReq.getGroupId());
            groupMember.setStatus(status);
            groupMember.setUserId(userId);
            groupMember.setCreated(time);
            groupMember.setUpdated(time);
            groupMembers.add(groupMember);
        });

        groupMemberRepository.save(groupMembers);

        // 返回新成员
        BaseModel<List<Long>> groupMemberRes = new BaseModel<>();

        groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", newMemberReq.getGroupId(), false));
        groupMemeberCriteria.add(JpaRestrictions.eq("status", status, false));
        List<IMGroupMember> allGroupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        List<Long> allGroupUsers = new ArrayList<>();
        allGroupMembers.forEach(member -> {
            allGroupUsers.add(member.getUserId());
        });

        groupMemberRes.setData(allGroupUsers);
        return groupMemberRes;
    }

    /**
     * 删除成员，并显示最新的用户
     * 
     * @param newMemberReq
     * @return
     * @since 1.0
     */
    @Override
    @PostMapping(path = "removeMember")
    public BaseModel<List<Long>> removeMember(@RequestBody GroupRemoveMemberReq newMemberReq) {

        byte status = 1;
        int time = CommonUtils.currentTimeSeconds();

        List<Integer> userIds = newMemberReq.getUserIds();
        // 查询已有成员
        SearchCriteria<IMGroupMember> groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", newMemberReq.getGroupId(), false));
        groupMemeberCriteria.add(JpaRestrictions.in("userId", userIds, false));

        List<IMGroupMember> groupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        // 更新为删除状态
        groupMembers.forEach(memeber -> {
            memeber.setStatus(status);
            memeber.setUpdated(time);
        });
        groupMemberRepository.save(groupMembers);

        // 返回新成员
        BaseModel<List<Long>> groupMemberRes = new BaseModel<>();

        groupMemeberCriteria = new SearchCriteria<>();
        groupMemeberCriteria.add(JpaRestrictions.eq("groupId", newMemberReq.getGroupId(), false));
        groupMemeberCriteria.add(JpaRestrictions.eq("status", status, false));
        List<IMGroupMember> allGroupMembers = groupMemberRepository.findAll(groupMemeberCriteria);

        List<Long> allGroupUsers = new ArrayList<>();
        allGroupMembers.forEach(member -> {
            allGroupUsers.add(member.getUserId());
        });

        groupMemberRes.setData(allGroupUsers);
        return groupMemberRes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.service.remote.GroupService#groupInfoList(java.util.List)
     */
    @Override
    @GetMapping(path = "/groupInfoList")
    public BaseModel<List<GroupEntity>> groupInfoList(@RequestParam("groupIdList") List<Long> groupIdList) {
        
        SearchCriteria<IMGroup> groupSearchCriteria = new SearchCriteria<>();
        groupSearchCriteria.add(JpaRestrictions.in("id", groupIdList, false));
        Sort sort = new Sort(Sort.Direction.DESC, "updated");
        
        List<IMGroup> groups = groupRepository.findAll(groupSearchCriteria, sort);

        List<GroupEntity> resData = new ArrayList<>();
        for (IMGroup group: groups) {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setId(Long.valueOf(group.getId()));
            groupEntity.setAvatar(group.getAvatar());
            groupEntity.setCreated(group.getCreated());
            groupEntity.setCreatorId(group.getCreator());
            groupEntity.setGroupType(group.getType());
            groupEntity.setStatus(group.getStatus());
            groupEntity.setMainName(group.getName());
            groupEntity.setVersion(group.getVersion());
            resData.add(groupEntity);
            
            // fillGroupMember
            String key = "group_member_" + group.getId();
            HashOperations<String, String, String> groupMapOps = redisTemplate.opsForHash();
            Map<String, String> groupMemberMap = groupMapOps.entries(key);
            List<Long> userIds = new ArrayList<>();
            if (groupMemberMap != null) {
                for (String memberId : groupMemberMap.keySet()) {
                    userIds.add(Long.valueOf(memberId));
                }
            }
            groupEntity.setUserList(userIds);
        }
        BaseModel<List<GroupEntity>> groupRes = new BaseModel<>();
        groupRes.setData(resData);
        
        return groupRes;
    }

    @Override
    @PostMapping(path = "/infoList")
    public BaseModel<List<GroupEntity>> groupInfoList(@RequestBody Map<String, Integer> groupIdList) {
        
        List<Long> groupIds = new ArrayList<>();
        for (String id: groupIdList.keySet()) {
            groupIds.add(Long.valueOf(id));
        }
        
        SearchCriteria<IMGroup> groupSearchCriteria = new SearchCriteria<>();
        groupSearchCriteria.add(JpaRestrictions.in("id", groupIds, false));
        Sort sort = new Sort(Sort.Direction.DESC, "updated");
        
        List<IMGroup> groups = groupRepository.findAll(groupSearchCriteria, sort);

        List<GroupEntity> resData = new ArrayList<>();
        for (IMGroup group: groups) {
            
            int version = groupIdList.get(String.valueOf(group.getId()));
            if (version < group.getVersion()) {
                
                GroupEntity groupEntity = new GroupEntity();
                groupEntity.setId(Long.valueOf(group.getId()));
                groupEntity.setAvatar(group.getAvatar());
                groupEntity.setCreated(group.getCreated());
                groupEntity.setCreatorId(group.getCreator());
                groupEntity.setGroupType(group.getType());
                groupEntity.setStatus(group.getStatus());
                groupEntity.setMainName(group.getName());
                groupEntity.setVersion(group.getVersion());
                resData.add(groupEntity);
                
                // fillGroupMember
                String key = "group_member_" + group.getId();
                HashOperations<String, String, String> groupMapOps = redisTemplate.opsForHash();
                Map<String, String> groupMemberMap = groupMapOps.entries(key);
                List<Long> userIds = new ArrayList<>();
                if (groupMemberMap != null) {
                    for (String memberId : groupMemberMap.keySet()) {
                        userIds.add(Long.valueOf(memberId));
                    }
                }
                groupEntity.setUserList(userIds);
            }
        }
        BaseModel<List<GroupEntity>> groupRes = new BaseModel<>();
        groupRes.setData(resData);
        
        return groupRes;
    }
}
