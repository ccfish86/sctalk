package com.blt.talk.service.jpa.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The persistent class for the IMMessage_0 database table.
 * 
 */
@MappedSuperclass
public abstract class IMMessageEntity extends IMAbstractMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 根据接收者分表处理
     * 
     * @param toId
     * @return
     * @since 1.0
     */
    public static IMMessageEntity getInstance(long relateId) {
        return new IMMessage();
    }

    @Column(name = "relate_id", nullable = false)
    private Long relateId;

    @Column(name = "to_id", nullable = false)
    private Long toId;

    public Long getRelateId() {
        return this.relateId;
    }

    public Long getToId() {
        return this.toId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }
}
