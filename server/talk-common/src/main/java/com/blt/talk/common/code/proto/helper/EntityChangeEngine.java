package com.blt.talk.common.code.proto.helper;

import com.blt.talk.common.model.MessageEntity;
import com.blt.talk.common.model.entity.SessionEntity;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public class EntityChangeEngine {

    public static SessionEntity getSessionEntity(MessageEntity msg){
        SessionEntity sessionEntity = new SessionEntity();

        // [图文消息] [图片] [语音]
        sessionEntity.setLatestMsgData(msg.getMessageDisplay());
        sessionEntity.setUpdated(msg.getUpdated());
        sessionEntity.setCreated(msg.getUpdated());
        sessionEntity.setLatestMsgId(msg.getMsgId());
        //sessionEntity.setPeerId(msg.getFromId());
        sessionEntity.setTalkId(msg.getFromId());
        sessionEntity.setPeerType(msg.getSessionType());
        sessionEntity.setLatestMsgType(msg.getMsgType());
        return  sessionEntity;
    }

    // todo enum
    // 组建与解析统一地方，方便以后维护
    public static String getSessionKey(long peerId,int sessionType){
        String sessionKey = sessionType + "_" + peerId;
        return sessionKey;
    }

    public static String[] spiltSessionKey(String sessionKey){
        if(sessionKey == null || sessionKey.length() == 0){
            throw new IllegalArgumentException("spiltSessionKey error,cause by empty sessionKey");
        }
        String[] sessionInfo = sessionKey.split("_",2);
        return sessionInfo;
    }
}
