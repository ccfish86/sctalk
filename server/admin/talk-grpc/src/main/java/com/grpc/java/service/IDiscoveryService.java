package com.grpc.java.service;

import com.grpc.java.kernel.entity.IMDiscovery;

import java.util.List;

/**
 * Created by wx on 2017/10/27.
 */
public interface IDiscoveryService {
    IMDiscovery getDiscoveryById(Integer id);

    IMDiscovery getDiscoveryByName(String name);

    List<IMDiscovery> getAllDiscovery();

    Boolean addDiscovery(IMDiscovery user);

    Boolean deleteDiscovery(Integer id);

    Boolean updateDiscovery(IMDiscovery user);
}
