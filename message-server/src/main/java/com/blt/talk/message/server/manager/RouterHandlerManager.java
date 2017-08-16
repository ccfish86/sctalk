/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.manager;

import java.util.ArrayList;
import java.util.List;

//import org.apache.sshd.client.session.ClientUserAuthService;
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
import com.blt.talk.common.code.proto.IMBaseDefine.FileCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.MessageCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.OtherCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.SwitchServiceCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMBuddy;
import com.blt.talk.common.code.proto.IMBuddy.IMPCLoginStatusNotify;
import com.blt.talk.common.code.proto.IMFile;
import com.blt.talk.common.code.proto.IMGroup.IMGroupChangeMemberNotify;
import com.blt.talk.common.code.proto.IMMessage.IMMsgData;
import com.blt.talk.common.code.proto.IMMessage.IMMsgDataReadNotify;
import com.blt.talk.common.code.proto.IMOther;
import com.blt.talk.common.code.proto.IMServer;
import com.blt.talk.common.code.proto.IMServer.IMServerKickUser;
import com.blt.talk.common.code.proto.IMServer.IMServerPCLoginStatusNotify;
import com.blt.talk.common.code.proto.IMSwitchService;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.common.util.CommonUtils;
import com.blt.talk.message.server.MessageServerStarter;
import com.blt.talk.message.server.RouterServerConnecter;
import com.blt.talk.message.server.remote.GroupService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理与Router-server相关的业务
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class RouterHandlerManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 30秒一次心跳包
	 * 
	 * @since 1.0
	 */
	@Scheduled(fixedRate = 300000)
	public void sendHeartBeat() {

		RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
		IMOther.IMHeartBeat beartHeart = IMOther.IMHeartBeat.newBuilder().build();
		IMHeader header = new IMHeader();
		header.setServiceId((short) IMBaseDefine.ServiceID.SID_OTHER_VALUE);
		header.setCommandId((short) IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE);

		// 发送心跳包
		routerConnector.send(new IMProtoMessage<>(header, beartHeart));
	}

	/**
	 * 每分钟一次发送用户信息
	 * 
	 * @since 1.0
	 */
	@Scheduled(fixedRate = 60000)
	public void sendUserInfo() {

		RouterServerConnecter routerConnector = applicationContext.getBean(RouterServerConnecter.class);
		IMHeader header = new IMHeader();
		header.setServiceId((short) IMBaseDefine.ServiceID.SID_OTHER_VALUE);
		header.setCommandId((short) IMBaseDefine.OtherCmdID.CID_OTHER_ONLINE_USER_INFO_VALUE);

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
	 * @since 1.0
	 */
	public void doBuddyList(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {

		logger.info("doBuddyList");
		switch (commandId) {
		// case
		// BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST_VALUE:
		// break;
		case BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE:
			// send friend online message to client
			sendStatusNotify(header, body, ctx);
			break;
		// case BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE:
		// break;
		// case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE:
		// break;
		// case BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE:
		// break;
		case BuddyListCmdID.CID_BUDDY_LIST_USERS_STATUS_RESPONSE_VALUE:
			// send back to user
			sendUsersStatus(header, body, ctx);
			break;
		// case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE:
		// break;
		// case BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE:
		// break;
		 case BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_NOTIFY_VALUE: //todebug
			 removeSessionNotify(header, body, ctx);
		 break;
		// case BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE:
		// break;
		// case BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE:
		// break;
		// case BuddyListCmdID.CID_BUDDY_LIST_CHANGE_SIGN_INFO_REQUEST_VALUE:
		// break;
		 case BuddyListCmdID.CID_BUDDY_LIST_SIGN_INFO_CHANGED_NOTIFY_VALUE: //todebug
			  signInfoChangedNotify(header, body, ctx);
		 break;
		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	/**
	 * 
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void signInfoChangedNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		//IMBuddy.IMSignInfoChangedNotify signInfoChangedNotify = (IMBuddy.IMSignInfoChangedNotify) body;
		
		//这样处理是否合理，需要检查？
		ClientUserManager.broadCast(new IMProtoMessage<>(header, body), SysConstant.CLIENT_TYPE_FLAG_BOTH);
		
	}

	/**
	 * 
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void removeSessionNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		IMBuddy.IMRemoveSessionNotify removeSessionNotify = (IMBuddy.IMRemoveSessionNotify) body;
		ClientUser clientUser = ClientUserManager.getUserById(removeSessionNotify.getUserId()) ;
		
		//这样处理是否合理，需要检查？
		if (clientUser != null){
			clientUser.broadcast(new IMProtoMessage<>(header, body), null);
		}
		
	}

	/**
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void sendStatusNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		// 通知好友及关联群组的用户在线状况
		ClientUserManager.broadCast(new IMProtoMessage<>(header, body), SysConstant.CLIENT_TYPE_FLAG_PC);
	}

	/**
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
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
	 * @since 1.0
	 */
	public void doMessage(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
		logger.debug("RouterHandleManager#doMessage");
		switch (commandId) {

		case MessageCmdID.CID_MSG_READ_NOTIFY_VALUE:
			handleMsgReadNotify(header,body, ctx);
			break;
		case MessageCmdID.CID_MSG_DATA_VALUE:
		    handleMsgData(header,body, ctx);
			break;

		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	/**
	 * @param ctx
	 * @param commandId
	 * @param header
	 * @param body
	 * @since 1.0
	 */
	public void doOther(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
		logger.debug("RouterHandleManager#doOther");
		switch (commandId) {
		case OtherCmdID.CID_OTHER_SERVER_KICK_USER_VALUE:
			handleKickUser(body, ctx);
			break;
		case OtherCmdID.CID_OTHER_LOGIN_STATUS_NOTIFY_VALUE:
			handlePCLoginStatusNotify(header,body, ctx);
			break;
		case OtherCmdID.CID_OTHER_HEARTBEAT_VALUE://无需实现
			break;			
		case OtherCmdID.CID_OTHER_ROLE_SET_VALUE://目前不需要实现			
			break;
		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	/**
	 * @param ctx
	 * @param commandId
	 * @param header
	 * @param body
	 * @since 1.0
	 */
	public void doSwitch(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
		logger.debug("RouterHandleManager#doSwitch");
		switch (commandId) {
		case SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE://todebug
			switchP2p(header, body, ctx);
		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	
	/**
	 * 
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void switchP2p(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		
		IMSwitchService.IMP2PCmdMsg switchP2p = (IMSwitchService.IMP2PCmdMsg) body;
		
		ClientUser fromUser = ClientUserManager.getUserById(switchP2p.getFromUserId());
		ClientUser toUser = ClientUserManager.getUserById(switchP2p.getToUserId());
		
		//这样处理是否合理，需要检查？
		if(fromUser != null){
			fromUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
		}
		
		if(toUser != null){
			toUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
		}			
		
	}

	/**
	 * @param ctx
	 * @param commandId
	 * @param header
	 * @param body
	 * @since 1.0
	 */
	public void doGroup(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
		logger.debug("RouterHandleManager#doSwitch");
		switch (commandId) {
		case GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE://todebug
			groupChangeMemberNotify(header, body, ctx);
			break;
		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	/**
	 * 
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void groupChangeMemberNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		IMGroupChangeMemberNotify groupChangeMemberNotify = (IMGroupChangeMemberNotify)body;
		ClientUser clientUser = null;
		
		for( long chgUserId : groupChangeMemberNotify.getChgUserIdListList()){
			clientUser = ClientUserManager.getUserById(chgUserId);
			if (clientUser != null){
				clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
			}
		}
		
		for( long curUserId : groupChangeMemberNotify.getCurUserIdListList()){
			clientUser = ClientUserManager.getUserById(curUserId);
			if (clientUser != null){
				clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
			}
		}		
		
	}

	/**
	 * @param ctx
	 * @param commandId
	 * @param header
	 * @param body
	 * @since 1.0
	 */
	public void doFile(ChannelHandlerContext ctx, short commandId, IMHeader header, MessageLite body) {
		logger.debug("RouterHandleManager#doSwitch");
		switch (commandId) {
		case FileCmdID.CID_FILE_NOTIFY_VALUE://todebug
			fileNotify(header, body, ctx);
			break;
		default:
			logger.warn("Unsupport command id {}", commandId);
			break;
		}
	}

	/**
	 * @param header
	 * @param body
	 * @param ctx
	 * @since 1.0
	 */
	private void fileNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
		IMFile.IMFileNotify fileNotify = (IMFile.IMFileNotify) body;
		
		ClientUser clientUser = ClientUserManager.getUserById(fileNotify.getToUserId());
		
		if (clientUser != null){
			clientUser.broadcast(new IMProtoMessage<MessageLite>(header, body), null);
		}
		
		
	}

	/**
	 * 发送当前Message-server的信息
	 * 
	 * @param ctx
	 * @since 1.0
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
			IMServer.IMMsgServInfo msgServerInfo = IMServer.IMMsgServInfo.newBuilder().setHostName(ip).setIp1(ip)
					.setIp2(ip).setPort(port).setCurConnCnt(clientConn.getTotalCount()).setMaxConnCnt(0).build();

			IMHeader header = new IMHeader();
			header.setServiceId((short) IMBaseDefine.ServiceID.SID_OTHER_VALUE);
			header.setCommandId((short) IMBaseDefine.OtherCmdID.CID_OTHER_MSG_SERV_INFO_VALUE);

			ctx.writeAndFlush(new IMProtoMessage<>(header, msgServerInfo));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送当前踢人消息 handleKickUser
	 * 
	 * @param MessageLite
	 * @param ChannelHandlerContext
	 * @since 1.0 李春生
	 */
    private void handleKickUser(MessageLite body, ChannelHandlerContext ctx) {


        // 转换body中的数据,判断是否是真正的kickUser消息,如果是,则进行下面的操作,不是抛出异常
        IMServerKickUser kickUser = (IMServerKickUser) body;

        long userId = kickUser.getUserId();
        int clientType = kickUser.getClientType().getNumber();
        int reason = kickUser.getReason();
        logger.debug("HandleKickUser, userId={}, clientType={}, reason={}", userId, clientType, reason);

        ClientUser clientUser = ClientUserManager.getUserById(userId);
        if (clientUser != null) {
            // 踢掉用户,根据ClientType进行判断
            clientUser.kickSameClientType(clientType, reason, null);
        }
    }

	/**
	 * PC登陆状态消息
	 * 
	 * @param MessageLite
	 * @param ChannelHandlerContext
	 * @throws InvalidProtocolBufferException 
	 * @since 1.0 李春生
	 */
	private void handlePCLoginStatusNotify(IMHeader header,MessageLite body, ChannelHandlerContext ctx)  {

		IMServerPCLoginStatusNotify msg = (IMServerPCLoginStatusNotify) body;
	
		long userId = msg.getUserId();
		int loginStatus = msg.getLoginStatus();
		logger.debug("HandlePCLoginStatusNotify, user_id={}, login_status={}", userId, loginStatus);

		ClientUser pUser = ClientUserManager.getUserById(userId);
		if (pUser != null) {
			pUser.setStatus(loginStatus);
			IMPCLoginStatusNotify.Builder loginStatusBuilder = IMPCLoginStatusNotify.newBuilder();
			loginStatusBuilder.setUserId(userId);

			if (1 == loginStatus) {
				loginStatusBuilder.setLoginStat(UserStatType.USER_STATUS_ONLINE);

			} else {
				loginStatusBuilder.setLoginStat(UserStatType.USER_STATUS_OFFLINE);

			}
			IMPCLoginStatusNotify msg2 = loginStatusBuilder.build();


			header.setServiceId((short) IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE);
			header.setCommandId((short) IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE);
			pUser.broadcastToMobile(new IMProtoMessage<MessageLite>(header, msg2), null);

		}
	}

	/**
	 * Message读消息
	 * @param MessageLite
	 * @param ChannelHandlerContext
	 * @throws InvalidProtocolBufferException 
	 * @since 1.0
	 * @author 李春生
	 */
	private void handleMsgReadNotify(IMHeader header, MessageLite body, ChannelHandlerContext ctx)  {

		 IMMsgDataReadNotify msg = (IMMsgDataReadNotify) body;	

        long reqId = msg.getUserId();

        ClientUser pUser = ClientUserManager.getUserById(reqId);
        if (pUser != null) {

            pUser.broadcast(new IMProtoMessage<MessageLite>(header, msg), null);
        }
		    
	}

	/**
	 * Message数据消息
	 * 
	 * @param MessageLite
	 * @param ChannelHandlerContext
	 * @since 1.0 
	 * @author 袁贵
	 */
	private void handleMsgData(IMHeader header,MessageLite body, ChannelHandlerContext ctx) {

        IMMsgData msg = (IMMsgData) body;

        if (CommonUtils.isGroup(msg.getMsgType())) {
            // 团队消息
            handleGroupMessageBroadcast(header, msg);
            return;
        }

        long fromUserId = msg.getFromUserId();
        long toUserId = msg.getToSessionId();

        IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msg);

        ClientUser fromClientUser = ClientUserManager.getUserById(fromUserId);
        ClientUser toClientUser = ClientUserManager.getUserById(toUserId);
        if (fromClientUser != null) {
            // 应该不会再把消息发送至最初的发出设备
            fromClientUser.broadcast(message, null);
        }
        if (toClientUser != null) {
            // 应该不会再把消息发送至最初的发出设备
            toClientUser.broadcast(message, null);
        }
    }

    /**
     * 群消息发送
     * 
     * @param header
     * @param msgdata
     * @since 1.0
     */
    private void handleGroupMessageBroadcast(IMHeader header, IMMsgData msgdata) {
        
        GroupService groupService = applicationContext.getBean(GroupService.class);
        
        // 服务器没有群的信息，向DB服务器请求群信息，并带上消息作为附件，返回时在发送该消息给其他群成员
        // 查询群员，然后推送消息
        List<Long> groupIdList = new ArrayList<>();
        groupIdList.add(msgdata.getToSessionId());
        BaseModel<List<GroupEntity>> groupListRes = groupService.groupInfoList(groupIdList);
        if (groupListRes.getCode() == GroupCmdResult.SUCCESS.getCode()) {
            if(groupListRes.getData() != null && !groupListRes.getData().isEmpty()) {
                List<Long> memberList = groupListRes.getData().get(0).getUserList();
                
                if (memberList.contains(msgdata.getFromUserId())) {
                    //用户在群中
                    IMProtoMessage<MessageLite> message = new IMProtoMessage<>(header, msgdata);
                    for (long userId : memberList) {
                        ClientUser clientUser = ClientUserManager.getUserById(userId);
                        if (clientUser == null) {
                            continue;
                        }
                        
                        clientUser.broadcast(message, null);
                    }
                }
            }
        }
    
    }
}
