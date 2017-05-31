/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blt.talk.common.code.IMHeader;
import com.blt.talk.common.code.IMProtoMessage;
import com.blt.talk.common.code.proto.IMBaseDefine;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupCmdID;
import com.blt.talk.common.code.proto.IMBaseDefine.GroupType;
import com.blt.talk.common.code.proto.IMGroup;
import com.blt.talk.common.code.proto.IMGroup.IMGroupChangeMemberReq;
import com.blt.talk.common.code.proto.IMGroup.IMGroupCreateReq;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListReq;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListRsp;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.param.GroupUpdateMemberReq;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.message.server.handler.IMGroupHandler;
import com.blt.talk.message.server.remote.GroupService;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import io.netty.channel.ChannelHandlerContext;


/**
 * 处理【GroupCmdID】
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Component
public class IMGroupHandlerImpl implements IMGroupHandler {



    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GroupService groupService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMGroupHandler#normalListReq(com.blt.talk.common.code.
     * proto.Header, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void normalListReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMNormalGroupListReq req = (IMNormalGroupListReq) body;

        try {
            BaseModel<List<GroupEntity>> groupListRes = groupService.normalList(req.getUserId());

            if (groupListRes.getCode() != GroupCmdResult.SUCCESS.getCode()) {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE);

                IMNormalGroupListRsp res = IMNormalGroupListRsp.newBuilder().setUserId(req.getUserId()).build();
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
            } else {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE);

                IMNormalGroupListRsp.Builder groupResBuilder = IMNormalGroupListRsp.newBuilder();
                groupResBuilder.setUserId(req.getUserId());
                if (groupListRes.getData() != null) {
                    List<IMBaseDefine.GroupVersionInfo> groupList = new ArrayList<>();
                    groupListRes.getData().forEach(groupEntity -> {
                        groupList.add(JavaBean2ProtoBuf.getGroupVersionInfo(groupEntity));
                    });
                    groupResBuilder.addAllGroupVersionList(groupList);
                }
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, groupResBuilder.build()));
            }
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMNormalGroupListRsp res;

            res = IMNormalGroupListRsp.newBuilder().setUserId(req.getUserId()).buildPartial();
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, res));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMGroupHandler#groupInfoReq(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void groupInfoReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMGroup.IMGroupInfoListReq req = (IMGroup.IMGroupInfoListReq) body;

        try {

            Map<String, Integer> groupIdList = new HashMap<>();
            for (IMBaseDefine.GroupVersionInfo groupVersion : req.getGroupVersionListList()) {
                groupIdList.put(String.valueOf(groupVersion.getGroupId()), groupVersion.getVersion());
            }

            BaseModel<List<GroupEntity>> groupListRes = groupService.groupInfoList(groupIdList);

            if (groupListRes.getCode() != GroupCmdResult.SUCCESS.getCode()) {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

                IMGroup.IMGroupInfoListRsp.Builder resBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
                resBuilder.setUserId(req.getUserId());
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
            } else {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

                IMGroup.IMGroupInfoListRsp.Builder groupResBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
                groupResBuilder.setUserId(req.getUserId());
                if (groupListRes.getData() != null) {
                    List<IMBaseDefine.GroupInfo> groupInfoList = new ArrayList<>();
                    for (GroupEntity groupEntity : groupListRes.getData()) {
                        groupInfoList.add(JavaBean2ProtoBuf.getGroupInfo(groupEntity));
                    }
                    groupResBuilder.addAllGroupInfoList(groupInfoList);
                }
                IMGroup.IMGroupInfoListRsp groupRes = groupResBuilder.build();
                logger.debug("IMGroupInfoListRsp {}", groupRes);
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, groupRes));
            }
        } catch (Exception e) {

            logger.error("服务器端异常", e);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

            IMGroup.IMGroupInfoListRsp.Builder resBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
            resBuilder.setUserId(req.getUserId());
            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, resBuilder.build()));
        }
    }

    @Override
    public void groupShieldReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMGroup.IMGroupShieldReq req = (IMGroup.IMGroupShieldReq) body;

        try {

            BaseModel<Integer> groupShieldRes = groupService.groupShieldStatus(req.getUserId());

            // 远程查询数据库成功，并且获取到数据
            if (groupShieldRes.getCode() == GroupCmdResult.SUCCESS.getCode() && groupShieldRes.getData() != null) {
                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_SHIELD_GROUP_RESPONSE_VALUE);

                IMGroup.IMGroupShieldRsp groupShieldBody =
                        IMGroup.IMGroupShieldRsp.newBuilder().setUserId(req.getUserId()).setGroupId(req.getGroupId())
                                .setResultCode(groupShieldRes.getData()).build();

                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, groupShieldBody));

            } else {
                logger.error("获取群组免打扰状态失败");
                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_SHIELD_GROUP_RESPONSE_VALUE);

                IMGroup.IMGroupShieldRsp groupShieldBody =
                        IMGroup.IMGroupShieldRsp.newBuilder().setUserId(req.getUserId()).setGroupId(req.getGroupId())
                                .setAttachData(ByteString.copyFrom("获取群组免打扰状态失败", Charset.forName("UTF-8"))) // 异常或获取值失败时，发送的信息
                                .build();
                ctx.writeAndFlush(new IMProtoMessage<>(resHeader, groupShieldBody));

            }


        } catch (Exception e) {

            logger.error("服务器端异常", e);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_SHIELD_GROUP_RESPONSE_VALUE);

            IMGroup.IMGroupShieldRsp groupShieldBody =
                    IMGroup.IMGroupShieldRsp.newBuilder().setUserId(req.getUserId()).setGroupId(req.getGroupId())
                            .setAttachData(ByteString.copyFrom("获取群组免打扰状态失败", Charset.forName("UTF-8"))) // 异常或获取值失败时，发送的信息
                            .build();
            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, groupShieldBody));
        }


    }

    // @Override
    // public void groupCreateReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
    //
    // IMGroup.IMGroupCreateReq req = (IMGroup.IMGroupCreateReq) body;
    //
    // try {
    //
    // GroupEntity createGroupInfo = new GroupEntity();
    // createGroupInfo.setMainName(req.getGroupName());
    // createGroupInfo.setAvatar(req.getGroupAvatar());
    // createGroupInfo.setCreatorId(req.getUserId());
    // createGroupInfo.setGroupType(req.getGroupType().getNumber());
    // createGroupInfo.setStatus(1);
    // createGroupInfo.setVersion(3);
    //
    //
    //
    // BaseModel<GroupEntity> groupCreateRes = groupService.groupCreateReq(createGroupInfo);
    //
    // //远程查询数据库成功，并且获取到数据
    // if (groupCreateRes.getCode() == GroupCmdResult.SUCCESS.getCode()) {
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CREATE_RESPONSE_VALUE);
    //
    // IMGroup.IMGroupCreateRsp groupCreateBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .setUserId(req.getUserId())
    // .addAllUserIdList(groupCreateRes.getData().getUserList())
    // .setGroupId(groupCreateRes.getData().getId())
    // .setGroupName(groupCreateRes.getData().getMainName())
    // .setResultCode(groupCreateRes.getCode())
    // .build();
    //
    // logger.debug("addAllUserIdList {}", groupCreateBody);
    //
    // ctx.write(new IMProtoMessage<>(resHeader, groupCreateBody));
    //
    // } else {
    //
    // logger.error("创建群组失败");
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CREATE_RESPONSE_VALUE);
    //
    // IMGroup.IMGroupCreateRsp groupCreateBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .setResultCode(GroupCmdResult.LIST_NORMAL_FAILD.getCode())
    // .setAttachData(ByteString.copyFrom("创建群组失败", Charset.forName("UTF-8"))) //异常或获取值失败时，发送的信息
    // .build();
    // ctx.write(new IMProtoMessage<>(resHeader, groupCreateBody));
    //
    // }
    //
    //
    // } catch (Exception e) {
    //
    // logger.error("服务器端异常", e);
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CREATE_RESPONSE_VALUE);
    //
    // IMGroup.IMGroupCreateRsp groupCreateBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .setResultCode(GroupCmdResult.LIST_NORMAL_FAILD.getCode())
    // .setAttachData(ByteString.copyFrom("创建群组失败", Charset.forName("UTF-8"))) //异常或获取值失败时，发送的信息
    // .build();
    // ctx.write(new IMProtoMessage<>(resHeader, groupCreateBody));
    // }
    //
    //
    // }

    // @Override
    // public void groupChangeMemberReq(IMHeader header, MessageLite body, ChannelHandlerContext
    // ctx) {
    //
    // IMGroup.IMGroupChangeMemberReq req = (IMGroup.IMGroupChangeMemberReq) body;
    //
    // try {
    //
    // GroupEntity groupChangeMember = new GroupEntity();
    // groupChangeMember.setId(req.getGroupId());
    // groupChangeMember.setCreatorId(req.getUserId());
    // groupChangeMember.setUserList(req.getMemberIdListList());
    //
    // BaseModel<GroupEntity> groupChangeRes = groupService.groupChangeMemberReq(groupChangeMember);
    //
    // //远程查询数据库成功，并且获取到数据
    // if (groupChangeRes.getCode() == GroupCmdResult.SUCCESS.getCode()) {
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE);
    //
    // //到底需要填什么，可能根据业务需要
    // IMGroup.IMGroupCreateRsp groupChangeMemberBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .addAllUserIdList(groupChangeRes.getData().getUserList())
    // .setGroupId(groupChangeRes.getData().getId())
    // .setGroupName(groupChangeRes.getData().getMainName())
    // .setResultCode(groupChangeRes.getCode())
    // .build();
    //
    // logger.debug("addAllUserIdList {}", groupChangeMemberBody);
    //
    // ctx.write(new IMProtoMessage<>(resHeader, groupChangeMemberBody));
    //
    // } else {
    // logger.error("更新组用户失败");
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE);
    //
    // IMGroup.IMGroupCreateRsp groupChangeMemberBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .setResultCode(GroupCmdResult.LIST_NORMAL_FAILD.getCode())
    // .setAttachData(ByteString.copyFrom("更新组用户失败", Charset.forName("UTF-8"))) //异常或获取值失败时，发送的信息
    // .build();
    // ctx.write(new IMProtoMessage<>(resHeader, groupChangeMemberBody));
    // }
    //
    //
    // } catch (Exception e) {
    //
    // logger.error("服务器端异常", e);
    // IMHeader resHeader = header.clone();
    // resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE);
    //
    // IMGroup.IMGroupCreateRsp groupChangeMemberBody = IMGroup.IMGroupCreateRsp.newBuilder()
    // .setResultCode(GroupCmdResult.LIST_NORMAL_FAILD.getCode())
    // .setAttachData(ByteString.copyFrom("更新组用户失败", Charset.forName("UTF-8"))) //异常或获取值失败时，发送的信息
    // .build();
    // ctx.write(new IMProtoMessage<>(resHeader, groupChangeMemberBody));
    // }
    //
    //
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMGroupHandler#createGroupReq(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void createGroupReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {
        IMGroupCreateReq msg = (IMGroupCreateReq) body;

        long reqUserId = msg.getUserId();
        String groupName = msg.getGroupName();
        GroupType groupType = msg.getGroupType();
        if (groupType == IMBaseDefine.GroupType.GROUP_TYPE_NORMAL) {
            return;
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setAvatar(msg.getGroupAvatar());
        groupEntity.setCreatorId(msg.getUserId());
        groupEntity.setGroupType(groupType.getNumber());
        groupEntity.setMainName(msg.getGroupName());
        groupEntity.setUserList(msg.getMemberIdListList());

        try {
            BaseModel<Long> groupCreateRsp = groupService.createGroup(groupEntity);

            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CREATE_RESPONSE_VALUE);
            IMGroup.IMGroupCreateRsp.Builder rspBuilder = IMGroup.IMGroupCreateRsp.newBuilder();
            rspBuilder.setUserId(reqUserId);
            rspBuilder.setGroupName(groupName);
            rspBuilder.setGroupId(groupCreateRsp.getData());
            rspBuilder.addAllUserIdList(msg.getMemberIdListList());
            rspBuilder.setResultCode(groupCreateRsp.getCode());

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, rspBuilder.build()));
        } catch (Exception e) {
            logger.warn("创建群失败！", e);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CREATE_RESPONSE_VALUE);
            IMGroup.IMGroupCreateRsp.Builder rspBuilder = IMGroup.IMGroupCreateRsp.newBuilder();
            rspBuilder.setUserId(reqUserId);
            rspBuilder.setGroupName(groupName);
            rspBuilder.setResultCode(1);

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, rspBuilder.build()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.blt.talk.message.server.handler.IMGroupHandler#changeMemberReq(com.blt.talk.common.code.
     * IMHeader, com.google.protobuf.MessageLite, io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void changeMemberReq(IMHeader header, MessageLite body, ChannelHandlerContext ctx) {

        IMGroupChangeMemberReq msg = (IMGroupChangeMemberReq) body;

        GroupUpdateMemberReq updateReq = new GroupUpdateMemberReq();
        updateReq.setGroupId(msg.getGroupId());
        updateReq.setUserId(msg.getUserId());
        updateReq.setUpdType(msg.getChangeType());
        updateReq.setUserIds(msg.getMemberIdListList());

        try {
            BaseModel<List<Long>> groupUpdateRsp = groupService.changeGroupMember(updateReq);

            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE);
            IMGroup.IMGroupChangeMemberRsp.Builder rspBuilder = IMGroup.IMGroupChangeMemberRsp.newBuilder();
            rspBuilder.setUserId(msg.getUserId());
            rspBuilder.setGroupId(msg.getGroupId());
            rspBuilder.setChangeType(msg.getChangeType());
            rspBuilder.setResultCode(groupUpdateRsp.getCode());
            rspBuilder.addAllChgUserIdList(msg.getMemberIdListList());
            rspBuilder.addAllCurUserIdList(groupUpdateRsp.getData());

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, rspBuilder.build()));
        } catch (Exception e) {

            logger.warn("更改成员群失败！", e);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE);
            IMGroup.IMGroupChangeMemberRsp.Builder rspBuilder = IMGroup.IMGroupChangeMemberRsp.newBuilder();
            rspBuilder.setUserId(msg.getUserId());
            rspBuilder.setGroupId(msg.getGroupId());
            rspBuilder.setChangeType(msg.getChangeType());
            rspBuilder.setResultCode(1);
            rspBuilder.addAllChgUserIdList(msg.getMemberIdListList());

            ctx.writeAndFlush(new IMProtoMessage<>(resHeader, rspBuilder.build()));
        }
    }
}
