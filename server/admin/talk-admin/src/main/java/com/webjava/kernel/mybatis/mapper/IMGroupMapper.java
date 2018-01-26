package com.webjava.kernel.mybatis.mapper;

import com.webjava.kernel.entity.IMGroup;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IMGroupMapper {
    IMGroup selectGroupById(@Param("id") Integer Id);

    IMGroup selectGroupByName(@Param("name") String Name);

    List<IMGroup> selectAllGroup();

    int deleteByPrimaryKey(@Param("id") Integer id);

    int insertSelective(IMGroup record);

    int updateByPrimaryKeySelective(IMGroup record);
}