/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.message.server.handler.impl;

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
import com.blt.talk.common.code.proto.IMGroup;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListReq;
import com.blt.talk.common.code.proto.IMGroup.IMNormalGroupListRsp;
import com.blt.talk.common.code.proto.helper.JavaBean2ProtoBuf;
import com.blt.talk.common.model.BaseModel;
import com.blt.talk.common.model.entity.GroupEntity;
import com.blt.talk.common.result.GroupCmdResult;
import com.blt.talk.message.server.handler.IMGroupHandler;
import com.blt.talk.message.server.remote.GroupService;
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
                ctx.write(new IMProtoMessage<>(resHeader, res));
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
                ctx.write(new IMProtoMessage<>(resHeader, groupResBuilder.build()));
            }
        } catch (Exception e) {
            logger.error("服务器端异常", e);
            IMNormalGroupListRsp res;

            res = IMNormalGroupListRsp.newBuilder()
                    .setUserId(req.getUserId())
                    .buildPartial();
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
            for (IMBaseDefine.GroupVersionInfo groupVersion:req.getGroupVersionListList()) {
                groupIdList.put(String.valueOf(groupVersion.getGroupId()), groupVersion.getVersion());
            }
            
            BaseModel<List<GroupEntity>> groupListRes = groupService.groupInfoList(groupIdList);

            if (groupListRes.getCode() != GroupCmdResult.SUCCESS.getCode()) {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

                IMGroup.IMGroupInfoListRsp.Builder resBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
                resBuilder.setUserId(req.getUserId());
                ctx.write(new IMProtoMessage<>(resHeader, resBuilder.build()));
            } else {

                IMHeader resHeader = header.clone();
                resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

                IMGroup.IMGroupInfoListRsp.Builder groupResBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
                groupResBuilder.setUserId(req.getUserId());
                if (groupListRes.getData() != null) {
                    List<IMBaseDefine.GroupInfo> groupInfoList = new ArrayList<>();
                    for (GroupEntity groupEntity: groupListRes.getData()) {
                        groupInfoList.add(JavaBean2ProtoBuf.getGroupInfo(groupEntity));
                    }
                    groupResBuilder.addAllGroupInfoList(groupInfoList);
                }
                IMGroup.IMGroupInfoListRsp groupRes = groupResBuilder.build();
                logger.debug("IMGroupInfoListRsp {}",  groupRes);
                ctx.write(new IMProtoMessage<>(resHeader, groupRes));
            }
        } catch (Exception e) {

            logger.error("服务器端异常", e);
            IMHeader resHeader = header.clone();
            resHeader.setCommandId((short) GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE);

            IMGroup.IMGroupInfoListRsp.Builder resBuilder = IMGroup.IMGroupInfoListRsp.newBuilder();
            resBuilder.setUserId(req.getUserId());
            ctx.write(new IMProtoMessage<>(resHeader, resBuilder.build()));
        }
    }

}
