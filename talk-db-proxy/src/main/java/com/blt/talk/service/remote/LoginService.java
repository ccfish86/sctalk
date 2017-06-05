/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote;

import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.param.UserToken;

/**
 * 登录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public interface LoginService {
    
    /**
     * 登录
     * @param param 用户名或手机号phone
     * @return 登录成功后返回用户基本信息
     * @since  1.0
     */
    BaseModel<UserEntity> login(LoginReq param);
    
    /**
     * 设置用户Token
     * <br>
     * 用于推送等场合
     * 
     * @param param 用户Token
     * @return 设置结果
     * @since  1.0
     */
    BaseModel<?> setDeviceToken(UserToken param);
}
