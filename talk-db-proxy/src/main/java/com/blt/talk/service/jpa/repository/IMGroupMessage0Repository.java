/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blt.talk.service.jpa.entity.IMGroupMessage0;

/**
 * im_group_message表对应JPARepository
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMGroupMessage0Repository extends PagingAndSortingRepository<IMGroupMessage0, Long>, JpaSpecificationExecutor<IMGroupMessage0> {

}
