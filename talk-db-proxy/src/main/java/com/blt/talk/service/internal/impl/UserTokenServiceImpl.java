/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.blt.talk.service.internal.UserTokenService;
import com.blt.talk.service.redis.RedisKeys;

/**
 * 用户token处理Service
 * 
 * @author 袁贵
 * @version 3.0
 * @since 3.0
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.service.internal.UserTokenService#getToken(java.lang.long)
     */
    @Override
    public String getToken(long userId) {
        HashOperations<String, String, String> opsHash = redisTemplate.opsForHash();
        String key = RedisKeys.concat(RedisKeys.USER_INFO, userId);
        
        return opsHash.get(key, RedisKeys.USER_TOKEN);
    }

    /* (non-Javadoc)
     * @see com.blt.talk.service.internal.UserTokenService#getToken(java.lang.String)
     */
    @Override
    public String getToken(String userId) {
        HashOperations<String, String, String> opsHash = redisTemplate.opsForHash();
        String key = RedisKeys.concat(RedisKeys.USER_INFO, userId);
        
        return opsHash.get(key, RedisKeys.USER_TOKEN);
    }

}
