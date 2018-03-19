package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.IMGroupMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMGroupMemberMapper {

    IMGroupMember selectGroupMemberById(@Param("id") Integer Id);

    IMGroupMember selectGroupMemberByGroupId(@Param("groupId") Integer Id);

    IMGroupMember selectGroupMemberByUserId(@Param("userId") Integer Id);

    List<IMGroupMember> selectAllGroupMember();

    int deleteByPrimaryKey(@Param("id") Integer id);

    int insertSelective(IMGroupMember record);

    int updateByPrimaryKeySelective(IMGroupMember record);
}