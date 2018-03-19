package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.role_power_info;
import com.grpc.java.kernel.mybatis.mapper.role_power_infoMapper;
import com.grpc.java.service.Role_PowerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/*******************************
 * Created by wx on 2017/10/27.*
 *******************************/
@Service("role_powerService")
public class Role_PowerServiceImpl implements Role_PowerService {
    @Resource
    private role_power_infoMapper role_powerMapper;

    @Override
    public role_power_info getId(Integer id) {

        return role_powerMapper.selectById(id);
    }

    @Override
    public role_power_info getPowerId(Integer id) {

        return role_powerMapper.selectByPowerId(id);
    }

    @Override
    public role_power_info getRoleId(Integer id) {

        return role_powerMapper.selectByRoleId(id);
    }

    @Override
    public List<role_power_info> getAll() {

        return this.role_powerMapper.selectAll();
    }

    @Override
    public Boolean add(role_power_info user) {
        return this.role_powerMapper.insertSelective(user) > 0;
    }
    
    @Override
    public Boolean update(role_power_info user){
        return this.role_powerMapper.updateByPrimaryKeySelective(user)>0;
    }
    
    @Override
    public Boolean delete(Integer id){
        return this.role_powerMapper.deleteByPrimaryKey(id) > 0;
    }


}
