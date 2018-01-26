package com.webjava.kernel.service.implement;

import com.webjava.kernel.entity.IMGroup;
import com.webjava.kernel.mybatis.mapper.IMGroupMapper;
import com.webjava.kernel.service.IGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
@Service("groupService")
public class GroupServiceImpl implements IGroupService{

    @Resource
    private IMGroupMapper groupMapper;

    public IMGroup getGroupById(Integer userId) {
       return groupMapper.selectGroupById(userId);
    }

    @Override
    public IMGroup getGroupByName(String userName) {
       return groupMapper.selectGroupByName(userName);
    }

    @Override
    public List<IMGroup> getAllGroup() {
        return groupMapper.selectAllGroup();
    }

    @Override
    public Boolean addGroup(IMGroup user) {
        return groupMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean deleteGroup(Integer id) {
        return groupMapper.deleteByPrimaryKey(id) > 0;
    }


    @Override
    public Boolean updateGroup(IMGroup user) {
        return groupMapper.updateByPrimaryKeySelective(user) > 0;
    }

}
