/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.BuddyListCmdID;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMOther;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.message.server.MessageServerStarter;
import com.blt.talk.message.server.RouterServerConnecter;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理与Router-server相关的业务
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class RouterHandlerManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * 30秒一次心跳包
     * 
     * @since  1.0
     */
    @Scheduled(fixedRate = 300000)
    public void sendHeartBeat() {
        
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        IMOther.IMHeartBeat beartHeart = IMOther.IMHeartBeat.newBuilder().build();
        IMHeader header = new IMHeader();
        header.setServiceId((short)IMBaseDefine.ServiceID.SID_OTHER_VALUE);
        header.setCommandId((short)IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE);
        
        // 发送心跳包
        routerConnector.send(new IMProtoMessage<>(header, beartHeart));
    }

    /**
     * 每分钟一次发送用户信息
     * 
     * @since  1.0
     */
    @Scheduled(fixedRate = 600000)
    public void sendUserInfo() {
        
        RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
        IMHeader header = new IMHeader();
        header.setServiceId((short)IMBaseDefine.ServiceID.SID_OTHER_VALUE);
        header.setCommandId((short)IMBaseDefine.OtherCmdID.CID_OTHER_ONLINE_USER_INFO_VALUE);
        
        List<IMBaseDefine.ServerUserStat> userStatList = ClientUserManager.getOnlineUser();

        IMServer.IMOnlineUserInfo.Builder userInfoBuilder = IMServer.IMOnlineUserInfo.newBuilder();
        userInfoBuilder.addAllUserStatList(userStatList);
        
        // 发送心跳包
        routerConnector.send(new IMProtoMessage<>(header, userInfoBuilder.build()));
    }

    /**
     * @param ctx
     * @param commandId
     * @param header
     * @param body
     * @since  1.0
     */
    public void doBuddyList(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

        logger.info("doBuddyList");
        switch (commandId) {
//            case BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST_VALUE:
//                break;
            case BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE:
                // send friend online message to client
                sendStatusNotify(header, body, ctx);
                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE:
//                break;
            case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE:
                // send back to user
                sendUsersStatus(header, body, ctx);
                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_REQUEST_VALUE:
//                break;
//            case BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE:
//                break;
            default:
                logger.warn("Unsupport command id {}", commandId);
                break;
        }
    }

    /**
     * @param header
     * @param body
     * @param ctx 
     * @since  1.0
     */
    private void sendStatusNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // IMBuddy.IMUserStatNotify userStatNotify = (IMBuddy.IMUserStatNotify)body;
        // 通知好友及关联群组的用户在线状况
        // FIXME 原程序为通知当前服务器所有用户
//        for (ClientConnection client :ClientConnectionMap.allClientMap.values()) {
//            client.getCtx().writeAndFlush(new IMProtoMessage<>(header, body));
//        }
    }

    /**
     * @param header
     * @param body
     * @param ctx
     * @since  1.0
     */
    private void sendUsersStatus(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMBuddy.IMUsersStatRsp usersStatRsp = (IMBuddy.IMUsersStatRsp) body;
        ClientUser client = ClientUserManager.getUserById(usersStatRsp.getUserId());
        if (client != null) {
            client.broadcast(new IMProtoMessage<MessageLite>(header, body), ctx);
        }
    }

    /**
     * @param ctx
     * @param commandId
     * @param header
     * @param body
     * @since  1.0
     */
    public void doMessage(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param ctx
     * @param commandId
     * @param header
     * @param body
     * @since  1.0
     */
    public void doOther(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param ctx
     * @param commandId
     * @param header
     * @param body
     * @since  1.0
     */
    public void doSwitch(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 发送当前Message-server的信息
     * 
     * @param ctx
     * @since  1.0
     */
    @Async
    public void sendServerInfo(ChannelHandlerContext ctx) {
        
        MessageServerStarter routerConnector = applicationContext.getBean(MessageServerStarter.class);

        try {
            while (!routerConnector.isStarted()) {
                    Thread.sleep(5000);
            }
    
            ClientUserManager.ClientUserConn clientConn = ClientUserManager.getUserConn();
            
            String ip = routerConnector.getIpadress();
            int port = routerConnector.getPort();
            IMServer.IMMsgServInfo msgServerInfo = IMServer.IMMsgServInfo.newBuilder()
                    .setHostName(ip).setIp1(ip).setIp2(ip).setPort(port).setCurConnCnt(clientConn.getTotalCount()).setMaxConnCnt(0).build();
            
            IMHeader header = new IMHeader();
            header.setServiceId((short)IMBaseDefine.ServiceID.SID_OTHER_VALUE);
            header.setCommandId((short)IMBaseDefine.OtherCmdID.CID_OTHER_MSG_SERV_INFO_VALUE);
            
            ctx.writeAndFlush(new IMProtoMessage<>(header, msgServerInfo));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
