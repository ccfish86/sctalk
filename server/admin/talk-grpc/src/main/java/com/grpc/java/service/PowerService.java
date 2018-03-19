package com.grpc.java.service;


import com.grpc.java.kernel.entity.power_info;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface PowerService {

    power_info getId(Integer id);

    power_info getName(String name);

    power_info getUrl(String url);

    Boolean add(power_info user);

    List<power_info> getAll();

    Boolean delete(Integer id);

    Boolean update(power_info user);

}
