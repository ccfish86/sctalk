/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 用户之前关系Sevice
 * <br>
 * 用户关系，依据两个用户ID生成，用来分表存储消息用
 *  
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface RelationShipService {

    /**
     * 查询用户关系ID
     * @param userId 用户ID
     * @param toId 对方ID（关联用户ID）
     * @param add 是否在不存在时追加
     * @return 关系ID
     * @since  1.0
     */
    Long getRelationId(long userId, long toId, boolean add);
}
