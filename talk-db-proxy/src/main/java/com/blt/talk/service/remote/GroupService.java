package com.blt.talk.service.remote;

import java.util.List;
import java.util.Map;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupUpdateMemberReq;

/**
 * Group相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface GroupService {

    /**
     * 查询组信息
     * @param userId
     * @return
     * @since  1.0
     */
    BaseModel<List<GroupEntity>> normalList(long userId);

    /**
     * 查询组的属性
     * @param groupVersionList
     * @return 查询结果:群资料（版本）
     * @since  1.0
     */
    BaseModel<List<GroupEntity>> groupInfoList(List<Long> groupIdList);

    /**
     * 查询组的属性
     * @param groupVersionList
     * @return 查询结果:群资料
     * @since  1.0
     */
    BaseModel<List<GroupEntity>> groupInfoList(Map<String, Integer> groupIdList);
    
    /**
     * 创建群组
     * @param groupEntity 群资料
     * @return 创建结果:新群的ID
     * @since  1.0
     */
    BaseModel<Long> createGroup(GroupEntity groupEntity);
    
    /**
     * 更改群员
     * @param groupMember 群员信息
     * @return 创建结果:群的现有成员列表
     * @since  1.0
     */
    BaseModel<List<Long>> changeGroupMember(GroupUpdateMemberReq groupMember);
}