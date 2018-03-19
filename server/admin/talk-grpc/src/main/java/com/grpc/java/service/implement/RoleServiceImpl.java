package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.role_info;
import com.grpc.java.kernel.mybatis.mapper.role_infoMapper;
import com.grpc.java.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/*******************************
 * Created by wx on 2017/10/27.*
 *******************************/
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Resource
    private role_infoMapper roleMapper;

    @Override
    public role_info getId(Integer id) {

        return roleMapper.selectById(id);
    }

    @Override
    public role_info getName(String name){
        return roleMapper.selectByName(name);
    }


    @Override
    public List<role_info> getAll() {

        return this.roleMapper.selectAll();
    }

    @Override
    public Boolean add(role_info user) {
        return this.roleMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean update(role_info user){
        return this.roleMapper.updateByPrimaryKeySelective(user)>0;
    }

    @Override
    public Boolean delete(Integer id){
        return this.roleMapper.deleteByPrimaryKey(id) > 0;
    }


}
