package com.grpc.java.service;



import com.grpc.java.kernel.entity.manager_info;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface ManagerService {

    manager_info getId(Integer id);

    manager_info getName(String userName);

    manager_info getToken(String token);

    Boolean add(manager_info user);

    Boolean delete(Integer id);

    Boolean update(manager_info user);

    List<manager_info> getAll();

    Boolean updatePassword(manager_info user);

}
