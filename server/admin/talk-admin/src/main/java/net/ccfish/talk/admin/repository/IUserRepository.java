/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package net.ccfish.talk.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.ccfish.talk.admin.domain.ManagerUser;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IUserRepository extends JpaRepository<ManagerUser, Long> {

    ManagerUser findByUsername(String username);
}
