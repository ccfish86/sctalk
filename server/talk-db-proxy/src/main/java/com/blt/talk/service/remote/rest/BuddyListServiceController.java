/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserAvatarReq;
import com.blt.talk.common.param.BuddyListUserInfoReq;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.jpa.entity.IMUser;
import com.blt.talk.service.jpa.repository.IMUserRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;

/**
 * 通信录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/buddyList")
public class BuddyListServiceController {

    @Autowired
    private IMUserRepository userRepository;

    /**
     * 更新用户签名
     * @param signInfo 签名
     * @return 更新结果
     * @since  1.0
     */
    @PostMapping(path = "/updateUserSignInfo")
    @Transactional
    public BaseModel<?> updateUserSignInfo(@RequestBody final BuddyListUserSignInfoReq signInfo) {

        int time = CommonUtils.currentTimeSeconds();
        IMUser imuser = userRepository.findOne(signInfo.getUserId());
        imuser.setSignInfo(signInfo.getSignInfo());
        imuser.setUpdated(time);

        userRepository.save(imuser);

        return new BaseModel<String>();
    }
    /**
     * 更新用户头像
     * @param userAvatarReq 头像信息
     * @return 更新结果
     * @since  1.0
     */
    @PostMapping(path = "/changeAvatar")
    @Transactional
    public BaseModel<?> updateUserAvatar(@RequestBody BuddyListUserAvatarReq userAvatarReq) {

        int time = CommonUtils.currentTimeSeconds();
        IMUser imuser = userRepository.findOne(userAvatarReq.getUserId());
        imuser.setAvatar(userAvatarReq.getAvatarUrl());
        imuser.setUpdated(time);

        userRepository.save(imuser);

        return new BaseModel<String>();
    }
    /**
     * 更新用户信息
     * @param userInfoReq 头像信息
     * @return 更新结果
     * @since  1.1
     */
    @PostMapping(path = "/changeUserInfo")
    @Transactional
    public BaseModel<?> updateUserInfo(@RequestBody BuddyListUserInfoReq userInfoReq) {

        int time = CommonUtils.currentTimeSeconds();
        IMUser imuser = userRepository.findOne(userInfoReq.getUserId());
        imuser.setPhone(userInfoReq.getTelephone());
        imuser.setEmail(userInfoReq.getEmail());
        imuser.setSignInfo(userInfoReq.getSignInfo());
        if (userInfoReq.getAvatarUrl() != null) {
            imuser.setAvatar(userInfoReq.getAvatarUrl());
        }
        imuser.setUpdated(time);

        userRepository.save(imuser);

        return new BaseModel<String>();
    }

    /**
     * 查询所有用户
     * @param userId
     * @param lastUpdateTime
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/allUser")
    public BaseModel<List<UserEntity>> getAllUser(@RequestParam("userId") long userId,
            @RequestParam("updateTime") int lastUpdateTime) {
        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));
        userSearchCriteria.add(JpaRestrictions.gt("updated", lastUpdateTime, false));

        List<IMUser> users = userRepository.findAll(userSearchCriteria);

        List<UserEntity> userInfoList = new ArrayList<>();
        for (IMUser user : users) {
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
            userInfoList.add(userEntity);
        }

        BaseModel<List<UserEntity>> userInfoRes = new BaseModel<>();
        userInfoRes.setData(userInfoList);
        return userInfoRes;
    }

    /**
     * 查询用户信息
     * @param userIdListList
     * @return
     * @since  1.0
     */
    @GetMapping(path = "/userInfo")
    public BaseModel<List<UserEntity>> getUserInfoList(@RequestParam("userId") List<Long> userIdListList) {

        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.in("id", userIdListList, false));
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));

        List<IMUser> users = userRepository.findAll(userSearchCriteria);

        List<UserEntity> userInfoList = new ArrayList<>();
        for (IMUser user : users) {
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
            userInfoList.add(userEntity);
        }

        BaseModel<List<UserEntity>> userInfoRes = new BaseModel<>();
        userInfoRes.setData(userInfoList);
        return userInfoRes;
    }
}
