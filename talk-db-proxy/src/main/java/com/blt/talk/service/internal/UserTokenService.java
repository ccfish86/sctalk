/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 用户token处理Service
 * 
 * 
 * @author 袁贵
 * @version 3.0
 * @since  3.0
 */
public interface UserTokenService {

    /**
     * 查询用户推送Token
     * 
     * @param userId 用户ID
     * @return
     * @since  3.0
     */
    String getToken(long userId);
    /**
     * 查询用户推送Token
     * 
     * @param userId 用户ID
     * @return
     * @since  3.0
     */
    String getToken(String userId);

}
