package com.mogujie.tt.imservice.entity;

import com.mogujie.tt.protobuf.helper.EntityChangeEngine;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 *
 * 未读session实体，并未保存在DB中
 */
public class UnreadEntity {
    private String sessionKey;
    private long peerId;
    private int sessionType;
    private int unReadCnt;
    private long laststMsgId;
    private int laststMsgType;
    private String latestMsgData;
    private long latestMsgFromUserId;
    private boolean isForbidden = false;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public int getUnReadCnt() {
        return unReadCnt;
    }

    public void setUnReadCnt(int unReadCnt) {
        this.unReadCnt = unReadCnt;
    }

    public long getLaststMsgId() {
        return laststMsgId;
    }

    public void setLaststMsgId(long laststMsgId) {
        this.laststMsgId = laststMsgId;
    }
    public int getLaststMsgType() {
        return laststMsgType;
    }


    public void setLaststMsgType(int laststMsgType) {
        this.laststMsgType = laststMsgType;
    }


    public long getLatestMsgFromUserId() {
        return latestMsgFromUserId;
    }
    public void setLatestMsgFromUserId(long latestMsgFromUserId) {
        this.latestMsgFromUserId = latestMsgFromUserId;
    }

    public String getLatestMsgData() {
        return latestMsgData;
    }

    public void setLatestMsgData(String latestMsgData) {
        this.latestMsgData = latestMsgData;
    }

    public boolean isForbidden() {
        return isForbidden;
    }

    public void setForbidden(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    @Override
    public String toString() {
        return "UnreadEntity{" +
                "sessionKey='" + sessionKey + '\'' +
                ", peerId=" + peerId +
                ", sessionType=" + sessionType +
                ", unReadCnt=" + unReadCnt +
                ", laststMsgId=" + laststMsgId +
                ", latestMsgData='" + latestMsgData + '\'' +
                ", isForbidden=" + isForbidden +
                '}';
    }

    public String buildSessionKey(){
        if(sessionType <=0 || peerId <=0){
            throw new IllegalArgumentException(
                    "SessionEntity buildSessionKey error,cause by some params <=0");
        }
        sessionKey = EntityChangeEngine.getSessionKey(peerId,sessionType);
        return sessionKey;
    }
}
