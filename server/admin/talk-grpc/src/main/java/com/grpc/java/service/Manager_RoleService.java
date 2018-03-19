package com.grpc.java.service;


import com.grpc.java.kernel.entity.manager_role_info;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface Manager_RoleService {

    manager_role_info getId(Integer id);

    manager_role_info getManagerId(Integer id);

    manager_role_info getRoleId(Integer id);

    List<manager_role_info> getAll();

    Boolean add(manager_role_info user);

    Boolean delete(Integer id);

    Boolean update(manager_role_info user);


}
