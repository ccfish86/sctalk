/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.router.server.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.router.server.handler.IMBuddyListHandler;
import com.blt.talk.router.server.manager.UserClientInfoManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 通讯录处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMBuddyListHandlerImpl implements IMBuddyListHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /* (non-Javadoc)
     * @see com.blt.talk.router.server.handler.IMBuddyListHandler#userStatusReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void userStatusReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMBuddy.IMUsersStatReq usersStatReq = (IMBuddy.IMUsersStatReq)body;

        logger.debug("查询用户在线状态, user_cnt={}", usersStatReq.getUserIdListCount());
        
        List<IMBaseDefine.UserStat> userStatList = new ArrayList<>();
        for (Long userId: usersStatReq.getUserIdListList()) {
            
            UserClientInfoManager.UserClientInfo userClientInfo = UserClientInfoManager.getUserInfo(userId);
            IMBaseDefine.UserStat.Builder userStatBuiler = IMBaseDefine.UserStat.newBuilder();
            userStatBuiler.setUserId(userId);
            if (userClientInfo != null) {
                userStatBuiler.setStatus(userClientInfo.getStatus());
            } else {
                userStatBuiler.setStatus(IMBaseDefine.UserStatType.USER_STATUS_OFFLINE);
            }
            
            userStatList.add(userStatBuiler.build());
        }
        
        IMBuddy.IMUsersStatRsp.Builder userStatRes = IMBuddy.IMUsersStatRsp.newBuilder();
        userStatRes.addAllUserStatList(userStatList);
        userStatRes.setUserId(usersStatReq.getUserId());
        
        IMHeader headerRes = header.clone();
        headerRes.setCommandId((short)BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<>(headerRes, userStatRes.build()));
    }
}
