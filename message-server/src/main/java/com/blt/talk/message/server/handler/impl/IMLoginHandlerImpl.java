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
import com.blt.talk.common.code.proto.IMBaseDefine.KickReasonType;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ResultType;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMLogin.IMDeviceTokenReq;
import com.blt.talk.common.code.proto.IMLogin.IMDeviceTokenRsp;
import com.blt.talk.common.code.proto.IMLogin.IMKickPCClientReq;
import com.blt.talk.common.code.proto.IMLogin.IMKickPCClientRsp;
import com.blt.talk.common.code.proto.IMLogin.IMLoginReq;
import com.blt.talk.common.code.proto.IMLogin.IMLoginRes;
import com.blt.talk.common.code.proto.IMLogin.IMLogoutReq;
import com.blt.talk.common.code.proto.IMLogin.IMLogoutRsp;
import com.blt.talk.common.code.proto.IMLogin.IMPushShieldReq;
import com.blt.talk.common.code.proto.IMLogin.IMPushShieldRsp;
import com.blt.talk.common.code.proto.IMLogin.IMQueryPushShieldReq;
import com.blt.talk.common.code.proto.IMLogin.IMQueryPushShieldRsp;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.param.KickUserReq;
import com.blt.talk.common.param.LoginReq;
import com.blt.talk.common.param.UserToken;
import com.blt.talk.common.result.LoginCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.message.server.handler.IMLoginHandler;
import com.blt.talk.message.server.handler.RouterHandler;
import com.blt.talk.message.server.manager.ClientUser;
import com.blt.talk.message.server.manager.ClientUserManager;
//import com.blt.talk.message.server.manager.ClientConnection;
//import com.blt.talk.message.server.manager.ClientConnectionMap;
import com.blt.talk.message.server.remote.LoginService;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;



