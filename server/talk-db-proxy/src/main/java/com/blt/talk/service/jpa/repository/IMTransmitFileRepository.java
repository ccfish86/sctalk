/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blt.talk.service.jpa.entity.IMTransmitFile;

/**
 * im_message表对应JPARepository
 * 
 * @author 袁贵
 * @version 1.1
 * @since 1.1
 */
public interface IMTransmitFileRepository extends PagingAndSortingRepository<IMTransmitFile, Long>,
        JpaSpecificationExecutor<IMTransmitFile> {


}
