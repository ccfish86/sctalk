package com.blt.talk.common.code.proto.helper;

import org.bouncycastle.util.encoders.Base64;

import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupType;
import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.DepartmentEntity;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.model.entity.SessionEntity;
import com.blt.talk.common.model.entity.UnreadEntity;
import com.blt.talk.common.model.entity.UserEntity;
import com.blt.talk.common.util.CommonUtils;
import com.google.protobuf.ByteString;

/**
 * Convert a entity to the protobuf message
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class JavaBean2ProtoBuf {

    /**
     * Convert the user information to the protobuf message
     * @param userInfo then user information
     * @return the message of the user information
     * @since  1.0
     */
    public static IMBaseDefine.UserInfo getUserInfo(UserEntity userInfo) {

        IMBaseDefine.UserInfo.Builder userBuilder = IMBaseDefine.UserInfo.newBuilder();
        
        userBuilder.setStatus(userInfo.getStatus());
        if (userInfo.getAvatar() == null)  {
            userBuilder.setAvatarUrl("");
        } else {
            userBuilder.setAvatarUrl(userInfo.getAvatar());
        }
        userBuilder.setDepartmentId(userInfo.getDepartmentId());
        userBuilder.setEmail(userInfo.getEmail());
        userBuilder.setUserGender(userInfo.getGender());
        userBuilder.setUserNickName(userInfo.getMainName());
        userBuilder.setUserTel(userInfo.getPhone());
        userBuilder.setUserDomain(userInfo.getPinyinName());
        userBuilder.setUserRealName(userInfo.getRealName());
        userBuilder.setUserId(userInfo.getPeerId());
        userBuilder.setSignInfo(userInfo.getSignInfo());

        return userBuilder.build();
    }

    public static IMBaseDefine.GroupVersionInfo getGroupVersionInfo(GroupEntity groupInfo){
        
        IMBaseDefine.GroupVersionInfo.Builder groupBuilder = IMBaseDefine.GroupVersionInfo.newBuilder();
        groupBuilder.setGroupId(groupInfo.getId());
        groupBuilder.setVersion(groupInfo.getVersion());
        
        return groupBuilder.build();
    }
    

    public static IMBaseDefine.DepartInfo getDepartmentInfo(DepartmentEntity deptInfo){
        
        IMBaseDefine.DepartInfo.Builder groupBuilder = IMBaseDefine.DepartInfo.newBuilder();
        groupBuilder.setDeptId(deptInfo.getId());
        groupBuilder.setDeptName(deptInfo.getDepartName());
        groupBuilder.setDeptStatus(IMBaseDefine.DepartmentStatusType.forNumber(deptInfo.getStatus()));
        groupBuilder.setParentDeptId(deptInfo.getDepartId());
        groupBuilder.setPriority(deptInfo.getPriority());
        
        return groupBuilder.build();
    }
    
    public static IMBaseDefine.GroupInfo getGroupInfo(GroupEntity groupInfo){
        
        IMBaseDefine.GroupInfo.Builder groupBuilder = IMBaseDefine.GroupInfo.newBuilder();
        groupBuilder.setGroupId(groupInfo.getId());
        groupBuilder.setGroupName(groupInfo.getMainName());
        groupBuilder.setGroupType(GroupType.forNumber(groupInfo.getGroupType()));
        groupBuilder.setGroupAvatar(groupInfo.getAvatar());
        groupBuilder.setVersion(groupInfo.getVersion());
        groupBuilder.setGroupCreatorId(groupInfo.getCreatorId());
        groupBuilder.setShieldStatus(groupInfo.getStatus());
        
        if (groupInfo.getUserList() != null) {
            groupBuilder.addAllGroupMemberList(groupInfo.getUserList());
        }

        return groupBuilder.build();
    }
    

    public static IMBaseDefine.ContactSessionInfo getContactSessionInfo(SessionEntity sessionInfo){
        
        IMBaseDefine.ContactSessionInfo.Builder sessionInfoBuilder = IMBaseDefine.ContactSessionInfo.newBuilder();
        sessionInfoBuilder.setLatestMsgFromUserId(sessionInfo.getTalkId());
        sessionInfoBuilder.setLatestMsgType(IMBaseDefine.MsgType.forNumber(sessionInfo.getLatestMsgType()));
        sessionInfoBuilder.setSessionType(IMBaseDefine.SessionType.forNumber(sessionInfo.getPeerType()));
        sessionInfoBuilder.setSessionId(sessionInfo.getPeerId());
        sessionInfoBuilder.setUpdatedTime(sessionInfo.getUpdated());
        
        return sessionInfoBuilder.build();
    }
    
    public static IMBaseDefine.UnreadInfo getUnreadInfo(UnreadEntity unreadEntity) {
        IMBaseDefine.UnreadInfo.Builder unreadBuilder = IMBaseDefine.UnreadInfo.newBuilder();
        unreadBuilder.setLatestMsgId(unreadEntity.getLaststMsgId());
        unreadBuilder.setLatestMsgFromUserId(unreadEntity.getLatestMsgFromUserId());
        unreadBuilder.setLatestMsgType(IMBaseDefine.MsgType.forNumber(unreadEntity.getLaststMsgType()));
        // unreadBuilder.setLatestMsgData(ByteString.copyFromUtf8(unreadEntity.getLatestMsgData()));
        if (unreadEntity.getLatestMsgData() != null) {
            unreadBuilder.setLatestMsgData(ByteString.copyFromUtf8(unreadEntity.getLatestMsgData()));
        } else {
            unreadBuilder.setLatestMsgData(ByteString.EMPTY);
        }
        unreadBuilder.setSessionId(unreadEntity.getPeerId());
        unreadBuilder.setSessionType(IMBaseDefine.SessionType.forNumber(unreadEntity.getSessionType()));
        unreadBuilder.setUnreadCnt(unreadEntity.getUnReadCnt());
        
        return unreadBuilder.build();
    }

    /**
     * @param message
     * @return
     * @since  1.0
     */
    public static IMBaseDefine.MsgInfo getMessageInfo(MessageEntity message) {
        IMBaseDefine.MsgInfo.Builder messageBuilder =IMBaseDefine.MsgInfo.newBuilder();
        messageBuilder.setMsgId(message.getMsgId());
        messageBuilder.setFromSessionId(message.getFromId());
        messageBuilder.setMsgType(IMBaseDefine.MsgType.forNumber(message.getMsgType()));
        messageBuilder.setCreateTime(message.getCreated());
        
        if (CommonUtils.isAudio(message.getMsgType())) {
            messageBuilder.setMsgData(ByteString.copyFrom(Base64.decode(message.getContent())));
        } else {
            messageBuilder.setMsgData(ByteString.copyFromUtf8(message.getContent()));
        }
        return messageBuilder.build();
    }
}
