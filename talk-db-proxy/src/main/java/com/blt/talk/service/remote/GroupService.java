package com.blt.talk.service.remote;

import java.util.List;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupInsertNewMemberReq;
import com.blt.talk.common.param.GroupRemoveMemberReq;

public interface GroupService {

    /**
     * 查询组信息
     * @param userId
     * @return
     * @since  1.0
     */
    BaseModel<List<GroupEntity>> normalList(long userId);

    /**
     * 追加新成员，并显示最新的用户
     * @param newMemberReq
     * @return
     * @since  1.0
     */
    BaseModel<List<Long>> insertNewMember(GroupInsertNewMemberReq newMemberReq);

    /**
     * 删除成员，并显示最新的用户
     * @param newMemberReq
     * @return
     * @since  1.0
     */
    BaseModel<List<Long>> removeMember(GroupRemoveMemberReq newMemberReq);
    
    /**
     * 查询组的属性
     * @param groupVersionList
     * @return
     * @since  1.0
     */
    BaseModel<List<GroupEntity>> groupInfoList(List<Long> groupIdList);
    
}