package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_9 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_9")
@NamedQuery(name = "IMGroupMessage9.findAll", query = "SELECT i FROM IMGroupMessage9 i")
public class IMGroupMessage9 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
