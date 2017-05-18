/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.blt.talk.service.jpa.entity.IMRelationShip;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMRelationShipRepository extends PagingAndSortingRepository<IMRelationShip, Long>, JpaSpecificationExecutor<IMRelationShip>  {

    List<IMRelationShip> findBySmallIdAndBigIdAndStatus(@Param("smallId") long smallId, @Param("bigId") long bigId, @Param("status")byte status);
}
