package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.IMUser;
import com.grpc.java.kernel.mybatis.mapper.IMUserMapper;
import com.grpc.java.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
    @Resource
    private IMUserMapper userMapper;

    @Override
    public IMUser getUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public IMUser getUserByName(String userName) {
        return userMapper.selectUserByName(userName);
    }

    @Override
    public List<IMUser> getAllUser() {
       return userMapper.selectAllUser();
    }

    @Override
    public Boolean addUser(IMUser user) {
        return userMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id) > 0;
    }


    @Override
    public Boolean updateUser(IMUser user) {
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public Boolean updatePassword(IMUser user) {
        return userMapper.updatePasswordSelective(user) > 0;
    }

}
