package com.webjava.kernel.service;

import com.webjava.kernel.entity.IMGroup;
import com.webjava.kernel.entity.IMUser;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface IGroupService {

    IMGroup getGroupById(Integer id);

    IMGroup getGroupByName(String name);

    List<IMGroup> getAllGroup();

    Boolean addGroup(IMGroup user);

    Boolean deleteGroup(Integer id);

    Boolean updateGroup(IMGroup user);

}
