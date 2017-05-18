package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_0 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_0")
@NamedQuery(name = "IMGroupMessage0.findAll", query = "SELECT i FROM IMGroupMessage0 i")
public class IMGroupMessage0 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
