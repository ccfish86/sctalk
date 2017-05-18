/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.service.internal.SequenceService;

/**
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
    public Long addAndGetLong(@RequestParam("key") String key, @RequestParam("step") int step) {
        RedisAtomicLong counter = 
                new RedisAtomicLong(key, redisTemplate.getConnectionFactory()); 
        Long sequence =  counter.addAndGet(step);
        
        return sequence; 
    }
    
    @GetMapping(path = "/addAndGet/integer")
    @Override
    public Integer addAndGetInteger(@RequestParam("key") String key, @RequestParam("step") int step) {
        RedisAtomicInteger counter = 
                new RedisAtomicInteger(key, redisTemplate.getConnectionFactory()); 
        Integer sequence =  counter.addAndGet(step);
        
        return sequence; 
    }

    @Override
    public Long addAndGetLongMap(String key, String hkey, int step) {
        HashOperations<String, String, String> hashOptions = redisTemplate.opsForHash();
        return hashOptions.increment(key, hkey, step);
    }

}
