package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_4 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_4")
@NamedQuery(name = "IMGroupMessage4.findAll", query = "SELECT i FROM IMGroupMessage4 i")
public class IMGroupMessage4 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
