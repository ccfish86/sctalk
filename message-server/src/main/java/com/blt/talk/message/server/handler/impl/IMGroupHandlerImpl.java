/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ResultType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListReq;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListRsp;
import com.blt.talk.common.code.proto.IMLogin.IMLoginRes;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupNormalListReq;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.message.server.handler.IMGroupHandler;
import com.blt.talk.message.server.remote.GroupService;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理【GroupCmdID】
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
@Component
public class IMGroupHandlerImpl implements IMGroupHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GroupService groupService;
    
    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMGroupHandler#normalListReq(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void normalListReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        
        IMNormalGroupListReq req = (IMNormalGroupListReq)body;
        
        try {
        BaseModel<List<GroupEntity>> groupListRes = groupService.normalList(req.getUserId());
        
        if (groupListRes.getCode() != GroupCmdResult.SUCCESS.getCode()) {

            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short)GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE);
            
            IMNormalGroupListRsp res = IMNormalGroupListRsp.newBuilder().setUserId(req.getUserId()).build();
            ctx.write(new IMProtoMessage<>(resHeader, res));
        } else {

            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short)GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE);
            
            IMNormalGroupListRsp.Builder groupResBuilder = IMNormalGroupListRsp.newBuilder();
            groupResBuilder.setUserId(req.getUserId());
            if (groupListRes.getData() != null) {
                groupListRes.getData().forEach(groupEntity -> {
                    groupResBuilder.addGroupVersionList(JavaBean2ProtoBuf.getGroupVersionInfo(groupEntity));
                });
            }
            ctx.write(new IMProtoMessage<>(resHeader, groupResBuilder.build()));
        }} catch (Exception e) {
        
            logger.error("服务器端异常", e);
            IMLoginRes res;
            
            res = IMLoginRes.newBuilder()
                    .setOnlineStatus(UserStatType.USER_STATUS_ONLINE)
                    .setResultCode(ResultType.REFUSE_REASON_DB_VALIDATE_FAILED).setResultString("服务器端异常").buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE);
            
            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        
        }
    }

}
