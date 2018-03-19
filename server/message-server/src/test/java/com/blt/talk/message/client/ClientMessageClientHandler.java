package com.blt.talk.message.client;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.DefaultIMHeader;
import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MsgType;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMLogin.IMLoginReq;
import com.blt.talk.common.code.proto.IMLogin.IMLoginRes;
import com.blt.talk.common.code.proto.IMMessage.IMMsgData;
import com.blt.talk.common.code.proto.IMOther.IMHeartBeat;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.common.util.SecurityUtils;
import com.blt.talk.message.server.handler.MessageServerHandler;
import com.google.protobuf.ByteString;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*
 * Copyright © 2013-2017 BLT, Co., Ltd. All Rights Reserved.
 */


/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientMessageClientHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);
    
    private String name;
    
    public ClientMessageClientHandler(String name) {
        this.name = name;
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
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {

        // 暂时只支持一次接收一个完整的消息
        // 如需要处理组合消息（长消息分多次发），需要另外处理，特别是解码逻辑
        ClientMessage<ByteString> message = (ClientMessage<ByteString>) object;

        // ctx.fireChannelReadComplete();
        IMHeader header = message.getHeader();
        
        // 处理请求分发
        switch(header.getServiceId()) {
            case ServiceID.SID_LOGIN_VALUE:
                // LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE:
                if (header.getCommandId() == LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE) {
                    IMLoginRes loginRes = IMLoginRes.parseFrom(message.getBody());
                    logger.warn("登录成功：{}", loginRes.getUserInfo());
                    // 发送心跳
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    hb(ctx);
                                    Thread.sleep(30 * 1000);
                                    msg(ctx);
                                    Thread.sleep(10 * 1000);
                                }
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
            default:
                logger.warn("暂不支持的服务ID{}" , header.getServiceId());
                break;
        }
    
    }

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
        login(ctx);
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
    
    private void login(ChannelHandlerContext ctx) {
        // this.name;
        IMLoginReq req = IMLoginReq.newBuilder().setUserName(name)
                .setPassword(SecurityUtils.getInstance().EncryptPass(name)).setClientVersion("1")
                .setClientType(ClientType.CLIENT_TYPE_ANDROID)
                .setOnlineStatus(UserStatType.USER_STATUS_ONLINE).build();
        
        IMHeader header = new DefaultIMHeader(LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<IMLoginReq>(header, req));
    }

    private void hb(ChannelHandlerContext ctx) {
        IMHeartBeat heartBeet = IMHeartBeat.newBuilder().build();
        IMHeader header = new DefaultIMHeader(OtherCmdID.CID_OTHER_HEARTBEAT_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<IMHeartBeat>(header, heartBeet));
    }
    
    private void msg(ChannelHandlerContext ctx) {
        // 发送随机消息
        byte[] msgData = SecurityUtils.getInstance().EncryptMsg(RandomStringUtils.randomAscii(20));
        IMMsgData msg = IMMsgData.newBuilder().setFromUserId(Long.valueOf(this.name))
                .setToSessionId(Long.valueOf(this.name) + 2).setMsgId(0).setMsgType(MsgType.MSG_TYPE_SINGLE_TEXT).setMsgData(ByteString.copyFrom(msgData)).setCreateTime(CommonUtils.currentTimeSeconds()).build();
        IMHeader header = new DefaultIMHeader(MessageCmdID.CID_MSG_DATA_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<IMMsgData>(header, msg));
    }
}
