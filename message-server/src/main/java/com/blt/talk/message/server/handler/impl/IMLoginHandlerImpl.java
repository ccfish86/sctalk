/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ResultType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMLogin.IMLoginReq;
import com.blt.talk.common.code.proto.IMLogin.IMLoginRes;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.result.LoginCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.message.server.handler.IMLoginHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
//import com.blt.talk.message.server.manager.ClientConnection;
//import com.blt.talk.message.server.manager.ClientConnectionMap;
import com.blt.talk.message.server.remote.LoginService;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理【LoginCmdID】
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class IMLoginHandlerImpl implements IMLoginHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoginService loginService;
    
    /**
     * 
     * @param header
     * @param msg
     * @param ctx
     * @throws Exception
     * @since  3.0
     */
    @Override
    public void login(IMHeader header, MessageLite msg, ChannelHandlerContext ctx) throws Exception {

        int serverTime = CommonUtils.currentTimeSeconds();
        IMLoginReq req = (IMLoginReq) msg;
        String userName = req.getUserName();
        String password = req.getPassword();
        
//        Map<String, String> params = new HashMap<>();
//        params.put("name", userName);
//        
//        ResponseEntity<String> userList = restTemplate.getForEntity("http://localhost:10060/user/search/findByName", String.class, params);
        
        // FIXME 处理登录前，先判断是否已经登录
        // 已登录的，踢掉
        
        
        LoginReq loginReq = new LoginReq();
        loginReq.setName(userName);
        loginReq.setPassword(password);
        
        try {

            BaseModel<UserEntity> userRes = loginService.login(loginReq);
            
            if (userRes.getCode() != LoginCmdResult.SUCCESS.getCode()) {
                
                IMLoginRes res = IMLoginRes.newBuilder().setServerTime(serverTime)
                        .setOnlineStatus(UserStatType.USER_STATUS_ONLINE)
                        .setResultCode(ResultType.REFUSE_REASON_DB_VALIDATE_FAILED).setResultString(userRes.getMsg()).buildPartial();
                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE);
                
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
            } else {
                IMBaseDefine.UserInfo userInfo = JavaBean2ProtoBuf.getUserInfo(userRes.getData());

                IMLoginRes res = IMLoginRes.newBuilder().setOnlineStatus(UserStatType.USER_STATUS_ONLINE)
                        .setServerTime(serverTime).setUserInfo(userInfo).setResultCode(ResultType.REFUSE_REASON_NONE)
                        .build();
                
                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE);
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
                
                // 查询同一用户的其他Client
                ClientUser clientUser = ClientUserManager.getUserById(userRes.getData().getId());
                if (clientUser == null) {
                    
                    logger.debug("登录成功:{}", userRes.getData().getId());
                    clientUser = new ClientUser(ctx, userRes.getData().getId(), req.getClientType(), UserStatType.USER_STATUS_ONLINE);
                    clientUser.setLoginName(userRes.getData().getRealName());
                    clientUser.setNickName(userRes.getData().getMainName());
                    ClientUserManager.addUserById(userRes.getData().getId(), clientUser);
                } else {
                    logger.debug("登录成功，设置参数:{}", userRes.getData().getId());
                    clientUser.addUnvalidateConn(ctx);
                    ctx.attr(ClientUser.HANDLE_ID).set(ClientUser.HandleIdGenerator.get());
                    ctx.attr(ClientUser.USER_ID).set(userRes.getData().getId());
                    ctx.attr(ClientUser.CLIENT_TYPE).set(req.getClientType());
                    ctx.attr(ClientUser.STATUS).set(UserStatType.USER_STATUS_ONLINE);
                }
            }
            
        } catch (Exception e) {
            
            logger.error("服务器端异常", e);
            IMLoginRes res;
            
            res = IMLoginRes.newBuilder().setServerTime(serverTime)
                    .setOnlineStatus(UserStatType.USER_STATUS_ONLINE)
                    .setResultCode(ResultType.REFUSE_REASON_DB_VALIDATE_FAILED).setResultString("服务器端异常").buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE);
            
            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#logOut(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void logOut(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#kickUser(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void kickUser(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#deviceToken(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void deviceToken(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#kickPcClient(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void kickPcClient(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#pushShield(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void pushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#queryPushShield(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void queryPushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        
    }

}
