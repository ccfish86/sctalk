/*
 * Copyright © 2013-2016 BLT, Co., Ltd. All Rights Reserved.
 */

package com.blt.talk.common.code;

import com.google.protobuf.MessageLite;

/**
 * TCP协议的内容（HEADER+BODY）
 * 
 * @param <T> the type of elements in this body
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class IMProtoMessage<T extends MessageLite> {

    /** the header of the message */
    private IMHeader header;

    /** the body of the message */
    private T body;
    
    /**
     * Constructs an empty message.
     */
    public IMProtoMessage() {
    }
    
    /**
     * Constructs an message with header and body
     * @param header the header of the message
     * @param body the body of the message
     */
    public IMProtoMessage(final IMHeader header, final T body) {
        this.header = header;
        this.body = body;
    }

    /**
     * Get the header
     * @return the header
     */
    public IMHeader getHeader() {
        return header;
    }

    /**
     * Set the header
     * @param header the header to set
     */
    public void setHeader(IMHeader header) {
        this.header = header;
    }

    /**
     * Get the body
     * @return the body
     */
    public T getBody() {
        return body;
    }

    /**
     * Set the body
     * @param body the body to set
     */
    public void setBody(T body) {
        this.body = body;
    }
    
}
