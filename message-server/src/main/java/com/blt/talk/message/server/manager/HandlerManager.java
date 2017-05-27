package com.blt.talk.message.server.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.handler.IMBuddyListHandler;
import com.blt.talk.message.server.handler.IMGroupHandler;
import com.blt.talk.message.server.handler.IMLoginHandler;
import com.blt.talk.message.server.handler.IMMessageHandler;
import com.blt.talk.message.server.handler.IMOtherHandler;
import com.blt.talk.message.server.handler.IMSwitchHandler;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理请求分发
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    @Autowired
    private IMLoginHandler imLoginHandler;

    @Autowired
    private IMBuddyListHandler imBuddyListHandler;
    @Autowired
    private IMMessageHandler imMessageHandler;
    @Autowired
    private IMGroupHandler imGroupHandler;
    @Autowired
    private IMOtherHandler imOtherHandler;
    @Autowired
    private IMSwitchHandler imSwitchHandler;
    @Autowired
    private ApplicationContext applicationContext;
    
    
    /**
     * 关闭连接时的处理
     * 
     * @param ctx
     * @since  1.0
     */
    public void offline(ChannelHandlerContext ctx) {
        
        Long handleId = ctx.attr(ClientUser.HANDLE_ID).get();
        Long userId = ctx.attr(ClientUser.USER_ID).get();

        if (handleId != null) {
            // 关闭
        }
        if (userId == null) {
            return;
        }
        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            clientUser.delConn(handleId);
            clientUser.delUnvalidateConn(ctx);
            
            // do UserStatusUpdate
            doUserStatusUpdate(ctx, IMBaseDefine.UserStatType.USER_STATUS_OFFLINE);
            
            if (clientUser.isConnEmpty()) {
                ClientUserManager.removeUser(clientUser);
            }
        }
    }
    
    /**
     * 用户状态变更通知
     * @param ctx
     * @param userStatus
     * @since  1.0
     */
    public void doUserStatusUpdate(ChannelHandlerContext ctx, UserStatType userStatus) {

        Long userId = ctx.attr(ClientUser.USER_ID).get();
        ClientType clientType = ctx.attr(ClientUser.CLIENT_TYPE).get();

        if (userId != null) {
        
            // 只有上下线通知才通知LoginServer/RouterServer
            if (userStatus == UserStatType.USER_STATUS_ONLINE 
                    || userStatus == UserStatType.USER_STATUS_OFFLINE) {
//                IMServer.IMUserCntUpdate userCntUpdate = IMServer.IMUserCntUpdate.newBuilder()
//                        .setUserAction(userStatus == IMBaseDefine.UserStatType.USER_STATUS_ONLINE?
//                                TalkServerEnums.USER_CNT.INC.ordinal():TalkServerEnums.USER_CNT.DEC.ordinal())
//                        .setUserId(userId).build();
//                IMHeader userCntUpdateHeader = new IMHeader();
//                userCntUpdateHeader.setServiceId((short)ServiceID.SID_OTHER_VALUE);
//                userCntUpdateHeader.setCommandId((short)OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE);
//                // > LoginServer
                
                IMServer.IMUserStatusUpdate userStatusUpdate = IMServer.IMUserStatusUpdate.newBuilder()
                        .setUserId(userId).setUserStatus(userStatus.getNumber()).setClientType(clientType).build();
                IMHeader userStatusUpdateHeader = new IMHeader();
                userStatusUpdateHeader.setServiceId((short)ServiceID.SID_OTHER_VALUE);
                userStatusUpdateHeader.setCommandId((short)OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE);
                
                // 发送给RouterServer
                RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
                routerConnector.send(new IMProtoMessage<MessageLite>(userStatusUpdateHeader, userStatusUpdate));
            } else {
                // 不做处理
            }
        }
    }
    
    /**
     * 处理登录认证
     * 
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @throws Exception
     * @since 1.0
     */
    public void doLogin(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) throws Exception {
        logger.info("doLogin");
        switch (commandId) {
            case LoginCmdID.CID_LOGIN_REQ_MSGSERVER_VALUE:
                // this was do at login_server
                logger.warn("this was do at login_server: commandId={}", commandId);
                break;
            case LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE:
                imLoginHandler.login(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_LOGINOUT_VALUE:
                imLoginHandler.logOut(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_KICK_USER_VALUE:
                imLoginHandler.kickUser(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_DEVICETOKEN_VALUE:
                imLoginHandler.deviceToken(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_KICKPCCLIENT_VALUE:
                imLoginHandler.kickPcClient(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_PUSH_SHIELD_VALUE:
                imLoginHandler.pushShield(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_QUERY_PUSH_SHIELD_VALUE:
                imLoginHandler.queryPushShield(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * 处理通讯录相关消息类型
     * 
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doBuddyList(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        // 判断是否登录
        if (!hasLogin(ctx)) {
            return ;
        }
        logger.info("doBuddyList");
        switch (commandId) {
            case BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST_VALUE:
                imBuddyListHandler.recentContactReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE:
                imBuddyListHandler.statusNotify(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE:
                imBuddyListHandler.userInfoReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE:
                imBuddyListHandler.removeSessionReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE:
                imBuddyListHandler.allUserReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE:
                imBuddyListHandler.userStatusReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE:
                imBuddyListHandler.changeAvaterReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE:
                imBuddyListHandler.pcLoginStatusNotify(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE:
                imBuddyListHandler.removeSessionNotify(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE:
                imBuddyListHandler.departmentReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE:
                imBuddyListHandler.avatarChangedNotify(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_REQUEST_VALUE:
                imBuddyListHandler.changeSignInfoReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE:
                imBuddyListHandler.signInfoChangedNotify(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * 处理消息相关消息类型
     * 
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doMessage(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        // 判断是否登录
        if (!hasLogin(ctx)) {
            return ;
        }
        
        logger.info("doMessage");
        switch (commandId) {
            case MessageCmdID.CID_MSG_DATA_VALUE:
                // 消息发送
                imMessageHandler.sendMessage(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_DATA_ACK_VALUE:
                imMessageHandler.clientMsgDataAck(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_READ_ACK_VALUE:
                imMessageHandler.readMessage(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_READ_NOTIFY_VALUE:
                break;
            case MessageCmdID.CID_MSG_TIME_REQUEST_VALUE:
                break;
            case MessageCmdID.CID_MSG_UNREAD_CNT_REQUEST_VALUE:
                imMessageHandler.getUnreadCount(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_LIST_REQUEST_VALUE:
                imMessageHandler.getMessageList(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_REQ_VALUE:
                break;
            case MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_RSP_VALUE:
                imMessageHandler.getLatestMessageId(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_GET_BY_MSG_ID_REQ_VALUE:
                imMessageHandler.getByMessageId(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_GET_BY_MSG_ID_RES_VALUE:
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }


    /**
     * 处理群相关消息类型
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doGroup(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        // 判断是否登录
        if (!hasLogin(ctx)) {
            return ;
        }
        logger.info("doGroup");
        switch (commandId) {
            case GroupCmdID.CID_GROUP_NORMAL_LIST_REQUEST_VALUE:
                imGroupHandler.normalListReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_INFO_REQUEST_VALUE:
                imGroupHandler.groupInfoReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_CREATE_REQUEST_VALUE:
//            	imGroupHandler.groupCreateReq(header, body, ctx);
                imGroupHandler.createGroupReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_REQUEST_VALUE:
//            	imGroupHandler.groupChangeMemberReq(header, body, ctx);
                imGroupHandler.changeMemberReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_SHIELD_GROUP_REQUEST_VALUE:
            	imGroupHandler.groupShieldReq(header, body, ctx);
                break;
//            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:
//            	//imGroupHandler.
//                break;
            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:
                break;
//            case GroupCmdID.CID_GROUP_SHIELD_GROUP_REQUEST_VALUE:
//                break;
//            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:
//                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * 处理其他消息类型
     * 
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doOther(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        logger.info("doOther");
        switch (commandId) {
            case OtherCmdID.CID_OTHER_HEARTBEAT_VALUE:
                imOtherHandler.hearBeat(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_STOP_RECV_PACKET_VALUE:
                break;
            case OtherCmdID.CID_OTHER_VALIDATE_REQ_VALUE:
                break;
            case OtherCmdID.CID_OTHER_GET_DEVICE_TOKEN_REQ_VALUE:
                break;
            case OtherCmdID.CID_OTHER_ROLE_SET_VALUE:
                break;
            case OtherCmdID.CID_OTHER_ONLINE_USER_INFO_VALUE:
                break;
            case OtherCmdID.CID_OTHER_USER_STATUS_UPDATE_VALUE:
                break;
            case OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE:
                break;
            case OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE:
                break;
            case OtherCmdID.CID_OTHER_LOGIN_STATUS_NOTIFY_VALUE:
                break;
            case OtherCmdID.CID_OTHER_PUSH_TO_USER_REQ_VALUE:
                break;
            case OtherCmdID.CID_OTHER_GET_SHIELD_REQ_VALUE:
                break;
            case OtherCmdID.CID_OTHER_FILE_TRANSFER_REQ_VALUE:
                break;
            case OtherCmdID.CID_OTHER_FILE_SERVER_IP_REQ_VALUE:
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * 处理P2P消息
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since  1.0
     */
    public void doSwitch(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        logger.info("doSwitch");
        switch (commandId) {
            case SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE:
                imSwitchHandler.switchP2p(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }
    
    /**
     * 处理用户是否登录
     * <br>
     * 如果未登录,可能会丢弃一部分需要登录才能处理的包
     * 
     * @param ctx 信道 
     * @return 是否登录
     * @since  1.0
     */
    private boolean hasLogin(ChannelHandlerContext ctx) {
        
        logger.debug("判断用户ID:{}", ctx.attr(ClientUser.USER_ID).get());
        if (ctx.attr(ClientUser.USER_ID).get() != null) {
            return true;
        }
        return false;
    }
}
