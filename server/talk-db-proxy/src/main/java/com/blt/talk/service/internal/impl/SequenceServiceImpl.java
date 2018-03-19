/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.blt.talk.service.internal.SequenceService;

/**
 * 用来处理ID自增
 * <br>
 * 使用Redis键/值自增功能
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Service
public class SequenceServiceImpl implements SequenceService {
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Long addAndGetLong(String key, int step) {
        RedisAtomicLong counter = 
                new RedisAtomicLong(key, redisTemplate.getConnectionFactory()); 
        Long sequence =  counter.addAndGet(step);
        
        return sequence; 
    }
    
    @GetMapping(path = "/addAndGet/integer")
    @Override
    public Integer addAndGetInteger(String key, int step) {
        RedisAtomicInteger counter = 
                new RedisAtomicInteger(key, redisTemplate.getConnectionFactory()); 
        Integer sequence =  counter.addAndGet(step);
        
        return sequence; 
    }

//    @Override
//    public Long addAndGetLongMap(String key, String hkey, int step) {
//        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
//        return hashOptions.increment(key, hkey, step);
//    }

}
