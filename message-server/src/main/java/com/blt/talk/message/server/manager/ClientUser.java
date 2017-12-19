/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.ClientType;
import com.blt.talk.common.code.proto.IMBaseDefine.LoginCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.ServiceID;
import com.blt.talk.common.code.proto.IMBaseDefine.UserStatType;
import com.blt.talk.common.code.proto.IMLogin;
import com.blt.talk.common.constant.SysConstant;
import com.blt.talk.common.util.CommonUtils;
import com.google.protobuf.MessageLite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户端信息
 * <br>
 * 可管理多端同时连接
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class ClientUser {

//    public static final AtomicLong HandleIdGenerator = new AtomicLong();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private long userId;
    private String loginName;
    private String nickName;
    private boolean updated;
    private int status; 
    private boolean validate;
    
    private Map<Long, ChannelHandlerContext> connMap = new HashMap<>();
    private List<ChannelHandlerContext> unValidateConnSet = new ArrayList<>();
    
    public static final AttributeKey<Long> HANDLE_ID = AttributeKey.valueOf("HANDLE_ID");
    public static final AttributeKey<Long> USER_ID = AttributeKey.valueOf("USER_ID");
    public static final AttributeKey<ClientType> CLIENT_TYPE = AttributeKey.valueOf("CLIENT_TYPE");
    public static final AttributeKey<UserStatType> STATUS = AttributeKey.valueOf("STATUS");
    
    public ClientUser() {
        this.validate = false;
        this.userId = 0;
        this.updated = false;
        this.status = UserStatType.USER_STATUS_OFFLINE_VALUE;
    }

    public ClientUser(String userName) {
        this();
        this.loginName = userName;
    }
    
    
    /**
     * @param ctx
     */
    public ClientUser(ChannelHandlerContext ctx, long userId, long handleId, ClientType clientType, UserStatType statType) {
        this();
        this.userId = userId;
        this.connMap.put(handleId, ctx);
        ctx.attr(HANDLE_ID).set(handleId);
        ctx.attr(USER_ID).set(userId);
        ctx.attr(CLIENT_TYPE).set(clientType);
        ctx.attr(STATUS).set(statType);
    }

    public ChannelHandlerContext getConn(long handle) {
        return connMap.get(handle);
    }
    
    public void addConn(long handle, ChannelHandlerContext conn) {
        connMap.put(handle, conn);
    }

    public void delConn(long handle) {
        connMap.remove(handle);
    }
    
    public ChannelHandlerContext getUnvalidateConn(long handle) {
        for (ChannelHandlerContext clientConn: unValidateConnSet) {
            if (clientConn.attr(HANDLE_ID).get() == handle) {
                return clientConn;
            }
        }
        return null;
    }
    
    public void addUnvalidateConn(ChannelHandlerContext conn) {
        unValidateConnSet.add(conn);
    }
    
    public void delUnvalidateConn(ChannelHandlerContext conn) {
        unValidateConnSet.remove(conn);
    }
    
    public void validateMsgConn(long handle, ChannelHandlerContext conn) {
        this.addConn(handle, conn);
        this.delUnvalidateConn(conn);
    }

    /**
     * 设置为无效连接
     * @param handle
     * @param conn
     * @since  1.0
     */
    public void unValidateMsgConn(long handle, ChannelHandlerContext conn) {
        this.delConn(handle);
        this.addUnvalidateConn(conn);
    }
    
    public UserConn getUserConn() {
        int count = 0;
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn.channel().isOpen()) {
                count++;
            }
        }
        return new UserConn(this.userId, count);
    }
    
    public void broadcast(IMProtoMessage<MessageLite> message, ChannelHandlerContext fromCtx) {
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn != fromCtx) {
                logger.debug("发送消息> {}", conn.channel().remoteAddress());
                conn.writeAndFlush(message);
                // conn > AddToSendList
            }
        }
        
    }

    public void broadcastWithOutMobile(IMProtoMessage<MessageLite> message, ChannelHandlerContext fromCtx) {
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn != fromCtx && CommonUtils.isPc(conn.attr(CLIENT_TYPE).get())) {
                logger.debug("发送消息> {}", conn.channel().remoteAddress());
                conn.writeAndFlush(message);
            }
        }
    }
    
    public void broadcastToMobile(IMProtoMessage<MessageLite> message, ChannelHandlerContext fromCtx) {
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn != fromCtx && CommonUtils.isMobile(conn.attr(CLIENT_TYPE).get())) {
                logger.debug("发送消息> {}", conn.channel().remoteAddress());
                conn.writeAndFlush(message);
            }
        }
    }
    
    public void broadcaseMessage(IMProtoMessage<MessageLite> message, long messageId, ChannelHandlerContext fromCtx, long fromId) {
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn != fromCtx) {
                logger.debug("发送消息> {}", conn.channel().remoteAddress());
                conn.writeAndFlush(message);
                // conn AddToSendList
            }
        }
    }
    
    public void broadcastData(ByteBuf message, int len,  ChannelHandlerContext fromCtx) {
        for (ChannelHandlerContext conn: connMap.values()) {
            if (conn != fromCtx) {
                logger.debug("发送消息> {}", conn.channel().remoteAddress());
                conn.writeAndFlush(message);
            }
        }
    }
    
    public void kickUser(ChannelHandlerContext conn, int reason) {
        long handle = conn.attr(HANDLE_ID).get();
        
        IMLogin.IMKickUser message = IMLogin.IMKickUser.newBuilder().setUserId(this.userId)
                .setKickReason(IMBaseDefine.KickReasonType.forNumber(reason)).build();
        
        IMHeader header = new IMHeader();
        header.setServiceId((short)ServiceID.SID_LOGIN_VALUE);
        header.setCommandId((short)LoginCmdID.CID_LOGIN_KICK_USER_VALUE);

        conn.writeAndFlush(new IMProtoMessage<>(header, message));
        conn.close();
        // conn ->SetKickOff();
        // conn ->Close();
    }
    /**
     * 只支持一个WINDOWS/MAC客户端登陆,或者一个ios/android登录
     * @param clientType
     * @param reason
     * @param fromCtx
     * @since  1.0
     */
    public void kickSameClientType(int clientType, int reason, ChannelHandlerContext fromCtx) {
        for (ChannelHandlerContext conn: connMap.values()) {
            int connClientType = conn.attr(CLIENT_TYPE).get().getNumber();
            // 16进制位移计算
            if ((connClientType ^ clientType) >> 4 == 0 && conn != fromCtx) {
                this.kickUser(conn, reason);
            }
        }
    }
    /**
     * 判断是PC/Mobile登录
     * 
     * @see SysConstant#CLIENT_TYPE_FLAG_NONE
     * @see SysConstant#CLIENT_TYPE_FLAG_PC
     * @see SysConstant#CLIENT_TYPE_FLAG_MOBILE
     * @see SysConstant#CLIENT_TYPE_FLAG_BOTH
     * @return 客户端登录类型
     * @since  1.0
     */
    public int getClientFlag() {
        
        int clientFlag = SysConstant.CLIENT_TYPE_FLAG_NONE;
        
        for (ChannelHandlerContext conn: connMap.values()) {
            int connClientType = conn.attr(CLIENT_TYPE).get().getNumber();
            if (CommonUtils.isPc(connClientType)) {
                clientFlag |= SysConstant.CLIENT_TYPE_FLAG_PC; 
            } else if (CommonUtils.isMobile(connClientType)) {
                clientFlag |= SysConstant.CLIENT_TYPE_FLAG_MOBILE;
            } else {
                // 不识别
            }
        }
        return clientFlag;
    }
    
    public boolean isConnEmpty() {
        return connMap.isEmpty();
    }
    
    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }
    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    /**
     * @return the nickName
     */
    public String getNickName() {
        return nickName;
    }
    /**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    /**
     * @return the updated
     */
    public boolean isUpdated() {
        return updated;
    }
    /**
     * @param updated the updated to set
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * @return the validate
     */
    public boolean isValidate() {
        return validate;
    }
    /**
     * @param validate the validate to set
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    /**
     * @return the connMap
     */
    public Map<Long, ChannelHandlerContext> getConnMap() {
        return connMap;
    }

    /**
     * @return the unvalidateConnSet
     */
    public List<ChannelHandlerContext> getUnValidateConnSet() {
        return unValidateConnSet;
    }

    public static class UserConn {
        private long userId;
        private int connCount;
        
        public UserConn(long userId, int count) {
            this.userId = userId;
            this.connCount = count;
        }
        /**
         * @return the userId
         */
        public long getUserId() {
            return userId;
        }
        /**
         * @param userId the userId to set
         */
        public void setUserId(long userId) {
            this.userId = userId;
        }
        /**
         * @return the connCount
         */
        public int getConnCount() {
            return connCount;
        }
        /**
         * @param connCount the connCount to set
         */
        public void setConnCount(int connCount) {
            this.connCount = connCount;
        }
    }
}
