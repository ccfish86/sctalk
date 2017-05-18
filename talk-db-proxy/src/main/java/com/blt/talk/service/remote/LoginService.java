/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface LoginService {
    
    BaseModel<UserEntity> login(LoginReq param);
}
