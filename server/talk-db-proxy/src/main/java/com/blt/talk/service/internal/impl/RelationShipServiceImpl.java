/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.internal.RelationShipService;
import com.blt.talk.service.jpa.entity.IMRelationShip;
import com.blt.talk.service.jpa.repository.IMRelationShipRepository;

/**
 * 用户之前关系Sevice
 * <br>
 * 用户关系，依据两个用户ID生成，用来分表存储消息用
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Service
public class RelationShipServiceImpl implements RelationShipService{

    @Autowired
    private IMRelationShipRepository relationShipRepository;
    
    /* (non-Javadoc)
     * @see com.blt.talk.service.service.RelationShipService#getRelationId(int, int)
     */
    @Override
    @Transactional
    public Long getRelationId(long userId, long toId, boolean add) {
        
        Long relateId = DBConstant.INVALIAD_VALUE;
        
        long smallId = Math.min(userId, toId);
        long bigId = Math.max(userId, toId);
        
        List<IMRelationShip> relationShipList = relationShipRepository.findBySmallIdAndBigIdAndStatus(smallId, bigId, DBConstant.DELETE_STATUS_OK);
        
        if (!relationShipList.isEmpty()) {
            relateId = relationShipList.get(0).getId();
        } else {
            
            if (add) {
                relationShipList = relationShipRepository.findBySmallIdAndBigIdAndStatus(smallId, bigId, DBConstant.DELETE_STATUS_DELETE);
                IMRelationShip relationShip;
                if (!relationShipList.isEmpty()) {
                    relationShip = relationShipList.get(0);
                    relationShip.setStatus(DBConstant.DELETE_STATUS_OK);
                    relateId = relationShipRepository.save(relationShip).getId();
                } else {
                      int time = CommonUtils.currentTimeSeconds();
                        
                      relationShip = new IMRelationShip();
                      relationShip.setSmallId(smallId);
                      relationShip.setBigId(bigId);
                      relationShip.setStatus(DBConstant.DELETE_STATUS_OK);
                      relationShip.setUpdated(time);
                      relationShip.setCreated(time);
                      relateId = relationShipRepository.save(relationShip).getId();
                }
            }
        }
        
        return relateId;
    }

}
