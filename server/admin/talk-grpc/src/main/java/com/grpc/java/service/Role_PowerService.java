package com.grpc.java.service;


import com.grpc.java.kernel.entity.role_power_info;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface Role_PowerService {

    role_power_info getId(Integer id);

    role_power_info getPowerId(Integer id);

    role_power_info getRoleId(Integer id);

    List<role_power_info> getAll();

    Boolean add(role_power_info user);

    Boolean delete(Integer id);

    Boolean update(role_power_info user);

}
