/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.result.LoginCmdResult;
import com.blt.talk.service.jpa.entity.IMUser;
import com.blt.talk.service.jpa.repository.IMUserRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.remote.LoginService;

/**
 * 登录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
public class LoginServiceController implements LoginService {

    @Autowired
    private IMUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.service.service.LoginService#login(org.springframework.ui.ModelMap)
     */
    @Override
    @PostMapping(path = "/login")
    public BaseModel<UserEntity> login(@RequestBody LoginReq param) {

        BaseModel<UserEntity> userRes = new BaseModel<>();
        
        // 改为：用户名或手机号皆可登录
        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.or(JpaRestrictions.eq("name", param.getName(), false),
                JpaRestrictions.eq("phone", param.getName(), false)));
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));
        List<IMUser> users = userRepository.findAll(userSearchCriteria);
        
        if (users.isEmpty()) {
            logger.debug("用户{}登录失败", param.getName());
            return userRes.setResult(LoginCmdResult.LOGIN_NOUSER);
        }

        IMUser user = users.get(0);

        // if (user.getStatus() == 0) {
        // 判断用户状态
        // }

        if (passwordEncoder.matches(param.getPassword(), user.getPassword())) {
            // 密码正确
            UserEntity userEntity = new UserEntity();
            userEntity.setId(Long.valueOf(user.getId()));
            userEntity.setMainName(user.getNick());
            userEntity.setAvatar(user.getAvatar());
            userEntity.setCreated(user.getCreated());
            userEntity.setDepartmentId(user.getDepartId());
            userEntity.setPeerId(user.getId());
            userEntity.setPhone(user.getPhone());
            userEntity.setRealName(user.getName());
            userEntity.setStatus(user.getStatus());
            userEntity.setGender(user.getSex());
            userEntity.setUpdated(user.getUpdated());
            userEntity.setEmail(user.getEmail());
            userEntity.setPinyinName(user.getDomain());
            userEntity.setSignInfo(user.getSignInfo());
            userRes.setData(userEntity);

            // 如果登陆成功，则清除错误尝试限制

        } else {

            // 密码错误，记录一次登陆失败
            
            return userRes.setResult(LoginCmdResult.LOGIN_NOUSER);
        }
        return userRes;
    }

}
