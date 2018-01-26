package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.manager_role_info;
import com.grpc.java.kernel.mybatis.mapper.manager_role_infoMapper;
import com.grpc.java.service.Manager_RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/*******************************
 * Created by wx on 2017/10/27.*
 *******************************/
@Service("manager_roleService")
public class Manager_RoleServiceImpl implements Manager_RoleService {
    @Resource
    private manager_role_infoMapper manager_roleMapper;

    @Override
    public manager_role_info getId(Integer id) {

        return manager_roleMapper.selectById(id);
    }

    @Override
    public manager_role_info getManagerId(Integer id) {

        return manager_roleMapper.selectByManagerId(id);
    }

    @Override
    public manager_role_info getRoleId(Integer id) {

        return manager_roleMapper.selectByRoleId(id);
    }

    @Override
    public List<manager_role_info> getAll() {
        return this.manager_roleMapper.selectAll();
    }

    @Override
    public Boolean add(manager_role_info user) {
        return this.manager_roleMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean update(manager_role_info user){
        return this.manager_roleMapper.updateByPrimaryKeySelective(user)>0;
    }

    @Override
    public Boolean delete(Integer id){
        return this.manager_roleMapper.deleteByPrimaryKey(id) > 0;
    }


}
