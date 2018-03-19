/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMFile;
import com.blt.talk.common.code.proto.IMServer.IMFileTransferReq;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.message.server.cluster.MessageServerCluster;
import com.blt.talk.message.server.handler.IMFileHandle;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理文件传输
 * @author 袁贵
 * @version 1.1
 * @since  1.0
 */
@Component
public class IMFileHandleImpl extends AbstractUserHandlerImpl implements IMFileHandle {

    @Autowired
    private MessageServerCluster messageServerCluster;
    
    private final Logger logger = LoggerFactory.getLogger(IMFileHandleImpl.class);
    
    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#fileReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void fileReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMFile.IMFileReq fileReq = (IMFile.IMFileReq)body;
        long userId = super.getUserId(ctx);
        
        Object fileServConn = getFileServConn();
        IMBaseDefine.TransferFileType transMode = fileReq.getTransMode();
        
        if (fileServConn != null) {
            
            long toId = fileReq.getToUserId();
            
            if (transMode == IMBaseDefine.TransferFileType.FILE_TYPE_OFFLINE) {
                // TODO pFileConn->SendPdu(&pdu); CID_OTHER_FILE_TRANSFER_REQ
            } else {
                ClientUser toUser = ClientUserManager.getUserById(toId);
                if (toUser != null && (toUser.getClientFlag() & SysConstant.CLIENT_TYPE_FLAG_PC) == SysConstant.CLIENT_TYPE_FLAG_PC) {
                    // TODO pFileConn->SendPdu(&pdu); CID_OTHER_FILE_TRANSFER_REQ
                } else {
                    // 无对应用户的pc登录状态,向route_server查询状态
                    ListenableFuture<IMBaseDefine.UserStat> userStatFuture = messageServerCluster.userStatusReq(toId);
                    userStatFuture.addCallback((IMBaseDefine.UserStat userStat) -> {
                        
                        IMFileTransferReq.Builder fileTransFerReqBuilder = IMFileTransferReq.newBuilder();
                        
                        if (userStat.getStatus() == IMBaseDefine.UserStatType.USER_STATUS_OFFLINE) {
                            // transMode < FILE_TYPE_OFFLINE
                        }
                        // TODO pFileConn->SendPdu(&pdu); CID_OTHER_FILE_TRANSFER_REQ
                        
                    }, (Throwable e) -> {
                        //  异常处理
                        logger.warn("处理文件发送异常", e);
                    });
                }
            }
        } else {
            // 无文件服务器
            // no file server. 
            IMFile.IMFileRsp fileRsp = IMFile.IMFileRsp.newBuilder().setResultCode(1)
                    .setFromUserId(userId).setToUserId(fileReq.getToUserId())
                    .setFileName(fileReq.getFileName()).setTaskId("").setTransMode(fileReq.getTransMode()).build();
            
            IMHeader resHeader = header.clone();
            resHeader.setCommandId(IMBaseDefine.FileCmdID.CID_FILE_RESPONSE_VALUE);
            
            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, fileRsp));
        }
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#hasOfflineReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void hasOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMFile.IMFileHasOfflineReq hasOfflineReq = (IMFile.IMFileHasOfflineReq)body;
        long userId = super.getUserId(ctx);
        
        // TODO 处理离线文件
        
        IMFile.IMFileHasOfflineRsp.Builder rspBuilder = IMFile.IMFileHasOfflineRsp.newBuilder();
        rspBuilder.setUserId(userId);
        
        IMHeader resHeader = header.clone();
        resHeader.setCommandId((short)IMBaseDefine.FileCmdID.CID_FILE_HAS_OFFLINE_RES_VALUE);
        ctx.writeAndFlush(new IMProtoMessage<>(resHeader, rspBuilder.build()));
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#addOfflineReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void addOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        long userId = super.getUserId(ctx);
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#delOfflineReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void delOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        long userId = super.getUserId(ctx);
        
    }

    //FIXME
    Object getFileServConn() {
        return null;
    }
}
