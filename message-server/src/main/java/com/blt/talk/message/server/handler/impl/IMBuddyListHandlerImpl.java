/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserInfo;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.handler.IMBuddyListHandler;
import com.blt.talk.message.server.remote.BuddyListService;
import com.blt.talk.message.server.remote.RecentSessionService;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理【BuddyListCmdID】
 * 
 * @author 袁贵
 * @version 1.0
 * @since 3.0
 */
@Component
public class IMBuddyListHandlerImpl implements IMBuddyListHandler {

    @Autowired
    private BuddyListService buddyListService;
    @Autowired
    private RecentSessionService sessionService;

    @Autowired
    private ApplicationContext applicationContext;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#recentContactReq(com.blt.talk.common.
     * code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void recentContactReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMBuddy.IMRecentContactSessionReq contackSessionReq = (IMBuddy.IMRecentContactSessionReq) body;
        try {
            BaseModel<List<SessionEntity>> contackSessionRes = sessionService
                    .getRecentSession(contackSessionReq.getUserId(), contackSessionReq.getLatestUpdateTime());

            IMBuddy.IMRecentContactSessionRsp.Builder resBuilder = IMBuddy.IMRecentContactSessionRsp.newBuilder();
            resBuilder.setUserId(contackSessionReq.getUserId());

            if (contackSessionRes.getData() != null) {
                contackSessionRes.getData().forEach(sessionInfo -> {
                    resBuilder.getContactSessionListList().add(JavaBean2ProtoBuf.getContactSessionInfo(sessionInfo));
                });
            }

            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMRecentContactSessionRsp res = IMBuddy.IMRecentContactSessionRsp.newBuilder()
                    .setUserId(contackSessionReq.getUserId()).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#statusNotify(com.blt.talk.common.code.
     * proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void statusNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#userInfoReq(com.blt.talk.common.code.
     * proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void userInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // 获取用户信息
        IMBuddy.IMUsersInfoReq userInfoReq = (IMBuddy.IMUsersInfoReq)body;
        try {
            if (userInfoReq.getUserIdListList() != null) {

                IMBuddy.IMUsersInfoRsp.Builder userInfoResBuilder = IMBuddy.IMUsersInfoRsp.newBuilder();
                userInfoResBuilder.setUserId(userInfoReq.getUserId());
                
                // 查询用户信息
                BaseModel<List<UserEntity>> userInfoRes = buddyListService.getUserInfoList(userInfoReq.getUserIdListList());
                if (userInfoRes.getCode() == 0 && userInfoRes.getData() != null) {
                    List<UserInfo> users = new ArrayList<>();
                    for(UserEntity userEntity :userInfoRes.getData()) {
                        users.add(JavaBean2ProtoBuf.getUserInfo(userEntity));
                    }
                    
                    userInfoResBuilder.addAllUserInfoList(users);
                }
                
                IMHeader headerRes = header.clone();
                headerRes.setCommandId((short)BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE);
                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, userInfoResBuilder.buildPartial())); 
            }
        } catch (Exception e) {
            
            logger.error("未读消息处理异常", e); 
            
            IMBuddy.IMUsersInfoRsp.Builder userInfoResBuilder = IMBuddy.IMUsersInfoRsp.newBuilder();
            userInfoResBuilder.setUserId(userInfoReq.getUserId());
            
            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short)BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE);
            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, userInfoResBuilder.buildPartial())); 
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#removeSessionReq(com.blt.talk.common.
     * code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void removeSessionReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#allUserReq(com.blt.talk.common.code.
     * proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void allUserReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMBuddy.IMAllUserReq allUserReq = (IMBuddy.IMAllUserReq) body;

        try {
            BaseModel<List<UserEntity>> contackSessionRes =
                    buddyListService.getAllUser(allUserReq.getUserId(), allUserReq.getLatestUpdateTime());

            List<IMBaseDefine.UserInfo> allUsers = new ArrayList<>();
            if (contackSessionRes.getData() != null) {
                contackSessionRes.getData().forEach(userInfo -> {
                    allUsers.add(JavaBean2ProtoBuf.getUserInfo(userInfo));
                });
            }
            
            IMBuddy.IMAllUserRsp.Builder resBuilder = IMBuddy.IMAllUserRsp.newBuilder();
            resBuilder.setUserId(allUserReq.getUserId());
            resBuilder.setLatestUpdateTime(allUserReq.getLatestUpdateTime());

            resBuilder.addAllUserList(allUsers);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMAllUserRsp res =
                    IMBuddy.IMAllUserRsp.newBuilder().setUserId(allUserReq.getUserId()).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#userStatusReq(com.blt.talk.common.code
     * .proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void userStatusReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        logger.info("Send the users status request to router");
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        routerConnector.send(new IMProtoMessage<>(header, body));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#changeAvaterReq(com.blt.talk.common.
     * code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void changeAvaterReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.message.server.handler.IMBuddyListHandler#pcLoginStatusNotify(com.blt.talk.
     * common.code.proto.Header, com.google.protobuf.MessageLite,
     * io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void pcLoginStatusNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.message.server.handler.IMBuddyListHandler#removeSessionNotify(com.blt.talk.
     * common.code.proto.Header, com.google.protobuf.MessageLite,
     * io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void removeSessionNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#departmentReq(com.blt.talk.common.code
     * .proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void departmentReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.blt.talk.message.server.handler.IMBuddyListHandler#avatarChangedNotify(com.blt.talk.
     * common.code.proto.Header, com.google.protobuf.MessageLite,
     * io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void avatarChangedNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#changeSignInfoReq(com.blt.talk.common.
     * code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void changeSignInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMBuddy.IMChangeSignInfoReq changeSignReq = (IMBuddy.IMChangeSignInfoReq) body;


        try {
            BuddyListUserSignInfoReq signInfoReq = new BuddyListUserSignInfoReq();
            signInfoReq.setSignInfo(changeSignReq.getSignInfo());
            signInfoReq.setUserId(changeSignReq.getUserId());
            BaseModel<?> signInfoRes = buddyListService.updateUserSignInfo(signInfoReq);

            IMBuddy.IMChangeSignInfoRsp res = IMBuddy.IMChangeSignInfoRsp.newBuilder()
                    .setUserId(changeSignReq.getUserId()).setSignInfo(changeSignReq.getSignInfo())
                    .setResultCode(signInfoRes.getCode()).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));

            // FIXME 通知群和好友
            // BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY

        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMChangeSignInfoRsp res =
                    IMBuddy.IMChangeSignInfoRsp.newBuilder().setUserId(changeSignReq.getUserId())
                            .setSignInfo(changeSignReq.getSignInfo()).setResultCode(1).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMBuddyListHandler#signInfoChangedNotify(com.blt.talk.
     * common.code.proto.Header, com.google.protobuf.MessageLite,
     * io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void signInfoChangedNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub

    }

}
