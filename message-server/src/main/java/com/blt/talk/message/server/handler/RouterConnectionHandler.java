/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.manager.RouterHandlerManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 用于处理Router-server过来的消息
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class RouterConnectionHandler extends SimpleChannelInboundHandler<IMProtoMessage<MessageLite>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RouterHandlerManager routerHanderManager;
    private RouterServerConnecter connecter;

    /**
     * @param routerHanderManager
     * @param connecter
     */
    public RouterConnectionHandler(RouterHandlerManager routerHanderManager, RouterServerConnecter connecter) {
        this.routerHanderManager = routerHanderManager;
        this.connecter = connecter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("与服务器建立连接");
        // 报告当前Message-server的IP和端口号
        routerHanderManager.sendServerInfo(ctx);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMProtoMessage<MessageLite> msg) throws Exception {
        logger.debug("收到消息");
        IMProtoMessage<MessageLite> message = (IMProtoMessage<MessageLite>)msg;
        IMHeader header = message.getHeader();
        
        // 处理请求分发
        switch(header.getServiceId()) {
        
            case IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE:
                routerHanderManager.doBuddyList(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_MSG_VALUE:
                routerHanderManager.doMessage(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_OTHER_VALUE:
                routerHanderManager.doOther(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_SWITCH_SERVICE_VALUE:
                routerHanderManager.doSwitch(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_FILE_VALUE:
                routerHanderManager.doFile(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_GROUP_VALUE:
                routerHanderManager.doGroup(ctx, header.getCommandId(), header, message.getBody());
                break;
            default:
                logger.warn("暂不支持的服务ID{}" , header.getServiceId());
                break;
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("与服务器断开连接服务器:{}" , "removed");
        super.handlerRemoved(ctx);
        if (connecter != null) {
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    connecter.doConnect();
                }
            }, 10, TimeUnit.SECONDS);
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与服务器断开连接服务器:{}" , "inactive");
        super.channelInactive(ctx);
//
//        // 重新连接服务器
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                connecter.doConnect();
//            }
//        }, 10, TimeUnit.SECONDS);
//        ctx.close();
    }
    
    @Override  
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {  
        super.channelUnregistered(ctx);
    }  
}
