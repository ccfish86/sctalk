/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

/**
 * 会话(Session)相关处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface SessionService {

    /**
     * Session追加
     * 
     * @param userId 用户
     * @param peerId 目标ID（用户或群组）
     * @param type 类型（用户或群组）
     * @return 会话ID
     * @since  1.0
     */
    long addSession(long userId, long peerId, int type);
    /**
     * 查询会话ID
     * 
     * @param userId 用户
     * @param peerId 目标ID（用户或群组）
     * @param type 类型（用户或群组）
     * @param isAll 是否包括已删除的数据（逻辑删除）
     * @return 会话ID
     * @since  1.0
     */
    long getSessionId(long userId, long peerId, int type, boolean isAll);
    
    /**
     * 删除会话
     * 
     * @param sessionId 会话ID
     * @since  1.0
     */
    void remove(long sessionId);
    
    /**
     * 更新会话
     * 
     * @param sessionId 会话ID
     * @param time 时间
     * @since  1.0
     */
    void update(long sessionId, int time);
}
