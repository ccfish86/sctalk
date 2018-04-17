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
        return new IMGroupMessage();
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