/**
 * 处理【LoginCmdID】
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class IMLoginHandlerImpl extends AbstractUserHandlerImpl implements IMLoginHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoginService loginService;
    @Autowired
    private RouterHandler routerHandler;
    
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
                    long handle = ClientUser.HandleIdGenerator.incrementAndGet();
                    clientUser.addConn(handle, ctx);
                    ctx.attr(ClientUser.HANDLE_ID).set(handle);
                    ctx.attr(ClientUser.USER_ID).set(userRes.getData().getId());
                    ctx.attr(ClientUser.CLIENT_TYPE).set(req.getClientType());
                    ctx.attr(ClientUser.STATUS).set(UserStatType.USER_STATUS_ONLINE);
                }
                // 更新用户状态
                // pMsgConn->SetUserId(user_id);
                // pMsgConn->SetOpen();
                // pMsgConn->SendUserStatusUpdate(IM::BaseDefine::USER_STATUS_ONLINE);
                // pUser->ValidateMsgConn(pMsgConn->GetHandle(), pMsgConn);
                // KickOutSameClientType
                clientUser.setValidate(true);
                
                // 广播
                routerHandler.sendUserStatusUpdate(ctx, IMBaseDefine.UserStatType.USER_STATUS_ONLINE);
                // 广播
                routerHandler.kickOutSameClientType(ctx, IMBaseDefine.KickReasonType.KICK_REASON_DUPLICATE_USER);

                // 后移，防止登录后判断是否在线（PC）失败
                IMBaseDefine.UserInfo userInfo = JavaBean2ProtoBuf.getUserInfo(userRes.getData());

                IMLoginRes res = IMLoginRes.newBuilder().setOnlineStatus(UserStatType.USER_STATUS_ONLINE)
                        .setServerTime(serverTime).setUserInfo(userInfo).setResultCode(ResultType.REFUSE_REASON_NONE)
                        .build();

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE);
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
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
    	
    	IMLogoutReq logoutReq = (IMLogoutReq) body;
    	long userId = super.getUserId(ctx);
    	
    	IMHeader resHeader = null;
    	IMLogoutRsp logoutRsp = null;
    	try {
    		
        	UserToken userToken = new UserToken();
    		userToken.setUserId(userId);
    		userToken.setUserToken("");
        	
    		//先发给给数据库
        	//BaseModel<Long> deviceTokenRes =  loginService.setdeviceToken(userToken);
        	loginService.setDeviceToken(userToken);
        	
        	resHeader = header.clone();
        	resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_REQ_LOGINOUT_VALUE);
        	
        	logoutRsp = IMLogoutRsp.newBuilder()
        			 .setResultCode(0)       			 
        			 .build();
        	ctx.writeAndFlush(new IMProtoMessage<>(resHeader, logoutRsp));
    	} catch (Exception e) {   		
    		logger.error("服务器端异常", e);
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, logoutRsp));
    		
		} finally {   		  		
    		ctx.close().addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					ClientUser clientUser = ClientUserManager.getUserById(userId);
	                if (clientUser == null) {
	                    
	                    logger.debug("用户没有登录过:{}", userId);
	                } else {
	                    logger.debug("用户已登录过，设置参数:{}", userId);
	                    
	                     for (ChannelHandlerContext chc :clientUser.getUnValidateConnSet() ){
	                    	 if (chc == ctx){
	                    		 
	                    		 clientUser.getUnValidateConnSet().remove(ctx);
	                    	 }
	                     }
	                     
	                     for ( ChannelHandlerContext chc :  clientUser.getConnMap().values()){
	                    	 if ( chc == ctx){
	                    		 long handle = chc.attr(ClientUser.HANDLE_ID).get();
	                    		 clientUser.getConnMap().remove(handle);
	                    	 }
	                     }
	                    	
	                     //...
	                     // 还需要其他处理，需要进一步添加
	                     //...
	                                         
	                    ctx.attr(ClientUser.STATUS).set(UserStatType.USER_STATUS_OFFLINE);
	                }
					
					
				}
			});
        }
    
    	
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#kickUser(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    //不在该处实现
    @Override
    public void kickUser(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        // TODO Auto-generated method stub
        logger.warn("++TODO +++++");
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#deviceToken(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void deviceToken(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
    	
    	IMDeviceTokenReq deviceTokenReq = (IMDeviceTokenReq) body;
    	long userId = super.getUserId(ctx);
    	
    	IMHeader resHeader = null;
    	IMDeviceTokenRsp deviceTokenRsp = null;
    	
    	try {   	
    		
    		deviceTokenRsp = IMDeviceTokenRsp.newBuilder()
    				            .setUserId(userId)
    				            .build();    	
    		
    		resHeader = header.clone();
    		resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_DEVICETOKEN_VALUE);
    		
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, deviceTokenRsp));

    		// 保存用户Token
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setClientType(deviceTokenReq.getClientType());
            userToken.setUserToken(deviceTokenReq.getDeviceTokenBytes().toStringUtf8());
            
            loginService.setDeviceToken(userToken);
    	} catch(Exception e){
    		
    		logger.error("服务器端异常", e);
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, deviceTokenRsp));
    	} 
    	 
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#kickPcClient(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    
    @Override
    public void kickPcClient(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
    	IMKickPCClientReq kickPCClientReq = (IMKickPCClientReq)body;
    	long userId = super.getUserId(ctx);
    	IMHeader resHeader = null;
    	IMKickPCClientRsp kickPCClientRsp = null;
    	
    	try{
    		KickUserReq kickUserReq = new KickUserReq();
    		kickUserReq.setUserId(userId);
    		kickUserReq.setUserType(IMBaseDefine.ClientType.CLIENT_TYPE_MAC);
    		kickUserReq.setKickReasonType(KickReasonType.KICK_REASON_MOBILE_KICK);
    		
    		//BaseModel<Long> kickUserRes  = loginService.kickPcClient(kickUserReq);
    		loginService.kickPcClient(kickUserReq);
    		
    		kickPCClientRsp = IMKickPCClientRsp.newBuilder()
    		          .setResultCode(0)
    		          .setUserId(userId)
    		          .build();
    		 		
    		resHeader = header.clone();
    		resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_KICKPCCLIENT_VALUE);
    		   		
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, kickPCClientRsp));		
    		
    	}catch(Exception e){
    		
    	}
        
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#pushShield(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void pushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMPushShieldReq pushShieldReq = (IMPushShieldReq) body;
        long userId = super.getUserId(ctx);
    	IMHeader resHeader = null;
    	IMPushShieldRsp pushShieldRsp = null;
    	
    	try {   	  		
    		BaseModel<Long> pushShieldRes  = loginService.pushShield(pushShieldReq.getUserId());
    		
    		pushShieldRsp = IMPushShieldRsp.newBuilder()
    				            .setUserId(userId)
    				            .setResultCode(pushShieldRes.getCode())
    				            .build();    
    		resHeader = header.clone();
    		resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_PUSH_SHIELD_VALUE);
    		
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, pushShieldRsp));
    		
    	} catch(Exception e){
    		
    		logger.error("服务器端异常", e);
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, pushShieldRsp));
    	} 
    }

    /* (non-Javadoc)
     * @see com.blt.talk.message.server.handler.IMLoginHandler#queryPushShield(com.blt.talk.common.code.proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void queryPushShield(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
    	IMQueryPushShieldReq queryPushShieldReq = (IMQueryPushShieldReq) body;
        long userId = super.getUserId(ctx);
    	
    	IMHeader resHeader = null;
        IMQueryPushShieldRsp queryPushShieldRsp = null;
    	
    	try {   	  		
    		BaseModel<Long> queryPushShieldRes  = loginService.pushShield(queryPushShieldReq.getUserId());
    		
    		queryPushShieldRsp = IMQueryPushShieldRsp.newBuilder()
    				            .setUserId(userId)
    				            .setShieldStatus(queryPushShieldRes.getData().intValue())
    				            .setResultCode(queryPushShieldRes.getCode())
    				            .build();    
    		resHeader = header.clone();
    		resHeader.setCommandId((short)LoginCmdID.CID_LOGIN_RES_QUERY_PUSH_SHIELD_VALUE);
    		
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, queryPushShieldRsp));
    		
    	} catch(Exception e){
    		
    		logger.error("服务器端异常", e);
    		ctx.writeAndFlush(new IMProtoMessage<>(resHeader, queryPushShieldRsp));
    	} 
    }

}
