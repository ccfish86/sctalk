package com.grpc.java.kernel.mybatis.mapper;

import com.grpc.java.kernel.entity.IMDepart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMDepartMapper {

    IMDepart selectDepartById(@Param("id") Integer Id);

    IMDepart selectDepartByName(@Param("departName") String Name);

    List<IMDepart> selectAllDepart();

    int deleteByPrimaryKey(@Param("id") Integer id);

    int insertSelective(IMDepart record);

    int updateByPrimaryKeySelective(IMDepart record);

    
}