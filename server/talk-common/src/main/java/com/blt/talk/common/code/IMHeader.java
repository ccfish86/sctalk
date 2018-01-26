package com.blt.talk.common.code;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blt.talk.common.constant.SysConstant;

/**
 * TCP协议的头文件
 *
 * @author 袁贵
 * @time 2017/05/04
 */
public class IMHeader implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8872067650988868285L;

    private Logger logger = LoggerFactory.getLogger(IMHeader.class);

    /** 数据包长度，包括包头 */
    private int length; 

    /** 版本号 */
    private short version;

    /** flag */
    private short flag;

    /** 服务ID */
    private short serviceId; // SID

    /** 命令ID */
    private short commandId; // CID

    /** 序列ID */
    private short seqnum;

    private short reserved; // 保留，可用于如序列号等

    public IMHeader() {
        this.length = 0;
        this.version = SysConstant.PROTOCOL_VERSION;
        this.serviceId = 0;
        this.commandId = 0;
        this.flag = SysConstant.PROTOCOL_FLAG;
        this.seqnum = 0;
        this.reserved = SysConstant.PROTOCOL_RESERVED;
    }

    /**
     * Get then flag
     * @return the flag
     * @since  1.0
     */
    public short getFlag() {
        return flag;
    }

    /**
     * Set then flag
     * @param flag the flag
     * @since  1.0
     */
    public void setFlag(short flag) {
        this.flag = flag;
    }

    /**
     * Get the sequence number
     * @return then sequence number
     * @since  1.0
     */
    public short getSeqnum() {
        return seqnum;
    }

    /**
     * Set the sequence number
     * @param seq the sequence number
     * @since  1.0
     */
    public void setSeqnum(short seq) {
        this.seqnum = seq;
    }

    /**
     * 头文件的压包函数
     *
     * @return 数据包
     */
    public DataBuffer encode() {
        DataBuffer db = new DataBuffer(SysConstant.PROTOCOL_HEADER_LENGTH);
        db.writeInt(length);
        db.writeShort(version);
        db.writeShort(flag);
        db.writeShort(serviceId);
        db.writeShort(commandId);
        db.writeShort(seqnum);
        db.writeShort(reserved);
        return db;

    }


    /**
     * 头文件的解包函数
     *
     * @param buffer the buffer
     */
    public void decode(DataBuffer buffer) {

        if (null == buffer)
            return;
        try {
            length = buffer.readInt();
            version = buffer.readShort();
            flag = buffer.readShort();
            serviceId = buffer.readShort();
            commandId = buffer.readShort();
            seqnum = buffer.readShort();
            reserved = buffer.readShort();
            logger.trace(
                    "Decode header, length:{}, version:{}, flag:{} serviceId:{}, commandId:{}, reserved:{},seq:{}",
                    length, version, flag, serviceId, commandId,
                    seqnum, reserved);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String toString() {

        return "Header [length=" + length + ", version=" + version + ", flag="
                + flag + ", serviceId=" + serviceId + ", commandId="
                + commandId + ", seq=" + seqnum + ", reserved=" + reserved
                + "]";
    }

    /**
     * Get then command id
     * @return then command id
     * @since  1.0
     */
    public short getCommandId() {
        return commandId;
    }

    /**
     * Set then command id
     * @param commandID the command id
     * @since  1.0
     */
    public void setCommandId(short commandID) {
        this.commandId = commandID;
    }

    /**
     * Get the service id
     * @return the service id
     * @since  1.0
     */
    public short getServiceId() {
        return serviceId;
    }

    /**
     * Set the service id
     * @param serviceID the service id
     * @since  1.0
     */
    public void setServiceId(short serviceID) {
        this.serviceId = serviceID;
    }

    /**
     * Get then length
     * @return the length
     * @since  1.0
     */
    public int getLength() {
        return length;
    }

    /**
     * Set the length
     * @param length the length
     * @since  1.0
     */
    public void setLength(int length) {
        this.length = length;
    }
    /**
     * Get then version
     * @return the version
     * @since  1.0
     */
    public short getVersion() {
        return version;
    }

    /**
     * Set the version
     * @param version the version
     * @since  1.0
     */
    public void setVersion(short version) {
        this.version = version;
    }

    /**
     * Get the reserved
     * @return the reserved
     * @since  1.0
     */
    public short getReserved() {
        return reserved;
    }

    /**
     * Set the reserved
     * @param reserved the reserved
     * @since  1.0
     */
    public void setReserved(short reserved) {
        this.reserved = reserved;
    }
    
    @Override
    public IMHeader clone() {
        IMHeader anobj = new IMHeader();
        anobj.length = this.length;
        anobj.version = this.version;
        anobj.flag = this.flag;
        anobj.serviceId = this.serviceId;
        anobj.commandId = this.commandId;
        anobj.seqnum = this.seqnum;
        anobj.reserved = this.reserved;
        return anobj;
    }
}
