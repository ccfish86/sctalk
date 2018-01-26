package com.grpc.java.server;

import com.grpc.java.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wx on 2017/11/8.
 */
@Service("beanService")
public class BeanContainer {

    @Resource
    public ManagerService managerService;

    @Resource
    public IDepartService departService;

    @Resource
    public IDiscoveryService discoveryService;

    @Resource
    public IGroupService groupService;

    @Resource
    public IUserService userService;

    @Resource
    public PowerService powerService;

    @Resource
    public Role_PowerService  role_powerService;

    @Resource
    public RoleService roleService;

    @Resource
    public Manager_RoleService manager_roleService;

}
