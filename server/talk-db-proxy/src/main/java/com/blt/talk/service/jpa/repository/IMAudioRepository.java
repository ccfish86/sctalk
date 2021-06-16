/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blt.talk.service.jpa.entity.IMAudio;

/**
 * im_audio表对应JPARepository
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public interface IMAudioRepository extends JpaRepository<IMAudio, Long>, PagingAndSortingRepository<IMAudio, Long>, JpaSpecificationExecutor<IMAudio> {

}
