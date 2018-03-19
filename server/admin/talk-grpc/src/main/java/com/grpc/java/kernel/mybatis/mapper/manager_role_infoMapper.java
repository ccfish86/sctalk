package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.manager_role_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface manager_role_infoMapper {
    manager_role_info selectById(@Param("id") Integer id);

    manager_role_info selectByManagerId(@Param("id") Integer id);

    manager_role_info selectByRoleId(@Param("id") Integer id);

    List<manager_role_info> selectAll();

    int insertSelective(manager_role_info record);

    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(manager_role_info record);
}