/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 用来处理ID自增
 * <br>
 * 使用Redis键/值自增功能
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface SequenceService {

    /**
     * 自增Integer值
     * 
     * @param key Redis键
     * @param step 自增量
     * @return 自增后的值
     * @since  1.0
     */
    public Integer addAndGetInteger(String key, int step);
    
    /**
     * 自增Long值
     * 
     * @param key Redis键
     * @param step 自增量
     * @return 自增后的值
     * @since  1.0
     */
    public Long addAndGetLong(String key, int step);
//    /**
//     * 自增Long值
//     * <br>
//     * 存储于Map中，用于处理同一组相似的ID自增
//     * 
//     * @param key Redis键
//     * @param hkey Map的Key
//     * @param step 自增量
//     * @return 自增后的值
//     * @since  1.0
//     */
//    public Long addAndGetLongMap(String key, String hkey, int step);
}
