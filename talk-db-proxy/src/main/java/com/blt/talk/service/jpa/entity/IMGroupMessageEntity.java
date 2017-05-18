package com.blt.talk.service.jpa.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The persistent class for the IMGroupMessage_0 database table.
 * 
 */
@MappedSuperclass
public abstract class IMGroupMessageEntity extends IMAbstractMessage {

    private static final long serialVersionUID = 1L;


    /**
     * 根据群组ID分表
     * 
     * @param groupId
     * @return
     * @since 1.0
     */
    public static IMGroupMessageEntity getInstance(long groupId) {

        Long splt = groupId % 10;
        IMGroupMessageEntity entity;
        switch (splt.intValue()) {
            case 0:
                entity = new IMGroupMessage0();
                break;
            case 1:
                entity = new IMGroupMessage1();
                break;
            case 2:
                entity = new IMGroupMessage2();
                break;
            case 3:
                entity = new IMGroupMessage3();
                break;
            case 4:
                entity = new IMGroupMessage4();
                break;
            case 5:
                entity = new IMGroupMessage5();
                break;
            case 6:
                entity = new IMGroupMessage6();
                break;
            case 7:
                entity = new IMGroupMessage7();
                break;
            case 8:
                entity = new IMGroupMessage8();
                break;
            case 9:
                entity = new IMGroupMessage9();
                break;
            default:
                entity = null;
                break;
        }
        return entity;
    }

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
