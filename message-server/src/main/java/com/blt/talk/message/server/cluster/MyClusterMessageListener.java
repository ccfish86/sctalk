/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.PduAttachData;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMFile;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.FileCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStat;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserTokenInfo;
import com.blt.talk.common.code.proto.IMBuddy.IMPCLoginStatusNotify;
import com.blt.talk.common.code.proto.IMGroup.IMGroupChangeMemberNotify;
import com.blt.talk.common.code.proto.IMMessage.IMMsgData;
import com.blt.talk.common.code.proto.IMMessage.IMMsgDataReadNotify;
import com.blt.talk.common.code.proto.IMServer.IMPushToUserReq;
import com.blt.talk.common.code.proto.IMServer.IMServerKickUser;
import com.blt.talk.common.code.proto.IMServer.IMServerPCLoginStatusNotify;
import com.blt.talk.common.constant.AttachType;
import com.blt.talk.common.constant.PushConstant;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.IosPushReq;
import com.blt.talk.common.param.UserToken;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
import com.blt.talk.message.server.remote.GroupService;
import com.blt.talk.message.server.remote.IphonePushService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.hazelcast.core.Member;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class MyClusterMessageListener implements MessageListener<MyClusterMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GroupService groupService;
    @Autowired
    IphonePushService iphonePushService;

    @Override
    public void onMessage(Message<MyClusterMessage> message) {

        MyClusterMessage clusterMessage = message.getMessageObject();
        Member member = message.getPublishingMember();

        // 根据不同的消息，做不同的处理
        // 处理请求分发
        switch (clusterMessage.getServiceId()) {
            case IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE:
                this.doBuddyList(clusterMessage.getCommandId(), clusterMessage);
                break;
            case IMBaseDefine.ServiceID.SID_MSG_VALUE:
                if (!member.localMember()) {
                    // 不处理当前node的消息
                    this.doMessage(clusterMessage.getCommandId(), clusterMessage);
                }
                break;
            case IMBaseDefine.ServiceID.SID_OTHER_VALUE:
                this.doOther(clusterMessage.getCommandId(), clusterMessage);
                break;
            case IMBaseDefine.ServiceID.SID_SWITCH_SERVICE_VALUE:
                if (!member.localMember()) {
                    this.doSwitch(clusterMessage.getCommandId(), clusterMessage);
                }
                break;
            case IMBaseDefine.ServiceID.SID_FILE_VALUE:
                this.doFile(clusterMessage.getCommandId(), clusterMessage);
                break;
            case IMBaseDefine.ServiceID.SID_GROUP_VALUE:
                if (!member.localMember()) {
                    this.doGroup(clusterMessage.getCommandId(), clusterMessage);
                }
                break;
            default:
                logger.warn("暂不支持的服务ID{}", clusterMessage.getServiceId());
                break;
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doGroup(short commandId, MyClusterMessage clusterMessage) {
        logger.debug("MyClusterMessageListener#doSwitch");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:// todebug
                    groupChangeMemberNotify(header, body);
                    break;
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doFile(short commandId, MyClusterMessage clusterMessage) {
        logger.debug("MyClusterMessageListener#doSwitch");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case FileCmdID.CID_FILE_NOTIFY_VALUE:// todebug
                    fileNotify(header, body);
                    break;
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doSwitch(short commandId, MyClusterMessage clusterMessage) {
        logger.debug("MyClusterMessageListener#doSwitch");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE:// todebug
                    switchP2p(header, body);
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doOther(short commandId, MyClusterMessage clusterMessage) {
        logger.debug("MyClusterMessageListener#doOther");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE:
                    handleKickUser(body);
                    break;
                case OtherCmdID.CID_OTHER_LOGIN_STATUS_NOTIFY_VALUE:
                    handlePCLoginStatusNotify(header, body);
                    break;
                case OtherCmdID.CID_OTHER_HEARTBEAT_VALUE:// 无需实现
                    break;
                case OtherCmdID.CID_OTHER_ROLE_SET_VALUE:// 目前不需要实现
                    break;
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doMessage(short commandId, MyClusterMessage clusterMessage) {
        logger.debug("MyClusterMessageListener#doMessage");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case MessageCmdID.CID_MSG_READ_NOTIFY_VALUE:
                    handleMsgReadNotify(header, body);
                    break;
                case MessageCmdID.CID_MSG_DATA_VALUE:
                    handleMsgData(header, body);
                    break;
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * @param commandId
     * @param clusterMessage
     * @since 1.0
     */
    private void doBuddyList(short commandId, MyClusterMessage clusterMessage) {
        logger.info("doBuddyList");
        IMHeader header = clusterMessage.getHeader();
        try {
            MessageLite body = clusterMessage.getMessage();
            switch (commandId) {
                case BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE:
                    // send friend online message to client
                    handleStatusNotify(header, body);
                    break;
                case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE:
                    // send back to user
                    handleUsersStatus(header, body);
                    break;
                case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE: // todebug
                    removeSessionNotify(header, body);
                    break;
                case BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE: // todebug
                    signInfoChangedNotify(header, body);
                    break;
                default:
                    logger.warn("Unsupport command id {}", commandId);
                    break;
            }
        } catch (IOException e) {
            logger.error("decode failed.", e);
        }
    }

    /**
     * 
     * @param header
     * @param body
     * @since 1.0
     */
    private void switchP2p(IMHeader header, MessageLite body) {

        IMSwitchService.IMP2PCmdMsg switchP2p = (IMSwitchService.IMP2PCmdMsg) body;

        ClientUser fromUser = ClientUserManager.getUserById(switchP2p.getFromUserId());
        ClientUser toUser = ClientUserManager.getUserById(switchP2p.getToUserId());

        // 这样处理是否合理，需要检查？
        if (fromUser != null) {
            fromUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
        }

        if (toUser != null) {
            toUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
        }

    }


    /**
     * @param header
     * @param body
     * @since 1.0
     */
    private void fileNotify(IMHeader header, MessageLite body) {
        IMFile.IMFileNotify fileNotify = (IMFile.IMFileNotify) body;

        ClientUser clientUser = ClientUserManager.getUserById(fileNotify.getToUserId());

        if (clientUser != null) {
            clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
        }


    }

    /**
     * 群消息发送
     * 
     * @param header
     * @param msgdata
     * @since 1.0
     */
    private void handleGroupMessageBroadcast(IMHeader header, IMMsgData msgdata) {

        // 服务器没有群的信息，向DB服务器请求群信息，并带上消息作为附件，返回时在发送该消息给其他群成员
        // 查询群员，然后推送消息
        List<Long> groupIdList = new ArrayList<>();
        groupIdList.add(msgdata.getToSessionId());
        BaseModel<List<GroupEntity>> groupListRes = groupService.groupInfoList(groupIdList);
        if (groupListRes.getCode() == GroupCmdResult.SUCCESS.getCode()) {
            if (groupListRes.getData() != null && !groupListRes.getData().isEmpty()) {
                List<Long> memberList = groupListRes.getData().get(0).getUserList();

                if (memberList.contains(msgdata.getFromUserId())) {
                    // 用户在群中
                    IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msgdata);
                    for (long userId : memberList) {
                        ClientUser clientUser = ClientUserManager.getUserById(userId);
                        if (clientUser == null) {
                            continue;
                        }

                        clientUser.broadcast(message, null);
                    }
                }
            }
        }

    }

    /**
     * 发送当前踢人消息 handleKickUser
     * 
     * @param MessageLite
     * @param ChannelHandlerContext
     * @since 1.0 李春生
     */
    private void handleKickUser(MessageLite body) {


        // 转换body中的数据,判断是否是真正的kickUser消息,如果是,则进行下面的操作,不是抛出异常
        IMServerKickUser kickUser = (IMServerKickUser) body;

        long userId = kickUser.getUserId();
        int clientType = kickUser.getClientType().getNumber();
        int reason = kickUser.getReason();
        logger.debug("HandleKickUser, userId={}, clientType={}, reason={}", userId, clientType,
                reason);

        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            // 踢掉用户,根据ClientType进行判断
            clientUser.kickSameClientType(clientType, reason, null);
        }
    }


    /**
     * Message数据消息
     * 
     * @param MessageLite
     * @param ChannelHandlerContext
     * @since 1.0
     * @author 袁贵
     */
    private void handleMsgData(IMHeader header, MessageLite body) {

        IMMsgData msg = (IMMsgData) body;

        if (CommonUtils.isGroup(msg.getMsgType())) {
            // 团队消息
            handleGroupMessageBroadcast(header, msg);
            return;
        }

        long fromUserId = msg.getFromUserId();
        long toUserId = msg.getToSessionId();

        IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msg);

        ClientUser fromClientUser = ClientUserManager.getUserById(fromUserId);
        ClientUser toClientUser = ClientUserManager.getUserById(toUserId);
        if (fromClientUser != null) {
            // 应该不会再把消息发送至最初的发出设备
            fromClientUser.broadcast(message, null);
        }
        if (toClientUser != null) {
            // 应该不会再把消息发送至最初的发出设备
            toClientUser.broadcast(message, null);
        }
    }

    /**
     * Message读消息
     * 
     * @param MessageLite
     * @param ChannelHandlerContext
     * @throws InvalidProtocolBufferException
     * @since 1.0
     * @author 李春生
     */
    private void handleMsgReadNotify(IMHeader header, MessageLite body) {

        IMMsgDataReadNotify msg = (IMMsgDataReadNotify) body;

        long reqId = msg.getUserId();

        ClientUser pUser = ClientUserManager.getUserById(reqId);
        if (pUser != null) {

            pUser.broadcast(new IMProtoMessage<MessageLite>(header, msg), null);
        }

    }

    /**
     * PC登陆状态消息
     * 
     * @param MessageLite
     * @throws InvalidProtocolBufferException
     * @since 1.0 李春生
     */
    private void handlePCLoginStatusNotify(IMHeader header, MessageLite body) {

        IMServerPCLoginStatusNotify msg = (IMServerPCLoginStatusNotify) body;

        long userId = msg.getUserId();
        int loginStatus = msg.getLoginStatus();
        logger.debug("HandlePCLoginStatusNotify, user_id={}, login_status={}", userId, loginStatus);

        ClientUser pUser = ClientUserManager.getUserById(userId);
        if (pUser != null) {
            pUser.setStatus(loginStatus);
            IMPCLoginStatusNotify.Builder loginStatusBuilder = IMPCLoginStatusNotify.newBuilder();
            loginStatusBuilder.setUserId(userId);

            if (1 == loginStatus) {
                loginStatusBuilder.setLoginStat(UserStatType.USER_STATUS_ONLINE);

            } else {
                loginStatusBuilder.setLoginStat(UserStatType.USER_STATUS_OFFLINE);

            }
            IMPCLoginStatusNotify msg2 = loginStatusBuilder.build();


            header.setServiceId((short) IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE);
            header.setCommandId(
                    (short) IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE);
            pUser.broadcastToMobile(new IMProtoMessage<MessageLite>(header, msg2), null);

        }
    }

    /**
     * @param header
     * @param body
     * @since 1.0
     */
    private void handleStatusNotify(IMHeader header, MessageLite body) {
        // 通知好友及关联群组的用户在线状况
        ClientUserManager.broadCast(new IMProtoMessage<>(header, body),
                SysConstant.CLIENT_TYPE_FLAG_PC);
    }

    /**
     * 处理用户状态查询响应
     * 
     * @param header
     * @param body
     * @since 1.0
     */
    private void handleUsersStatus(IMHeader header, MessageLite body) {
        IMBuddy.IMUsersStatRsp usersStatRsp = (IMBuddy.IMUsersStatRsp) body;
        PduAttachData attachData = new PduAttachData(usersStatRsp.getAttachData());

        if (attachData.getType() == AttachType.HANDLE) {
            Long handleId = attachData.getHandle();
            ChannelHandlerContext msgCtx =
                    ClientUserManager.getConnByHandle(usersStatRsp.getUserId(), handleId);
            if (msgCtx != null) {
                msgCtx.writeAndFlush(new IMProtoMessage<MessageLite>(header, body));
            }
        } else if (attachData.getType() == AttachType.PDU_FOR_PUSH) {

            try {

                List<UserToken> userTokenList = new ArrayList<>();
                List<UserToken> olUserTokenList = new ArrayList<>();

                IMPushToUserReq pushToUserReq = IMPushToUserReq.parseFrom(attachData.getPdu());
                for (UserStat userStat : usersStatRsp.getUserStatListList()) {
                    UserToken userToken = new UserToken();
                    userToken.setUserId(userStat.getUserId());
                    for (UserTokenInfo utokenInfo : pushToUserReq.getUserTokenListList()) {
                        if (utokenInfo.getUserId() == userStat.getUserId()) {
                            userToken.setUserToken(utokenInfo.getToken());
                            break;
                        }
                    }

                    if (userStat.getStatus() == UserStatType.USER_STATUS_ONLINE) {
                        // userToken.setPushType(PushConstant.IM_PUSH_TYPE_SILENT);
                        olUserTokenList.add(userToken);
                    } else {
                        // userToken.setPushType(PushConstant.IM_PUSH_TYPE_NORMAL);
                        userTokenList.add(userToken);
                    }
                }

                JSONObject pushJson = JSON.parseObject(pushToUserReq.getData());

                // 推送
                if (iphonePushService != null) {
                    if (!userTokenList.isEmpty()) {
                        IosPushReq pushReq = new IosPushReq();
                        pushReq.setContent(pushToUserReq.getFlash());
                        pushReq.setMsgType(pushJson.getIntValue("msg_type"));
                        pushReq.setFromId(pushJson.getLong("from_id"));
                        pushReq.setGroupId(pushJson.getLong("group_id"));
                        pushReq.setPushType(PushConstant.IM_PUSH_TYPE_NORMAL);
                        pushReq.setUserTokenList(userTokenList);
                        iphonePushService.sendToUsers(pushReq);
                    }
                    if (!olUserTokenList.isEmpty()) {
                        IosPushReq pushReq = new IosPushReq();
                        pushReq.setContent(pushToUserReq.getFlash());
                        pushReq.setMsgType(pushJson.getIntValue("msg_type"));
                        pushReq.setFromId(pushJson.getLong("from_id"));
                        pushReq.setGroupId(pushJson.getLong("group_id"));
                        pushReq.setPushType(PushConstant.IM_PUSH_TYPE_SILENT);
                        pushReq.setUserTokenList(olUserTokenList);
                        iphonePushService.sendToUsers(pushReq);
                    }
                }

            } catch (InvalidProtocolBufferException e) {
                // e.printStackTrace();
                logger.warn("推送消息解码失败！", e);
            }
        } else {
            // 暂不支持，待追加
        }
    }

    /**
     * 
     * @param header
     * @param body
     * @since 1.0
     */
    private void signInfoChangedNotify(IMHeader header, MessageLite body) {
        // 这样处理是否合理，需要检查？
        ClientUserManager.broadCast(new IMProtoMessage<>(header, body),
                SysConstant.CLIENT_TYPE_FLAG_BOTH);
    }

    /**
     * 
     * @param header
     * @param body
     * @since 1.0
     */
    private void removeSessionNotify(IMHeader header, MessageLite body) {
        IMBuddy.IMRemoveSessionNotify removeSessionNotify = (IMBuddy.IMRemoveSessionNotify) body;
        ClientUser clientUser = ClientUserManager.getUserById(removeSessionNotify.getUserId());

        // 这样处理是否合理，需要检查？
        if (clientUser != null) {
            clientUser.broadcast(new IMProtoMessage<>(header, body), null);
        }
    }

    /**
     * 
     * @param header
     * @param body
     * @since 1.0
     */
    private void groupChangeMemberNotify(IMHeader header, MessageLite body) {
        IMGroupChangeMemberNotify groupChangeMemberNotify = (IMGroupChangeMemberNotify) body;
        ClientUser clientUser = null;

        for (long chgUserId : groupChangeMemberNotify.getChgUserIdListList()) {
            clientUser = ClientUserManager.getUserById(chgUserId);
            if (clientUser != null) {
                clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
            }
        }

        for (long curUserId : groupChangeMemberNotify.getCurUserIdListList()) {
            clientUser = ClientUserManager.getUserById(curUserId);
            if (clientUser != null) {
                clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
            }
        }
    }
}
