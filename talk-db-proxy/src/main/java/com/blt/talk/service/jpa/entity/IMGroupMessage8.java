package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_8 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_8")
@NamedQuery(name = "IMGroupMessage8.findAll", query = "SELECT i FROM IMGroupMessage8 i")
public class IMGroupMessage8 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
