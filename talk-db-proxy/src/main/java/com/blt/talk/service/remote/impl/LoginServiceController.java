/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.param.UserToken;
import com.blt.talk.common.result.LoginCmdResult;
import com.blt.talk.service.jpa.entity.IMUser;
import com.blt.talk.service.jpa.repository.IMUserRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.redis.RedisKeys;
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
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
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
            userEntity.setId(user.getId());
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

    /* (non-Javadoc)
     * @see com.blt.talk.service.remote.LoginService#setDeviceToken(com.blt.talk.common.param.UserToken)
     */
    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/login/setdeviceToken")
    public BaseModel<?> setDeviceToken(@RequestBody UserToken param) {

        // 存储> Redis: RedisKeys(user_info_${user_id}/token)
        HashOperations<String, String, String> opsHash = redisTemplate.opsForHash();
        String key = RedisKeys.concat(RedisKeys.USER_INFO, param.getUserId());
        // 取用户ID对应的现有Token值
        String oldToken = opsHash.get(key, RedisKeys.USER_TOKEN);
        
        if (StringUtils.isEmpty(param.getUserToken())) {
            opsHash.delete(key, RedisKeys.USER_TOKEN);
        } else {
            if (ClientType.CLIENT_TYPE_IOS == param.getClientType()) {
                opsHash.put(key, RedisKeys.USER_TOKEN, "ios:" + param.getUserToken());
            } else if (ClientType.CLIENT_TYPE_ANDROID == param.getClientType()) {
                opsHash.put(key, RedisKeys.USER_TOKEN, "android:" + param.getUserToken());
            } else {
                opsHash.put(key, RedisKeys.USER_TOKEN, param.getUserToken());
            }

            // 设置TOKEN对应的新的用户ID
            int tokenHash = param.getUserToken().hashCode();
            opsHash.put(RedisKeys.concat(RedisKeys.TOKEN_USER, tokenHash), param.getUserToken(), String.valueOf(param.getUserId()));
        }
        
        // 清除旧Token对应的用户ID
        if (!StringUtils.isEmpty(oldToken)) {
            int index = oldToken.indexOf(":");
            String oldTokenPureVal = oldToken.substring(index + 1);
            
            if (!StringUtils.isEmpty(oldTokenPureVal)) {
                int tokenHash = oldTokenPureVal.hashCode();
                opsHash.delete(RedisKeys.concat(RedisKeys.TOKEN_USER, tokenHash), oldTokenPureVal);
            }
        }
        
        return new BaseModel<>();
    }

}
