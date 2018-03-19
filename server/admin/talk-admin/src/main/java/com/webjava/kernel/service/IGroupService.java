package com.webjava.kernel.service;

import java.util.List;

import net.ccfish.talk.admin.domain.ImGroup;

/**
 * Created by wx on 2017/10/27.
 */
public interface IGroupService {

    ImGroup getGroupById(Long id);

    ImGroup getGroupByName(String name);

    List<ImGroup> getAllGroup();

    void addGroup(ImGroup group);

    void deleteGroup(Long id);

    void updateGroup(ImGroup group);

}
