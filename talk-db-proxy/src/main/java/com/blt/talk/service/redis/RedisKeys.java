/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.redis;

import org.springframework.data.redis.core.ScanOptions;

/**
 * Redis存储用Key及相关编辑功能
 * <br>
 * 具体参考[doc/Redis设计书.xlsx]
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public abstract class RedisKeys {

    /** 用户信息(Map) */
    public static String USER_INFO = "usr";
    /** 用户临时信息-密码错误 */
    public static String USER_LOGIN_FAILD = "ulf";
    
    /** 用户信息-用户未读 */
    public static String USER_UNREAD = "usr_unr";
    /** 用户信息-群未读 */
    public static String GROUP_UNREAD = "usr_gunr";
    
    /** 用户信息：用户Token */
    public static String USER_TOKEN = "tkn";

    /** Token信息[tkn_${token.hashCode}] */
    public static String TOKEN_USER = "tkn";
    
    /** 群组消息(Map)[grp_${gruop_id}]-用户信息 */
    public static String GROUP_INFO = "grp";
    /** 群组消息：设置（存储用户及群的设置） */
    public static String SETTING_INFO = "set";
    
    /** 群组消息:件数（hkey, 用于在Map中）[count] -计数用*/
    public static String COUNT = "cnt";
    
    /** 群组消息:MESSAGEID（hkey, 用于在Map中）[group_message_id_]*/
    public static String GROUP_MESSAGE_ID = "msg_id";

    /** RELATION信息（每10000个用一个MAP）[rel_${relation_id % 10000}]*/
    public static String RELATION_INFO = "rel";
    
    
    /**
     * 合并Key值
     * 
     * @param key Redis的Key值
     * @param subKeys 子Key
     * @return 合并后的值
     * @since 3.0
     */
    public static String concat(String key, Object... subKeys) {

        StringBuffer fullKey = new StringBuffer();

        fullKey.append(key);

        for (Object subKey : subKeys) {
            fullKey.append(".").append(subKey);
        }

        return fullKey.toString();
    }
    
    /**
     * 前缀
     * @param prefix
     * @return
     * @since  1.0
     */
    public static ScanOptions getStartOptions(String prefix) {
        return ScanOptions.scanOptions().match("^" + prefix + ".*").build();
    }

    /**
     * 后缀
     * @param suffix
     * @return
     * @since  1.0
     */
    public static ScanOptions getEndOptions(String suffix) {
        return ScanOptions.scanOptions().match(".*" + suffix + "$").build();
    }
}
