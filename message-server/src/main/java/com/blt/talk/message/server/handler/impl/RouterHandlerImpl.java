/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine.KickReasonType;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.common.constant.TalkServerEnums;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.handler.RouterHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 发送消息到Router的处理
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class RouterHandlerImpl implements RouterHandler {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public void sendUserStatusUpdate(ChannelHandlerContext msgCtx, UserStatType userStatusOnline) {
        
        // 更新在线状态
        long userId = msgCtx.attr(ClientUser.USER_ID).get();
        IMServer.IMUserStatusUpdate.Builder userStatusUpdate = IMServer.IMUserStatusUpdate.newBuilder();
        userStatusUpdate.setClientType(msgCtx.attr(ClientUser.CLIENT_TYPE).get());
        userStatusUpdate.setUserId(userId);
        userStatusUpdate.setUserStatus(userStatusOnline.getNumber());
        IMHeader onlineHeader = new IMHeader();
        onlineHeader.setServiceId((short)ServiceID.SID_OTHER_VALUE);
        onlineHeader.setCommandId((short)OtherCmdID.CID_OTHER_USER_STATUS_UPDATE_VALUE);
        send(onlineHeader, userStatusUpdate.build(), true);
        
        // 更新在线人数
        IMServer.IMUserCntUpdate.Builder userCntUpdate = IMServer.IMUserCntUpdate.newBuilder();
        userCntUpdate.setUserId(userId);
        if (userStatusOnline == UserStatType.USER_STATUS_OFFLINE) {
            userCntUpdate.setUserAction(TalkServerEnums.USER_CNT.DEC.ordinal());
        } else {
            userCntUpdate.setUserAction(TalkServerEnums.USER_CNT.INC.ordinal());
        }
        
        IMHeader cntHeader = new IMHeader();
        cntHeader.setServiceId((short)ServiceID.SID_OTHER_VALUE);
        cntHeader.setCommandId((short)OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE);
        send(cntHeader, userStatusUpdate.build(), false);
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.RouterHandler#kickOutSameClientType(io.netty.channel.ChannelHandlerContext, com.blt.talk.common.code.proto.IMBaseDefine.KickReasonType)
     */
    @Override
    public void kickOutSameClientType(ChannelHandlerContext msgCtx, KickReasonType kickReasonDuplicateUser) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void send(IMHeader header, MessageLite messageLite) {
        send(header, messageLite, false);
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.RouterHandler#send(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, boolean)
     */
    @Override
    public void send(IMHeader header, MessageLite messageLite, boolean retry) {
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        routerConnector.send(new IMProtoMessage<MessageLite>(header, messageLite), retry);
    }
}
