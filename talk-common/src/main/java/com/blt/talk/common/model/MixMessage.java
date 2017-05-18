package com.blt.talk.common.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blt.talk.common.constant.DBConstant;

/**
 * @author : yingmu on 15-1-14.
 * @email : yingmu@mogujie.com.
 */
public class MixMessage extends MessageEntity {

    public List<MessageEntity> msgList ;


    /**
     * 从net端解析需要
     * @param entityList
     */
    public MixMessage(List<MessageEntity> entityList){
        if(entityList ==null || entityList.size()<=1){
            throw new RuntimeException("MixMessage# type is error!");
        }

        MessageEntity justOne = entityList.get(0);
        id =  justOne.getId();
        msgId   = justOne.getMsgId();
        fromId  = justOne.getFromId();
        toId    = justOne.getToId();
        sessionKey = justOne.getSessionKey();
        msgType = justOne.getMsgType();
        status  = justOne.getStatus();
        created = justOne.getCreated();
        updated = justOne.getUpdated();
        msgList = entityList;
        displayType= DBConstant.SHOW_MIX_TEXT;

        /**分配主键Id
         * 图文混排的之间全部从-1开始
         * 在messageAdapter中 结合msgId进行更新
         *
         * dbinterface 结合id sessionKey msgid来替换具体的消息
         * {insertOrUpdateMix}
         * */
         long index = -1;
         for(MessageEntity msg:entityList){
             msg.setId(index);
             index --;
         }
    }

    /**
     * Not-null value.
     */
    @Override
    public String getContent() {
        return getSerializableContent(msgList);
    }

    /**
     *sessionKey是在外边设定的，所以子对象是没有的
     * 所以在设定的时候，都需要加上
     * */
    @Override
    public void setSessionKey(String sessionKey) {
        super.setSessionKey(sessionKey);
        for(MessageEntity msg:msgList){
            msg.setSessionKey(sessionKey);
        }
    }

    @Override
    public void setToId(long toId) {
        super.setToId(toId);
        for(MessageEntity msg:msgList){
            msg.setToId(toId);
        }
    }

    public MixMessage(MessageEntity dbEntity){
        id =  dbEntity.getId();
        msgId   = dbEntity.getMsgId();
        fromId  = dbEntity.getFromId();
        toId    = dbEntity.getToId();
        msgType = dbEntity.getMsgType();
        status  = dbEntity.getStatus();
        created = dbEntity.getCreated();
        updated = dbEntity.getUpdated();
        content = dbEntity.getContent();
        displayType = dbEntity.getDisplayType();
        sessionKey = dbEntity.getSessionKey();

    }

    private String getSerializableContent(List<MessageEntity> entityList){
        
        String json = JSON.toJSONString(entityList);
        return json;
    }

    public static MixMessage parseFromDB(MessageEntity entity) throws JSONException {
        if(entity.getDisplayType() != DBConstant.SHOW_MIX_TEXT){
            throw new RuntimeException("#MixMessage# parseFromDB,not SHOW_MIX_TEXT");
        }
        MixMessage mixMessage = new MixMessage(entity);
        List<MessageEntity> msgList = new ArrayList<>();
        JSONArray jsonArray = JSON.parseArray(entity.getContent());

        for (int i = 0, length = jsonArray.size(); i < length; i++) {
            JSONObject jsonOb = (JSONObject) jsonArray.getJSONObject(i);
            int displayType = jsonOb.getIntValue("displayType");
            String jsonMessage = jsonOb.toString();
            switch (displayType){
                case DBConstant.SHOW_ORIGIN_TEXT_TYPE:{
                    TextMessage textMessage = JSON.parseObject(jsonMessage,TextMessage.class);
                    textMessage.setSessionKey(entity.getSessionKey());
                    msgList.add(textMessage);
                }break;

                case DBConstant.SHOW_IMAGE_TYPE:
                    ImageMessage imageMessage = JSON.parseObject(jsonMessage,ImageMessage.class);
                    imageMessage.setSessionKey(entity.getSessionKey());
                    msgList.add(imageMessage);
                    break;
            }
        }
        mixMessage.setMsgList(msgList);
        return mixMessage;
    }


    public List<MessageEntity> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MessageEntity> msgList) {
        this.msgList = msgList;
    }
}
