package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.role_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface role_infoMapper {
    role_info selectById(@Param("id") Integer id);

    role_info selectByName(@Param("name") String userName);

    List<role_info> selectAll();

    int insertSelective(role_info record);

    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(role_info record);
}