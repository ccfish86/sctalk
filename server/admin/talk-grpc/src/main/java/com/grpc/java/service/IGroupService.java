package com.grpc.java.service;



import com.grpc.java.kernel.entity.IMGroup;

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
