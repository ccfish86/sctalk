/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.DepartmentService;
import com.blt.talk.service.jpa.entity.IMDepart;
import com.blt.talk.service.jpa.repository.IMDepartRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    IMDepartRepository departRepository;
    
    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.DepartmentService#getDeptId()
     */
    @Override
    public Long getDeptId(String deptName) {
        
        SearchCriteria<IMDepart> deptCriteria = new SearchCriteria<>();
        deptCriteria.add(JpaRestrictions.eq("departName", deptName, false));
        
        List<IMDepart> departList =  departRepository.findAll(deptCriteria);
        if (departList.isEmpty()) {
            
            int time = CommonUtils.currentTimeSeconds();
            
            // 追加
            IMDepart depart = new IMDepart();
            depart.setDepartName(deptName);
            depart.setPriority(1);
            depart.setParentId(0L);
            depart.setStatus(DBConstant.DELETE_STATUS_OK);
            depart.setCreated(time);
            depart.setUpdated(time);
            
            depart = departRepository.save(depart);
            return depart.getId();
        }
        return departList.get(0).getId();
    }

}
