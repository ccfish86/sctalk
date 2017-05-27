/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.DepartmentEntity;

/**
 * 部门相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface DepartmentService {

    /**
     * 部门列表查询
     * @param lastTime 更新时间
     * @return
     * @since  1.0
     */
    BaseModel<List<DepartmentEntity>> changedList(@RequestParam("lastTime") int lastTime);
}
