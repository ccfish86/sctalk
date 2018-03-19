package com.blt.talk.common.model;

import com.blt.talk.common.code.proto.helper.EntityChangeEngine;
import com.blt.talk.common.constant.DBConstant;
/**
 * 这个类不同与其他自动生成代码
 * 需要依赖conten与display 依赖不同的状态
 * */
// KEEP INCLUDES END
/**
 * Entity mapped to table Message.
 */
public class MessageEntity implements java.io.Serializable {

    protected Long id;
    protected long msgId;
    protected long fromId;
    protected long toId;
    /** Not-null value. */
    protected String sessionKey;
    /** Not-null value. */
    protected String content;
    protected int msgType;
    protected int displayType;
    protected int status;
    protected int created;
    protected int updated;

    // KEEP FIELDS - put your custom fields here

    protected boolean isGIfEmo;
    // KEEP FIELDS END

    public MessageEntity() {
    }

    public MessageEntity(Long id) {
        this.id = id;
    }

    public MessageEntity(Long id, int msgId, int fromId, int toId, String sessionKey, String content, int msgType, int displayType, int status, int created, int updated) {
        this.id = id;
        this.msgId = msgId;
        this.fromId = fromId;
        this.toId = toId;
        this.sessionKey = sessionKey;
        this.content = content;
        this.msgType = msgType;
        this.displayType = displayType;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    /** Not-null value. */
    public String getSessionKey() {
        return sessionKey;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /** Not-null value. */
    public String getContent() {
        return content;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    // KEEP METHODS - put your custom methods here
    /**
     * -----根据自身状态判断的---------
     */
    public int getSessionType() {
        switch (msgType) {
            case  DBConstant.MSG_TYPE_SINGLE_TEXT:
            case  DBConstant.MSG_TYPE_SINGLE_AUDIO:
                return DBConstant.SESSION_TYPE_SINGLE;
            case DBConstant.MSG_TYPE_GROUP_TEXT:
            case DBConstant.MSG_TYPE_GROUP_AUDIO:
                return DBConstant.SESSION_TYPE_GROUP;
            default:
                //todo 有问题
                return DBConstant.SESSION_TYPE_SINGLE;
        }
    }


    public String getMessageDisplay() {
        switch (displayType){
            case DBConstant.SHOW_AUDIO_TYPE:
                return DBConstant.DISPLAY_FOR_AUDIO;
            case DBConstant.SHOW_ORIGIN_TEXT_TYPE:
                return content;
            case DBConstant.SHOW_IMAGE_TYPE:
                return DBConstant.DISPLAY_FOR_IMAGE;
            case DBConstant.SHOW_MIX_TEXT:
                return DBConstant.DISPLAY_FOR_MIX;
            default:
                return DBConstant.DISPLAY_FOR_ERROR;
        }
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", msgId=" + msgId +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", msgType=" + msgType +
                ", displayType=" + displayType +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity)) return false;

        MessageEntity that = (MessageEntity) o;

        if (created != that.created) return false;
        if (displayType != that.displayType) return false;
        if (fromId != that.fromId) return false;
        if (msgId != that.msgId) return false;
        if (msgType != that.msgType) return false;
        if (status != that.status) return false;
        if (toId != that.toId) return false;
        if (updated != that.updated) return false;
        if (!content.equals(that.content)) return false;
        if (!id.equals(that.id)) return false;
        if (!sessionKey.equals(that.sessionKey)) return false;

        return true;
    }

    /**
     * 获取会话的sessionId
     * @param isSend
     * @return
     */
    public long getPeerId(boolean isSend){
        if(isSend){
            /**自己发出去的*/
            return toId;
        }else{
            /**接受到的*/
            switch (getSessionType()){
                case DBConstant.SESSION_TYPE_SINGLE:
                    return fromId;
                case DBConstant.SESSION_TYPE_GROUP:
                    return toId;
                default:
                    return toId;
            }
        }
    }

    public byte[] getSendContent(){
        return null;
    }

    public boolean isGIfEmo() {
        return isGIfEmo;
    }

    public void setGIfEmo(boolean isGIfEmo) {
        this.isGIfEmo = isGIfEmo;
    }

    public boolean isSend(int loginId){
        boolean isSend = (loginId==fromId)?true:false;
        return isSend;
    }

    public String buildSessionKey(boolean isSend){
        int sessionType = getSessionType();
        long peerId = getPeerId(isSend);
        sessionKey = EntityChangeEngine.getSessionKey(peerId,sessionType);
        return sessionKey;
    }
    // KEEP METHODS END

}
