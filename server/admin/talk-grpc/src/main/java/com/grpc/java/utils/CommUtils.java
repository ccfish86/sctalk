/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.grpc.java.utils;

import java.time.Instant;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class CommUtils {

    public static int getSystemSeconds() {
        long seconds = Instant.now().getEpochSecond();
        if (seconds > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) seconds;
    }
}
