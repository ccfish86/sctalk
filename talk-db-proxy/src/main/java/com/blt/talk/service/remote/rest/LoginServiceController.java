/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.param.RegistReq;
import com.blt.talk.common.param.UserToken;
import com.blt.talk.common.result.LoginCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.common.util.SecurityUtils;
import com.blt.talk.service.internal.DepartmentService;
import com.blt.talk.service.jpa.entity.IMUser;
import com.blt.talk.service.jpa.repository.IMUserRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.redis.RedisKeys;

/**
 * 登录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
public class LoginServiceController {

    @Autowired
    private IMUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 注册
     * @param param 用户名或手机号phone
     * @return 登录成功后返回用户ID
     * @since  1.1
     */
    @PostMapping(path = "/regist")
    public BaseModel<Long> regist(@RequestBody RegistReq param) {
        BaseModel<Long> userRes = new BaseModel<>();
        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.or(JpaRestrictions.eq("name", param.getName(), false),
                JpaRestrictions.eq("phone", param.getName(), false)));
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));
        List<IMUser> users = userRepository.findAll(userSearchCriteria);
        
        if (!users.isEmpty()) {
            logger.debug("用户{}注册失败， 已存在", param.getName());
            return userRes.setResult(LoginCmdResult.REGIST_EXISTED_USER);
        }
        
        Long departId = departmentService.getDeptId("TT测试部门");
        
        int time = CommonUtils.currentTimeSeconds(); 
        
        // String md5 = SecurityUtils.getInstance().EncryptPass(param.getPassword());
        // String pwdEnc = passwordEncoder.encode(md5);
        String pwdEnc = passwordEncoder.encode(param.getPassword());
        
        IMUser user = new IMUser();
        user.setName(param.getName());
        user.setPassword(pwdEnc);
        user.setSex((byte)param.getSex());
        user.setNick(param.getName());
        user.setPhone(param.getPhone());
        user.setDepartId(departId);
        user.setSalt("1111");
        user.setStatus(DBConstant.USER_STATUS_OFFICIAL);
        user.setPushShieldStatus(DBConstant.SHIELD_ONLINE);
        user.setDomain(" ");
        user.setAvatar("");
        user.setSignInfo(" ");
        user.setEmail(param.getEmail() == null? param.getName()+ "@tt": param.getEmail());
        user.setCreated(time);
        user.setUpdated(time);
        
        user = userRepository.save(user);
        userRes.setData(user.getId());
        return userRes;
    }
    /**
     * 登录
     * @param param 用户名或手机号phone
     * @return 登录成功后返回用户基本信息
     * @since  1.0
     */
    @PostMapping(path = "/login")
    public BaseModel<UserEntity> login(@RequestBody LoginReq param) {

        BaseModel<UserEntity> userRes = new BaseModel<>();
        String key = RedisKeys.concat(RedisKeys.USER_LOGIN_FAILD, param.getName());
        ValueOperations<String, String> faildCountOps = redisTemplate.opsForValue();
        String faildCount = faildCountOps.get(key);
        int faild = 0;
        if (!StringUtils.isEmpty(faildCount)) {
            faild = Integer.parseInt(faildCount);
        }
        if (faild > 5) {
            return userRes.setResult(LoginCmdResult.LOGIN_PASSWORD_LOCK);
        }
        
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
            if (faild > 0) {
                redisTemplate.delete(key);
            }
        } else {

            // 密码错误，记录一次登陆失败
            faildCountOps.set(key, String.valueOf(++faild), 30, TimeUnit.MINUTES);
            return userRes.setResult(LoginCmdResult.LOGIN_NOUSER);
        }
        return userRes;
    }

    /**
     * 设置用户Token
     * <br>
     * 用于推送等场合
     * 
     * @param param 用户Token
     * @return 设置结果
     * @since  1.0
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login/deviceToken")
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

    @PostMapping(path = "/login/pushShield")
    public BaseModel<Integer> pushShield(@RequestParam("userId") long userId, @RequestParam("shieldStatus") int shieldStatus) {
        String key = RedisKeys.concat(RedisKeys.USER_INFO, userId); 
        HashOperations<String, String, String> userMapOps = redisTemplate.opsForHash();
        userMapOps.put(key, RedisKeys.USER_SHIELD, String.valueOf(shieldStatus));
        
        return new BaseModel<Integer>();
    }
    
    @GetMapping(path = "/login/queryPushShield")
    public BaseModel<Integer> queryPushShield(@RequestParam("userId") long userId) {
        
        String key = RedisKeys.concat(RedisKeys.USER_INFO, userId); 
        HashOperations<String, String, String> userMapOps = redisTemplate.opsForHash();
        String shieldStatus = userMapOps.get(key, RedisKeys.USER_SHIELD);
        
        BaseModel<Integer> res = new BaseModel<Integer>();
        if (shieldStatus != null) {
            res.setData(Integer.valueOf(shieldStatus));
        }
        return res;
    }
    
}
