package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.power_info;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface power_infoMapper {

    power_info selectById(@Param("id") Integer id);

    power_info selectByName(@Param("name") String userName);

    power_info selectByUrl(@Param("url") String userName);

    List<power_info> selectAll();

    int insertSelective(power_info record);

    int deleteByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(power_info record);
}