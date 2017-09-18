/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.DepartmentEntity;
import com.blt.talk.service.jpa.entity.IMDepart;
import com.blt.talk.service.jpa.repository.IMDepartRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;

/**
 * 部门相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@RestController
@RequestMapping("/department")
public class DepartmentServiceController {

    @Autowired
    private IMDepartRepository departRepository;

    /**
     * 部门列表查询
     * @param lastTime 更新时间
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/changedList")
    public BaseModel<List<DepartmentEntity>> changedList(@RequestParam("lastTime") int lastTime) {
        
        SearchCriteria<IMDepart> departSearchCriteria = new SearchCriteria<>();
        departSearchCriteria.add(JpaRestrictions.gte("updated", lastTime, false));
        
        Sort sort = new Sort(Sort.Direction.ASC, "updated");
        
        List<IMDepart> depts = departRepository.findAll(departSearchCriteria, sort);
        
        List<DepartmentEntity> departments = new ArrayList<>();
        for (IMDepart dept: depts) {
            DepartmentEntity entity = new DepartmentEntity(); 
            entity.setId(dept.getId());
            entity.setDepartId(dept.getId());
            entity.setDepartName(dept.getDepartName());
            entity.setPriority(dept.getPriority());
            entity.setStatus(dept.getStatus());
            entity.setUpdated(dept.getUpdated());
            entity.setCreated(dept.getCreated());
            departments.add(entity);
        }
        BaseModel<List<DepartmentEntity>> resDepartments = new BaseModel<>();
        resDepartments.setData(departments);
        return resDepartments;
    }

}
