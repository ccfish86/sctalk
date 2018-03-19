package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.IMGroupMember;
import com.grpc.java.kernel.mybatis.mapper.IMGroupMemberMapper;
import com.grpc.java.service.IGroupMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
@Service("groupMemberService")
public class GroupMemberServiceImpl implements IGroupMemberService {

    @Resource
    private IMGroupMemberMapper groupMemberMapper;

    @Override
    public IMGroupMember getGroupMemberById(Integer Id) {
        return groupMemberMapper.selectGroupMemberById(Id);
    }

    @Override
    public IMGroupMember getGroupMemberByGroupId(Integer groupId) {
       return groupMemberMapper.selectGroupMemberByGroupId(groupId);
    }

    @Override
    public IMGroupMember getGroupMemberByUserId(Integer userId) {

        return groupMemberMapper.selectGroupMemberByUserId(userId);
    }


    @Override
    public List<IMGroupMember> getAllGroupMember() {
        return groupMemberMapper.selectAllGroupMember();
    }

    @Override
    public Boolean addGroupMember(IMGroupMember user) {
        return groupMemberMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean deleteGroupMember(Integer id) {
        return groupMemberMapper.deleteByPrimaryKey(id) > 0;
    }


    @Override
    public Boolean updateGroupMember(IMGroupMember user) {
        return groupMemberMapper.updateByPrimaryKeySelective(user) > 0;
    }



}
