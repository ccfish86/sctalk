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
        Long splt = relateId % 10;
        IMMessageEntity entity;
        switch (splt.intValue()) {
            case 0:
                entity = new IMMessage0();
                break;
            case 1:
                entity = new IMMessage1();
                break;
            case 2:
                entity = new IMMessage2();
                break;
            case 3:
                entity = new IMMessage3();
                break;
            case 4:
                entity = new IMMessage4();
                break;
            case 5:
                entity = new IMMessage5();
                break;
            case 6:
                entity = new IMMessage6();
                break;
            case 7:
                entity = new IMMessage7();
                break;
            case 8:
                entity = new IMMessage8();
                break;
            case 9:
                entity = new IMMessage9();
                break;
            default:
                entity = null;
                break;
        }
        return entity;
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
