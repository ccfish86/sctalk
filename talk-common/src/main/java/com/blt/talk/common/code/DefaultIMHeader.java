package com.blt.talk.common.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.constant.SysConstant;

public class DefaultIMHeader extends IMHeader {
    
    private Logger logger = LoggerFactory.getLogger(DefaultIMHeader.class);

    public DefaultIMHeader(int serviceId, int commandId) {
        setVersion((short) SysConstant.PROTOCOL_VERSION);
        setFlag((short) SysConstant.PROTOCOL_FLAG);
        setServiceId((short)serviceId);
        setCommandId((short)commandId);
        short seqNo = (short)0; //?? FIXME
        setSeqnum(seqNo);
        setReserved((short)SysConstant.PROTOCOL_RESERVED);

        logger.trace("packet#construct Default Header -> serviceId:{}, commandId:{}, seqNo:{}", serviceId, commandId, seqNo);
    }
}