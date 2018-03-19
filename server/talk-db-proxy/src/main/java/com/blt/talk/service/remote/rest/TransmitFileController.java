/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.service.jpa.repository.IMTransmitFileRepository;

/**
 * 离线文件处理
 * 
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
@RestController
@RequestMapping("/file")
public class TransmitFileController {

    @Autowired
    private IMTransmitFileRepository fileRepository;

    @GetMapping(path = "/offlineFile")
    public BaseModel<?> getOfflineFile(@RequestParam("userId") long userId) {
        
        //TODO 
        return null;
    }
}
