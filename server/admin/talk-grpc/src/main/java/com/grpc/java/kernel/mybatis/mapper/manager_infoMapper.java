package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.manager_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface manager_infoMapper {
    manager_info selectById(@Param("id") Integer id);

    manager_info selectByName(@Param("username") String userName);

    manager_info selectToken(@Param("token") String token);

    List<manager_info> selectAll();

    int insertSelective(manager_info record);

    int updatePasswordSelective(manager_info record);

    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(manager_info record);
}