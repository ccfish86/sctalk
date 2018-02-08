package com.blt.talk.message.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
//import com.blt.talk.message.server.manager.ClientConnectionMap;
import com.blt.talk.message.server.manager.HandlerManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 用来处理用户过来的消息
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class MessageServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

    private final HandlerManager handlerManager;

    public MessageServerHandler(HandlerManager handlerManager) {
        super();
        this.handlerManager = handlerManager;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        logger.debug("channel#handlerAdded");
        // 保存客户端连接
        // ClientConnectionMap.addClientConnection(ctx);
    }

    /**
     * 每当服务端断开客户端连接时,客户端的channel从ChannelGroup中移除,并通知列表中其他客户端channel
     * 
     * @param ctx 连接context
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        // 1. 已经与远程主机建立的连接，远程主机主动关闭连接，或者网络异常连接被断开的情况
        // 2. 已经与远程主机建立的连接，本地客户机主动关闭连接的情况
        // 3. 本地客户机在试图与远程主机建立连接时，遇到类似与connection refused这样的异常，未能连接成功时
        // 而只有当本地客户机已经成功的与远程主机建立连接（connected）时，连接断开的时候才会触发channelDisconnected事件，即对应上述的1和2两种情况。
        logger.debug("channel#handlerRemoved");
        handlerManager.remove(ctx);
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {

        // 暂时只支持一次接收一个完整的消息
        // 如需要处理组合消息（长消息分多次发），需要另外处理，特别是解码逻辑
        IMProtoMessage<MessageLite> message = (IMProtoMessage<MessageLite>) object;

        // ctx.fireChannelReadComplete();
        logger.info("channel#channelReadComplete");
        
        logger.info("channel#channelRead0");
        IMHeader header = message.getHeader();
        
        // 处理请求分发
        switch(header.getServiceId()) {
            case IMBaseDefine.ServiceID.SID_LOGIN_VALUE:
                handlerManager.doLogin(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE:
                handlerManager.doBuddyList(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_MSG_VALUE:
                handlerManager.doMessage(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_GROUP_VALUE:
                handlerManager.doGroup(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_OTHER_VALUE:
                handlerManager.doOther(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_FILE_VALUE:
                handlerManager.doFile(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_SWITCH_SERVICE_VALUE:
                handlerManager.doSwitch(ctx, header.getCommandId(), header, message.getBody());
                break;
            case IMBaseDefine.ServiceID.SID_AVCALL_VALUE: // for webrtc
                handlerManager.doWebrtc(ctx, header.getCommandId(), header, message.getBody());
                break;
            default:
                logger.warn("暂不支持的服务ID{}" , header.getServiceId());
                break;
        }
    
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        
//    }

    /**
     * 服务端监听到客户端活动
     * 
     * @param ctx 连接context
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 服务端接收到客户端上线通知
        Channel incoming = ctx.channel();
        logger.debug("MessageServerHandler:" + incoming.remoteAddress() + "在线");
        handlerManager.online(ctx);
        ctx.fireChannelActive();
    }

    /**
     * 服务端监听到客户端不活动
     * 
     * @param ctx 连接context
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 服务端接收到客户端掉线通知
        Channel incoming = ctx.channel();
        logger.debug("MessageServerHandler:" + incoming.remoteAddress() + "掉线");
        handlerManager.offline(ctx);
        ctx.fireChannelInactive();
    }
    /**
     * 当服务端的IO 抛出异常时被调用
     * 
     * @param ctx 连接context
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // super.exceptionCaught(ctx, cause);
        Channel incoming = ctx.channel();
        logger.debug("MessageServerHandler异常:{}", incoming.remoteAddress());

        // 异常出现就关闭连接
        ctx.fireExceptionCaught(cause).close();
    }
}
