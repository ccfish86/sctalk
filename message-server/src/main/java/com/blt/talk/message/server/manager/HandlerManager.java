package com.blt.talk.message.server.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.FileCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.handler.IMBuddyListHandler;
import com.blt.talk.message.server.handler.IMFileHandle;
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
    private IMFileHandle imFileHandle;
    @Autowired
    private MessageServerCluster messageServerCluster;
    
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
            case LoginCmdID.CID_LOGIN_REQ_LOGINOUT_VALUE: //todebug
                imLoginHandler.logOut(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_KICK_USER_VALUE: //todebug
                imLoginHandler.kickUser(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_DEVICETOKEN_VALUE: //todebug
                imLoginHandler.deviceToken(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_KICKPCCLIENT_VALUE:  //todebug
                imLoginHandler.kickPcClient(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_PUSH_SHIELD_VALUE: //todebug
                imLoginHandler.pushShield(header, body, ctx);
                break;
            case LoginCmdID.CID_LOGIN_REQ_QUERY_PUSH_SHIELD_VALUE: //todebug
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
            case BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE:
                imBuddyListHandler.userInfoReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE:
                imBuddyListHandler.removeSessionReq(header, body, ctx); //todebug
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE:
                imBuddyListHandler.allUserReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE:
                imBuddyListHandler.userStatusReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE:
                imBuddyListHandler.changeAvaterReq(header, body, ctx); //todebug
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE:
                imBuddyListHandler.departmentReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_REQUEST_VALUE:
                imBuddyListHandler.changeSignInfoReq(header, body, ctx);
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
                imMessageHandler.clientMsgDataAck(header, body, ctx); //不需要在这里处理，可以删除
                break;
            case MessageCmdID.CID_MSG_READ_ACK_VALUE:
                imMessageHandler.readMessage(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_TIME_REQUEST_VALUE: //todebug
            	imMessageHandler.clientTimeReq(header, body, ctx); 
                break;
            case MessageCmdID.CID_MSG_UNREAD_CNT_REQUEST_VALUE:
                imMessageHandler.getUnreadCount(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_LIST_REQUEST_VALUE:
                imMessageHandler.getMessageList(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_GET_LATEST_MSG_ID_REQ_VALUE: 
                imMessageHandler.getLatestMessageId(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_GET_BY_MSG_ID_REQ_VALUE://
                imMessageHandler.getByMessageId(header, body, ctx);
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
            case GroupCmdID.CID_GROUP_INFO_REQUEST_VALUE:  //todebug
                imGroupHandler.groupInfoReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_CREATE_REQUEST_VALUE: //todebug
//            	imGroupHandler.groupCreateReq(header, body, ctx);
                imGroupHandler.createGroupReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_REQUEST_VALUE: //todebug
//            	imGroupHandler.groupChangeMemberReq(header, body, ctx);
                imGroupHandler.changeMemberReq(header, body, ctx);
                break;
            case GroupCmdID.CID_GROUP_SHIELD_GROUP_REQUEST_VALUE://todebug
            	imGroupHandler.groupShieldReq(header, body, ctx);
                break;
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
            case OtherCmdID.CID_OTHER_STOP_RECV_PACKET_VALUE: //不需要实现？
            	imOtherHandler.StopReceivePacket(header, body, ctx);
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
                imSwitchHandler.switchP2p(header, body, ctx); //todebug
                break; 
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }
    
    /**
     * 处理File消息
     * @param ctx 信道
     * @param commandId 命令
     * @param header 消息头
     * @param body 消息体
     * @since  1.0
     */
    public void doFile(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        // 判断是否登录
        if (!hasLogin(ctx)) {
            return ;
        }
        switch (commandId) {
            case FileCmdID.CID_FILE_REQUEST_VALUE:
                imFileHandle.fileReq(header, body, ctx);
                break;
            case FileCmdID.CID_FILE_HAS_OFFLINE_REQ_VALUE:
                imFileHandle.hasOfflineReq(header, body, ctx);
                break;
            case FileCmdID.CID_FILE_ADD_OFFLINE_REQ_VALUE:
                imFileHandle.addOfflineReq(header, body, ctx);
                break;
            case FileCmdID.CID_FILE_DEL_OFFLINE_REQ_VALUE:
                imFileHandle.delOfflineReq(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }
    /**
     * @param ctx
     * @since  1.0
     */
    public void remove(ChannelHandlerContext ctx) {
        
        Long handleId = ctx.attr(ClientUser.HANDLE_ID).get();
        Long userId = ctx.attr(ClientUser.USER_ID).get();

        if (handleId != null) {
            // 关闭
        }
        if (userId == null) {
            return;
        }

        // routerHandler.sendUserStatusUpdate(ctx, UserStatType.USER_STATUS_OFFLINE);
        messageServerCluster.userStatusUpdate(userId, ctx, UserStatType.USER_STATUS_OFFLINE);
        
        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            clientUser.unValidateMsgConn(handleId, ctx);
            
            if (clientUser.isConnEmpty()) {
                ClientUserManager.removeUser(clientUser);
            }
        }
    }
    
    /**
     * 掉线时的处理
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

        // routerHandler.sendUserStatusUpdate(ctx, UserStatType.USER_STATUS_OFFLINE);
        messageServerCluster.userStatusUpdate(userId, ctx, UserStatType.USER_STATUS_OFFLINE);
        
        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            clientUser.unValidateMsgConn(handleId, ctx);
        }
    }

    
    /**
     * 连接时的处理
     * 
     * @param ctx
     * @since  1.0
     */
    public void online(ChannelHandlerContext ctx) {
        
        Long handleId = ctx.attr(ClientUser.HANDLE_ID).get();
        Long userId = ctx.attr(ClientUser.USER_ID).get();

        if (handleId != null) {
            // 关闭
        }
        if (userId == null) {
            return;
        }

        // routerHandler.sendUserStatusUpdate(ctx, UserStatType.USER_STATUS_ONLINE);
        messageServerCluster.userStatusUpdate(userId, ctx, UserStatType.USER_STATUS_OFFLINE);
        
        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            clientUser.validateMsgConn(handleId, ctx);
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
        
        if (ctx.attr(ClientUser.USER_ID).get() != null) {
            return true;
        }
        return false;
    }

}
