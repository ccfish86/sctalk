/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.SessionType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserInfo;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMBuddy.IMUsersStatReq;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.DepartmentEntity;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.BuddyListUserAvatarReq;
import com.blt.talk.common.param.BuddyListUserSignInfoReq;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.handler.IMBuddyListHandler;
import com.blt.talk.message.server.manager.ClientUserManager;
import com.blt.talk.message.server.remote.BuddyListService;
import com.blt.talk.message.server.remote.DepartmentService;
import com.blt.talk.message.server.remote.RecentSessionService;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理【BuddyListCmdID】
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class IMBuddyListHandlerImpl extends AbstractUserHandlerImpl implements IMBuddyListHandler {

    @Autowired
    private BuddyListService buddyListService;
    @Autowired
    private RecentSessionService sessionService;
    @Autowired
    private MessageServerCluster messageServerCluster;
    @Autowired
    private DepartmentService departmentService;

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

        long userId = super.getUserId(ctx);
        IMBuddy.IMRecentContactSessionReq contackSessionReq =
                (IMBuddy.IMRecentContactSessionReq) body;
        try {
            BaseModel<List<SessionEntity>> contackSessionRes = sessionService
                    .getRecentSession(userId, contackSessionReq.getLatestUpdateTime());

            IMBuddy.IMRecentContactSessionRsp.Builder resBuilder =
                    IMBuddy.IMRecentContactSessionRsp.newBuilder();
            resBuilder.setUserId(userId);

            if (contackSessionRes.getData() != null) {
                contackSessionRes.getData().forEach(sessionInfo -> {
                    resBuilder.getContactSessionListList()
                            .add(JavaBean2ProtoBuf.getContactSessionInfo(sessionInfo));
                });
            }

            IMHeader resHeader = header.clone();
            resHeader.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMRecentContactSessionRsp res =
                    IMBuddy.IMRecentContactSessionRsp.newBuilder().setUserId(userId).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
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
        IMBuddy.IMUsersInfoReq userInfoReq = (IMBuddy.IMUsersInfoReq) body;
        long userId = super.getUserId(ctx);
        try {
            if (userInfoReq.getUserIdListList() != null) {

                IMBuddy.IMUsersInfoRsp.Builder userInfoResBuilder =
                        IMBuddy.IMUsersInfoRsp.newBuilder();
                userInfoResBuilder.setUserId(userId);

                // 查询用户信息
                BaseModel<List<UserEntity>> userInfoRes =
                        buddyListService.getUserInfoList(userInfoReq.getUserIdListList());
                if (userInfoRes.getCode() == 0 && userInfoRes.getData() != null) {
                    List<UserInfo> users = new ArrayList<>();
                    for (UserEntity userEntity : userInfoRes.getData()) {
                        users.add(JavaBean2ProtoBuf.getUserInfo(userEntity));
                    }

                    userInfoResBuilder.addAllUserInfoList(users);
                }

                IMHeader headerRes = header.clone();
                headerRes.setCommandId(
                        (short) BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE);
                ctx.writeAndFlush(
                        new IMProtoMessage<>(headerRes, userInfoResBuilder.buildPartial()));
            }
        } catch (Exception e) {

            logger.error("未读消息处理异常", e);

            IMBuddy.IMUsersInfoRsp.Builder userInfoResBuilder = IMBuddy.IMUsersInfoRsp.newBuilder();
            userInfoResBuilder.setUserId(userId);

            IMHeader headerRes = header.clone();
            headerRes.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE);
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

        long userId = super.getUserId(ctx);
        IMBuddy.IMRemoveSessionReq removeSessionReq = (IMBuddy.IMRemoveSessionReq) body;
        IMBuddy.IMRemoveSessionRsp removeSessionRsp = null;
        IMHeader headerRes = null;
        try {

            BaseModel<?> removeSessionRes = buddyListService.removeSession(userId);
            if (removeSessionRes.getCode() == 0 && removeSessionRes.getData() != null) {
                removeSessionRsp = IMBuddy.IMRemoveSessionRsp.newBuilder().setUserId(userId)
                        .setSessionId(removeSessionReq.getSessionId())
                        .setSessionType(removeSessionReq.getSessionType()).setResultCode(0).build();
            } else {
                removeSessionRsp = IMBuddy.IMRemoveSessionRsp.newBuilder().setUserId(userId)
                        .setResultCode(1).build();
            }

            headerRes = header.clone();
            headerRes.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_RES_VALUE);
            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, removeSessionRsp));

            // 如果session类型为single则发送CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY通知
            if (removeSessionReq.getSessionType() == SessionType.SESSION_TYPE_SINGLE) {

                IMBuddy.IMRemoveSessionNotify removeSessionNotify =
                        IMBuddy.IMRemoveSessionNotify.newBuilder().setUserId(userId)
                                .setSessionId(removeSessionReq.getSessionId())
                                .setSessionType(removeSessionReq.getSessionType()).build();
                IMHeader removeSessionNotifyHeader = new IMHeader();
                removeSessionNotifyHeader.setServiceId((short) ServiceID.SID_BUDDY_LIST_VALUE);
                removeSessionNotifyHeader.setCommandId(
                        (short) BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE);

                IMProtoMessage<MessageLite> removeSessionNotifyMsg =
                        new IMProtoMessage<>(removeSessionNotifyHeader, removeSessionNotify);

                // 是否需要广播给用户自身?跟下面的广播是否重复？
                ClientUserManager.broadCast(removeSessionNotifyMsg,
                        SysConstant.CLIENT_TYPE_FLAG_BOTH);
                // routerHandler.send(removeSessionNotifyHeader, removeSessionNotify);
                messageServerCluster.send(removeSessionNotifyHeader, removeSessionNotify);
            }

        } catch (Exception e) {

            logger.error("移除会话异常", e);

            removeSessionRsp = IMBuddy.IMRemoveSessionRsp.newBuilder().setUserId(userId)
                    .setSessionId(removeSessionReq.getSessionId())
                    .setSessionType(removeSessionReq.getSessionType()).setResultCode(1).build();

            headerRes = header.clone();
            headerRes.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_RES_VALUE);
            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, removeSessionRsp));
        }

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
        long userId = super.getUserId(ctx);

        try {
            BaseModel<List<UserEntity>> contackSessionRes =
                    buddyListService.getAllUser(userId, allUserReq.getLatestUpdateTime());

            List<IMBaseDefine.UserInfo> allUsers = new ArrayList<>();

            // 最后更新时间
            int lastestTime = allUserReq.getLatestUpdateTime();
            if (contackSessionRes.getData() != null) {
                for (UserEntity userInfo : contackSessionRes.getData()) {
                    lastestTime = Integer.max(lastestTime, userInfo.getUpdated());
                    allUsers.add(JavaBean2ProtoBuf.getUserInfo(userInfo));
                }
            }

            IMBuddy.IMAllUserRsp.Builder resBuilder = IMBuddy.IMAllUserRsp.newBuilder();
            resBuilder.setUserId(userId);
            resBuilder.setLatestUpdateTime(lastestTime);

            resBuilder.addAllUserList(allUsers);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMAllUserRsp res =
                    IMBuddy.IMAllUserRsp.newBuilder().setUserId(userId).buildPartial();
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

        // CID_BUDDY_LIST_USERS_STATUS_REQUEST
        logger.debug("Send the users status request to router");
        long userId = super.getUserId(ctx);

        IMUsersStatReq usersStatReq = (IMUsersStatReq) body;

        ListenableFuture<List<IMBaseDefine.UserStat>> userStatFuture =
                messageServerCluster.userStatusReq(userId, usersStatReq.getUserIdListList());
        userStatFuture.addCallback((List<IMBaseDefine.UserStat> userStatList) -> {
            // 查询用户状态后处理
            IMBuddy.IMUsersStatRsp.Builder userStatRes = IMBuddy.IMUsersStatRsp.newBuilder();
            userStatRes.addAllUserStatList(userStatList);
            userStatRes.setUserId(userId);
            userStatRes.setAttachData(usersStatReq.getAttachData());

            IMHeader headerRes = header.clone();
            headerRes.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE);
            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, userStatRes.build()));
        }, (Throwable e) -> {
            // 异常处理
            logger.warn("处理推送异常", e);
        });
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
        //
        IMBuddy.IMChangeAvatarReq changeAvatarReq = (IMBuddy.IMChangeAvatarReq) body;
        IMBuddy.IMChangeAvatarRsp changeAvatarRsp = null;
        IMHeader headerRes = null;
        long userId = super.getUserId(ctx);

        try {
            if (StringUtils.isNotEmpty(changeAvatarReq.getAvatarUrl())) {
                BuddyListUserAvatarReq userAvatarReq = new BuddyListUserAvatarReq();
                userAvatarReq.setUserId(userId);
                userAvatarReq.setAvatarUrl(changeAvatarReq.getAvatarUrl());
                
                // 头像更新
                BaseModel<?> changeAvatarRes = buddyListService.updateUserAvatar(userAvatarReq);
                if (changeAvatarRes.getCode() == 0 && changeAvatarRes.getData() != null) {
                    changeAvatarRsp = IMBuddy.IMChangeAvatarRsp.newBuilder().setResultCode(0).build();
                } else {
                    changeAvatarRsp = IMBuddy.IMChangeAvatarRsp.newBuilder().setResultCode(1).build();
                }
    
                headerRes = header.clone();
                headerRes.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_RESPONSE_VALUE);
                ctx.writeAndFlush(new IMProtoMessage<>(headerRes, changeAvatarRsp));
    
                // 头像更新广播
                IMHeader notifyHeader = new IMHeader();
                // notifyHeader.setServiceId((short)ServiceID.SID_BUDDY_LIST_VALUE);
                notifyHeader.setCommandId((short) BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE);
                IMBuddy.IMAvatarChangedNotify.Builder notifyBody = IMBuddy.IMAvatarChangedNotify.newBuilder();
                notifyBody.setChangedUserId(userId);
                notifyBody.setAvatarUrl(changeAvatarReq.getAvatarUrl());
                messageServerCluster.send(notifyHeader, notifyBody.build());
                // routerHandler.send(notifyHeader, notifyBody.build());
            }
        } catch (Exception e) {
            logger.error("更新头像异常", e);
            changeAvatarRsp = IMBuddy.IMChangeAvatarRsp.newBuilder()
                    .setUserId(changeAvatarReq.getUserId()).setResultCode(1).build();

            headerRes = header.clone();
            headerRes.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_RESPONSE_VALUE);
            ctx.writeAndFlush(new IMProtoMessage<>(headerRes, changeAvatarRsp));
        }
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
        IMBuddy.IMDepartmentReq departmentReq = (IMBuddy.IMDepartmentReq) body;
        long userId = super.getUserId(ctx);

        try {
            BaseModel<List<DepartmentEntity>> departments =
                    departmentService.changedList(departmentReq.getLatestUpdateTime());
            if (departments.getCode() == 0) {
                if (departments.getData() != null) {
                    IMBuddy.IMDepartmentRsp.Builder departmentResBuilder =
                            IMBuddy.IMDepartmentRsp.newBuilder();
                    List<IMBaseDefine.DepartInfo> depts = new ArrayList<>();

                    int latestUpdateTime = departmentReq.getLatestUpdateTime();
                    for (DepartmentEntity department : departments.getData()) {
                        latestUpdateTime = Integer.max(latestUpdateTime, department.getUpdated());
                        depts.add(JavaBean2ProtoBuf.getDepartmentInfo(department));
                    }

                    departmentResBuilder.setUserId(userId);
                    departmentResBuilder.setLatestUpdateTime(latestUpdateTime);
                    departmentResBuilder.addAllDeptList(depts);

                    IMHeader resHeader = header.clone();
                    resHeader.setCommandId(
                            (short) BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_RESPONSE_VALUE);
                    ctx.writeAndFlush(
                            new IMProtoMessage<>(resHeader, departmentResBuilder.build()));
                }
            }
        } catch (Exception e) {
            logger.error("查询部门列表时发生异常", e);
        }

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
        long userId = super.getUserId(ctx);

        try {
            BuddyListUserSignInfoReq signInfoReq = new BuddyListUserSignInfoReq();
            signInfoReq.setSignInfo(changeSignReq.getSignInfo());
            signInfoReq.setUserId(userId);
            BaseModel<?> signInfoRes = buddyListService.updateUserSignInfo(signInfoReq);

            IMBuddy.IMChangeSignInfoRsp res = IMBuddy.IMChangeSignInfoRsp.newBuilder()
                    .setUserId(userId).setSignInfo(changeSignReq.getSignInfo())
                    .setResultCode(signInfoRes.getCode()).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));

            // 通知群和好友
            // BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY
            // 广播
            IMHeader notifyHeader = new IMHeader();
            notifyHeader.setServiceId((short) ServiceID.SID_BUDDY_LIST_VALUE);
            notifyHeader.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE);
            IMBuddy.IMSignInfoChangedNotify.Builder notifyBody =
                    IMBuddy.IMSignInfoChangedNotify.newBuilder();
            notifyBody.setChangedUserId(userId);
            notifyBody.setSignInfo(changeSignReq.getSignInfo());
            // routerHandler.send(notifyHeader, notifyBody.build());
            messageServerCluster.send(notifyHeader, notifyBody.build());
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMBuddy.IMChangeSignInfoRsp res = IMBuddy.IMChangeSignInfoRsp.newBuilder()
                    .setUserId(userId).setSignInfo(changeSignReq.getSignInfo()).setResultCode(1)
                    .buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId(
                    (short) BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

}
