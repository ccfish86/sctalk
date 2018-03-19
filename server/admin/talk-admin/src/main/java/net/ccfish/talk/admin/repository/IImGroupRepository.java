/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package net.ccfish.talk.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.ccfish.talk.admin.domain.ImGroup;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IImGroupRepository
        extends JpaRepository<ImGroup, Long>, JpaSpecificationExecutor<ImGroup> {

    ImGroup findByName(String name);
}
