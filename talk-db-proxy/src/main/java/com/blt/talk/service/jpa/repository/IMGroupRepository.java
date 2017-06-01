/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.blt.talk.service.jpa.entity.IMGroup;

/**
 * im_group表对应JPARepository
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface IMGroupRepository extends PagingAndSortingRepository<IMGroup, Long>, JpaSpecificationExecutor<IMGroup> {

    @Query("from IMGroup as g inner join fetch g.groupMemberList e where e.userId = ? and g.type=1")
    List<IMGroup> findByUserId(@Param("userId") long userId);
}
