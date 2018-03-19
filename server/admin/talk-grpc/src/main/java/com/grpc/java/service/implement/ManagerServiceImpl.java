package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.manager_info;
import com.grpc.java.kernel.mybatis.mapper.manager_infoMapper;
import com.grpc.java.service.ManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/*******************************
 * Created by wx on 2017/10/27.*
 *******************************/
@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
    @Resource
    private manager_infoMapper managerMapper;

    @Override
    public manager_info getId(Integer id) {

        return managerMapper.selectById(id);
    }

    @Override
    public manager_info getName(String userName) {
        return this.managerMapper.selectByName(userName);
    }

    @Override
    public manager_info getToken(String token){
        return  this.managerMapper.selectToken(token);
    }

    @Override
    public List<manager_info> getAll() {

        return this.managerMapper.selectAll();
    }

    @Override
    public Boolean add(manager_info user) {
        return this.managerMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean delete(Integer id) {
        return managerMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Boolean update(manager_info user) {
        return managerMapper.updateByPrimaryKeySelective(user) > 0;
    }


    @Override
    public Boolean updatePassword(manager_info user) {
        return managerMapper.updatePasswordSelective(user) > 0;
    }

}
