/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMFile;
import com.blt.talk.message.server.handler.IMFileHandle;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMFileHandleImpl implements IMFileHandle {

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#fileReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void fileReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#hasOfflineReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void hasOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMFile.IMFileHasOfflineReq hasOfflineReq = (IMFile.IMFileHasOfflineReq)body;
        
        // TODO 处理离线文件
        
        IMFile.IMFileHasOfflineRsp.Builder rspBuilder = IMFile.IMFileHasOfflineRsp.newBuilder();
        rspBuilder.setUserId(hasOfflineReq.getUserId());
        
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
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMFileHandle#delOfflineReq(com.blt.talk.common.code.IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void delOfflineReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

}
