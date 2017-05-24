/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.DepartmentEntity;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient("talk-db-server")
public interface DepartmentService {

    /**
     * 查询部门列表
     * @param lastTime 更新时间
     * @return
     * @since  1.0
     */
    @RequestMapping(method = RequestMethod.GET, value = "/department/changedList")
    BaseModel<List<DepartmentEntity>> changedList(@RequestParam("lastTime") int lastTime);
}
