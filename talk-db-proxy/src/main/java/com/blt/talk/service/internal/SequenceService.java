/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface SequenceService {

    public Integer addAndGetInteger(String key, int step);
    public Long addAndGetLong(String key, int step);
    public Long addAndGetLongMap(String key, String hkey, int step);
}
