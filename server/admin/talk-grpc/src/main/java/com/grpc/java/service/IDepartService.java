package com.grpc.java.service;

import com.grpc.java.kernel.entity.IMDepart;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface IDepartService {

    IMDepart getDepartById(Integer id);

    IMDepart getDepartByName(String departName);

    List<IMDepart> getAllDepart();

    Boolean addDepart(IMDepart user);

    Boolean deleteDepart(Integer id);

    Boolean updateDepart(IMDepart user);
}
