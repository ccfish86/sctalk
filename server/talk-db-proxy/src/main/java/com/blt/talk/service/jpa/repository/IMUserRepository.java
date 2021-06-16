/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.blt.talk.service.jpa.entity.IMUser;

/**
 * im_message表对应JPARepository
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMUserRepository extends JpaRepository<IMUser, Long>, PagingAndSortingRepository<IMUser, Long>, JpaSpecificationExecutor<IMUser> {

    List<IMUser> findByName(@Param("name") String name);

}
