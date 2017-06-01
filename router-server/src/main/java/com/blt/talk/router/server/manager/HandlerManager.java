/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */
package com.blt.talk.router.server.manager;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.router.server.handler.IMBuddyListHandler;
import com.blt.talk.router.server.handler.IMOtherHandler;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;


/**
 * 由消息服务器过来的Message根据ServiceId和CommandId分别进行处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    @Autowired
    private IMBuddyListHandler imBuddyListHandler;

    @Autowired
    private IMOtherHandler imOtherHandler;

    /**
     * 登录相关处理
     * <br>
     * RouterServer暂时无相关功能
     * 
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @throws Exception
     * @since 1.0
     */
    public void doLogin(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) throws Exception {
        switch (commandId) {
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * 通讯录相关处理
     * 
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doBuddyList(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        switch (commandId) {
            case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_REQUEST_VALUE:
                imBuddyListHandler.userStatusReq(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            case BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE:
                broadcastMsg(header, body);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doMessage(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        switch (commandId) {
            case MessageCmdID.CID_MSG_DATA_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            case MessageCmdID.CID_MSG_READ_NOTIFY_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }


    /**
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doGroup(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        switch (commandId) {
            case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * Other业务处理
     * 
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @since 1.0
     */
    public void doOther(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        switch (commandId) {
            case OtherCmdID.CID_OTHER_HEARTBEAT_VALUE:
                imOtherHandler.hearBeat(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_ROLE_SET_VALUE:
                imOtherHandler.roleSet(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_ONLINE_USER_INFO_VALUE:
                imOtherHandler.onlineUserInfo(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_USER_STATUS_UPDATE_VALUE:
                imOtherHandler.onlineUserStatus(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_MSG_SERV_INFO_VALUE:
                imOtherHandler.updateMessageServer(header, body, ctx);
                break;
            case OtherCmdID.CID_OTHER_USER_CNT_UPDATE_VALUE: //todo
                imOtherHandler.updateUserCnt(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * Switch相关处理
     * <br>
     * 如用户之间的动作同步（如：正在输入...）
     * 
     * @param ctx 连接（MessageServer-RouterServer）
     * @param commandId 操作类型ID
     * @param header 消息头
     * @param body 消息体
     * @since  1.0
     */
    public void doSwitch(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        switch (commandId) {
            case SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE:
                broadcastMsg(header, body, ctx);
                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }
    
    /**
     * 广播MessageServer
     * 
     * @param header 消息头
     * @param body 消息体
     * @since  1.0
     */
    protected void broadcastMsg(IMHeader header, MessageLite body) {

        Collection<ClientConnection> clientConnections = ClientConnectionMap.getAllClient();
        
        if (!clientConnections.isEmpty()) {
            for (ClientConnection conn : clientConnections) {
                // BroadcastMsg
                conn.getCtx().writeAndFlush(new IMProtoMessage<MessageLite>(header, body));
            }
        }
    }

    /**
     * 广播MessageServer
     * <br>
     * 由一个MessageServer过来的消息，向其他MessageServer进行转发
     * 
     * @param header 消息头
     * @param body 消息体
     * @param ctx 连接（MessageServer-RouterServer）
     * @since  1.0
     */
    protected void broadcastMsg(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        Collection<ClientConnection> clientConnections = ClientConnectionMap.getAllClient();
        
        if (!clientConnections.isEmpty()) {
            for (ClientConnection conn : clientConnections) {
                if (conn.getCtx() == ctx) {
                    continue;
                }
                // BroadcastMsg
                conn.getCtx().writeAndFlush(new IMProtoMessage<MessageLite>(header, body));
            }
        }
    }
}
