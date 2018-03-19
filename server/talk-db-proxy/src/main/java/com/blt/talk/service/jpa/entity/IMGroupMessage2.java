package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_2 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_2")
@NamedQuery(name = "IMGroupMessage2.findAll", query = "SELECT i FROM IMGroupMessage2 i")
public class IMGroupMessage2 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
