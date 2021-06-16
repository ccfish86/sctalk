/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.DepartmentEntity;

/**
 * 部门业务远程调用Service
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@FeignClient(name = "talk-db-server", contextId = "dept")
public interface DepartmentService {

    /**
     * 查询部门列表
     * @param lastTime 更新时间
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/department/changedList")
    BaseModel<List<DepartmentEntity>> changedList(@RequestParam("lastTime") int lastTime);
}
