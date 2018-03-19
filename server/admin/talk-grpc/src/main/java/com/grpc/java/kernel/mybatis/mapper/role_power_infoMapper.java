package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.role_power_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface role_power_infoMapper {
    role_power_info selectById(@Param("id") Integer id);

    role_power_info selectByPowerId(@Param("id") Integer id);

    role_power_info selectByRoleId(@Param("id") Integer id);

    List<role_power_info> selectAll();

    int insertSelective(role_power_info record);

    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(role_power_info record);
}