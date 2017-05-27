/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupUpdateMemberReq;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.GroupInternalService;
import com.blt.talk.service.jpa.entity.IMGroup;
import com.blt.talk.service.jpa.repository.IMGroupRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.remote.GroupService;

/**
 * Group相关业务处理
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
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private GroupInternalService groupInternalService;

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

    /* (non-Javadoc)
     * @see com.blt.talk.service.remote.GroupService#createGroup(com.blt.talk.common.model.entity.GroupEntity)
     */
    @Override
    @PostMapping(path = "/createGroup")
    public BaseModel<Long> createGroup(@RequestBody GroupEntity groupEntity) {

        List<Long> userList = groupEntity.getUserList();
        
        int time = CommonUtils.currentTimeSeconds();
        
        // createGroup: insert IMGroup
        IMGroup group = new IMGroup();
        group.setStatus((byte)DBConstant.GROUP_STATUS_ONLINE);
        group.setName(groupEntity.getMainName());
        group.setCreator(groupEntity.getCreatorId());
        group.setAvatar(groupEntity.getAvatar());
        group.setType((byte)groupEntity.getType());
        group.setUserCnt(userList.size());
        group.setVersion(1);
        group.setLastChated(time);
        group.setUpdated(time);
        group.setCreated(time);
        
        group = groupRepository.save(group);
        
        // createGroup: insert IMGroupMember
        groupInternalService.insertNewMember(groupEntity.getCreatorId(), group.getId(), userList);
        
        BaseModel<Long> createRsp = new BaseModel<>();
        createRsp.setData(group.getId());
        return createRsp;
    }
    
    /**
     * 更改群员
     * @param groupMember 群员信息
     * @return 创建结果:群的现有成员列表
     * @since  1.0
     */
    @PostMapping(path = "/updateMember")
    public BaseModel<List<Long>> changeGroupMember(@RequestBody GroupUpdateMemberReq groupMember) {
        
        List<Long> members = null;
        
        if (groupMember.getUpdType() == IMBaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_ADD) {
            members = groupInternalService.insertNewMember(groupMember.getUserId(), groupMember.getGroupId(), groupMember.getUserIds());
        } else if (groupMember.getUpdType() == IMBaseDefine.GroupModifyType.GROUP_MODIFY_TYPE_DEL) {
            members = groupInternalService.removeMember(groupMember.getUserId(), groupMember.getGroupId(), groupMember.getUserIds());
        } else {
            // 不处理
            BaseModel<List<Long>> changeRsp = new BaseModel<>();
            changeRsp.setResult(GroupCmdResult.PARAM_ERROR);
            return changeRsp;
        }
        
        BaseModel<List<Long>> changeRsp = new BaseModel<>();
        changeRsp.setData(members);
        return changeRsp;
    }
}
