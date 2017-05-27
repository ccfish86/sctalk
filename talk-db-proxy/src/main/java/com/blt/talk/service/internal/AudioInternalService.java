/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.internal;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.blt.talk.service.jpa.entity.IMAudio;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface AudioInternalService {

    /**
     * 保存音频数据
     * @param fromId 发送者ID
     * @param toId 接收者ID
     * @param time 时间（秒--from1970）
     * @param content 内容
     * @return 音频ID
     * @since  1.0
     */
    long saveAudioInfo(long fromId, long toId, int time, byte[] content);
    
    /**
     * 读音频
     * @param id
     * @return
     * @since  1.0
     */
    byte[] readAudioInfo(long id);
    
    /**
     * 读音频
     * @param audioIds
     * @return
     * @since  1.0
     */
    List<byte[]> readAudios(List<Long> audioIds);
}
