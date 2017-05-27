/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.service.remote.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.service.jpa.entity.IMUser;
import com.blt.talk.service.jpa.repository.IMUserRepository;
import com.blt.talk.service.jpa.util.JpaRestrictions;
import com.blt.talk.service.jpa.util.SearchCriteria;
import com.blt.talk.service.remote.BuddyListService;

/**
 * 通信录相关业务处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/buddyList")
public class BuddyListServiceController implements BuddyListService {

    @Autowired
    private IMUserRepository userRepository;

    @Override
    @PostMapping(path = "/updateUserSignInfo")
    public BaseModel<?> updateUserSignInfo(@RequestBody final BuddyListUserSignInfoReq signInfo) {

        int time = CommonUtils.currentTimeSeconds();
        IMUser imuser = userRepository.findOne(signInfo.getUserId());
        imuser.setSignInfo(signInfo.getSignInfo());
        imuser.setUpdated(time);

        userRepository.save(imuser);

        return new BaseModel<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.service.service.BuddyListService#getAllUser(int, int)
     */
    @Override
    @GetMapping(path = "/allUser")
    public BaseModel<List<UserEntity>> getAllUser(@RequestParam("userId") long userId,
            @RequestParam("updateTime") int lastUpdateTime) {
        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));

        List<IMUser> users = userRepository.findAll(userSearchCriteria);

        List<UserEntity> userInfoList = new ArrayList<>();
        for (IMUser user : users) {
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
            userInfoList.add(userEntity);
        }

        BaseModel<List<UserEntity>> userInfoRes = new BaseModel<>();
        userInfoRes.setData(userInfoList);
        return userInfoRes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.service.service.BuddyListService#getUserInfoList(java.util.List)
     */
    @Override
    @GetMapping(path = "/userInfo")
    public BaseModel<List<UserEntity>> getUserInfoList(@RequestParam("userId") List<Long> userIdListList) {

        SearchCriteria<IMUser> userSearchCriteria = new SearchCriteria<>();
        userSearchCriteria.add(JpaRestrictions.in("id", userIdListList, false));
        userSearchCriteria.add(JpaRestrictions.ne("status", DBConstant.USER_STATUS_LEAVE, false));

        List<IMUser> users = userRepository.findAll(userSearchCriteria);

        List<UserEntity> userInfoList = new ArrayList<>();
        for (IMUser user : users) {
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
            userInfoList.add(userEntity);
        }

        BaseModel<List<UserEntity>> userInfoRes = new BaseModel<>();
        userInfoRes.setData(userInfoList);
        return userInfoRes;
    }
}
