package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_0 database table.
 * 
 */
@Entity
@Table(name = "im_group_message")
@NamedQuery(name = "IMGroupMessage.findAll", query = "SELECT i FROM IMGroupMessage i")
public class IMGroupMessage extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
