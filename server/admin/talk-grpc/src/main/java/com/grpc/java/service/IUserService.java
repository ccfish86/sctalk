package com.grpc.java.service;


import com.grpc.java.kernel.entity.IMUser;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface IUserService  {

    IMUser getUserById(Integer id);

    IMUser getUserByName(String name);

    List<IMUser> getAllUser();

    Boolean addUser(IMUser user);

    Boolean deleteUser(Integer id);

    Boolean updateUser(IMUser user);

    Boolean updatePassword(IMUser user);

}
