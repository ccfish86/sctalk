package com.blt.talk.common.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blt.talk.common.constant.DBConstant;
import com.blt.talk.common.constant.MessageConstant;
import com.blt.talk.common.util.SequenceNumberUtils;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 *
 */
public class AudioMessage extends MessageEntity implements Serializable{

    private String audioPath = "";
    private int audiolength =0 ;
    private int readStatus = MessageConstant.AUDIO_UNREAD;

    public AudioMessage(){
        msgId = SequenceNumberUtils.getInstance().makelocalUniqueMsgId();
    }

    private AudioMessage(MessageEntity entity){
        // 父类主键
        id =  entity.getId();
        msgId  = entity.getMsgId();
        fromId = entity.getFromId();
        toId   = entity.getToId();
        content=entity.getContent();
        msgType=entity.getMsgType();
        sessionKey = entity.getSessionKey();
        displayType=entity.getDisplayType();
        status = entity.getStatus();
        created = entity.getCreated();
        updated = entity.getUpdated();
    }


    public static AudioMessage parseFromDB(MessageEntity entity)  {
        if(entity.getDisplayType() != DBConstant.SHOW_AUDIO_TYPE){
           throw new RuntimeException("#AudioMessage# parseFromDB,not SHOW_AUDIO_TYPE");
        }
        AudioMessage audioMessage = new AudioMessage(entity);
        // 注意坑 啊
        String originContent = entity.getContent();

        JSONObject extraContent = null;
        try {
            extraContent = JSON.parseObject(originContent);
            audioMessage.setAudioPath(extraContent.getString("audioPath"));
            audioMessage.setAudiolength(extraContent.getIntValue("audiolength"));
            audioMessage.setReadStatus(extraContent.getIntValue("readStatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return audioMessage;
    }

//    public static AudioMessage buildForSend(float audioLen,String audioSavePath,UserEntity fromUser,PeerEntity peerEntity){
//        int tLen = (int) (audioLen + 0.5);
//        tLen = tLen < 1 ? 1 : tLen;
//        if (tLen < audioLen) {
//            ++tLen;
//        }
//
//        int nowTime = (int) (System.currentTimeMillis() / 1000);
//        AudioMessage audioMessage = new AudioMessage();
//        audioMessage.setFromId(fromUser.getPeerId());
//        audioMessage.setToId(peerEntity.getPeerId());
//        audioMessage.setCreated(nowTime);
//        audioMessage.setUpdated(nowTime);
//        int peerType = peerEntity.getType();
//        int msgType = peerType == DBConstant.SESSION_TYPE_GROUP ? DBConstant.MSG_TYPE_GROUP_AUDIO :
//                DBConstant.MSG_TYPE_SINGLE_AUDIO;
//        audioMessage.setMsgType(msgType);
//
//        audioMessage.setAudioPath(audioSavePath);
//        audioMessage.setAudiolength(tLen);
//        audioMessage.setReadStatus(MessageConstant.AUDIO_READED);
//        audioMessage.setDisplayType(DBConstant.SHOW_AUDIO_TYPE);
//        audioMessage.setStatus(MessageConstant.MSG_SENDING);
//        audioMessage.buildSessionKey(true);
//        return audioMessage;
//    }


    /**
     * Not-null value.
     * DB 存数解析的时候需要
     */
    @Override
    public String getContent() {
        JSONObject extraContent = new JSONObject();
        try {
            extraContent.put("audioPath",audioPath);
            extraContent.put("audiolength",audiolength);
            extraContent.put("readStatus",readStatus);
            String audioContent = extraContent.toString();
            return audioContent;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


//    @Override
//    public byte[] getSendContent() {
//        byte[] result = new byte[4];
//        result = CommonUtil.intToBytes(audiolength);
//        if (TextUtils.isEmpty(audioPath)) {
//            return result;
//        }
//
//        byte[] bytes = FileUtil.getFileContent(audioPath);
//        if (bytes == null) {
//            return bytes;
//        }
//        int contentLength = bytes.length;
//        byte[] byteAduioContent = new byte[4 + contentLength];
//        System.arraycopy(result, 0, byteAduioContent, 0, 4);
//        System.arraycopy(bytes, 0, byteAduioContent, 4, contentLength);
//        return byteAduioContent;
//    }


    /***-------------------------------set/get----------------------------------*/
    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getAudiolength() {
        return audiolength;
    }

    public void setAudiolength(int audiolength) {
        this.audiolength = audiolength;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }


}
