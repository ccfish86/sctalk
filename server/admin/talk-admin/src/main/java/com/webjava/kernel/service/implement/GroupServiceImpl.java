package com.webjava.kernel.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webjava.kernel.service.IGroupService;

import net.ccfish.talk.admin.domain.ImGroup;
import net.ccfish.talk.admin.repository.IImGroupRepository;

/**
 * Created by wx on 2017/10/27.
 */
@Service("groupService")
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IImGroupRepository groupMapper;

    public ImGroup getGroupById(Long id) {
        return groupMapper.findOne(id);
    }

    @Override
    public ImGroup getGroupByName(String userName) {
        return groupMapper.findByName(userName);
    }

    @Override
    public List<ImGroup> getAllGroup() {
        // return groupMapper.selectAllGroup();
        return groupMapper.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    @Override
    public void addGroup(ImGroup group) {
        // groupMapper.save(user);
        groupMapper.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        // groupMapper.delete(id);
        ImGroup group = groupMapper.findOne(id);
        if (group!= null) {
            group.setStatus((byte)1);;
            groupMapper.save(group);
        }
    }


    @Override
    public void updateGroup(ImGroup group) {
        // groupMapper.updateByPrimaryKeySelective(user);
        groupMapper.save(group);
    }

}
