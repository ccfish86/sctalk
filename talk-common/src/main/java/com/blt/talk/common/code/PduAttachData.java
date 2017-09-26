/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.code;

import com.google.protobuf.ByteString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 用户存放服务器端待处理消息
 * 
 * @author 袁贵
 * @version 1.1
 * @since  1.1
 */
public class PduAttachData {

    private ByteBuf buffer = ByteBufUtil.threadLocalDirectBuffer();
    private int type;
    private Long handle;
    private Integer serviceType;
    private Integer pduLength; 
    private ByteString pdu; 
    
    public PduAttachData(int attachType, Long handle, Integer serviceType) {

        this.type = attachType;
        this.handle = handle;
        this.serviceType = serviceType;
        this.pduLength = 0;
        
        buffer.writeInt(attachType);
        buffer.writeLong(handle);
        buffer.writeInt(serviceType);
        buffer.writeInt(0);
    }

    public PduAttachData(int attachType, Long handle, Integer serviceType, ByteString pdu) {
        
        this.type = attachType;
        this.handle = handle;
        this.serviceType = serviceType;
        this.pdu = pdu;
        this.pduLength = pdu.size();
        
        buffer.writeInt(attachType);
        buffer.writeLong(handle);
        buffer.writeInt(serviceType);
        buffer.writeInt(pdu.size());
        buffer.writeBytes(pdu.toByteArray());
    }
    
    public byte[] getBufferData() {
        if (buffer.hasArray()) {
            return buffer.array();
        } else {
            buffer.resetReaderIndex();
            byte[] content = new byte[buffer.writerIndex()];
            buffer.readBytes(content);
            return content;
        }
    }
    
    public PduAttachData(ByteString bytes) {
        
        buffer.writeBytes(bytes.toByteArray());
        
        this.type = buffer.readInt();
        this.handle = buffer.readLong();
        this.serviceType = buffer.readInt();
        this.pduLength = buffer.readInt();
        ByteBuf buf = buffer.readBytes(this.pduLength);
        
        if (buf.hasArray()) {
            this.pdu = ByteString.copyFrom(buf.array());
        } else {
            byte[] content = new byte[buf.capacity()];
            buf.readBytes(content);
            this.pdu = ByteString.copyFrom(content);
        }
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the handle
     */
    public Long getHandle() {
        return handle;
    }

    /**
     * @return the serviceType
     */
    public Integer getServiceType() {
        return serviceType;
    }

    /**
     * @return the pduLength
     */
    public Integer getPduLength() {
        return pduLength;
    }

    /**
     * @return the pdu
     */
    public ByteString getPdu() {
        return pdu;
    }
    
}
