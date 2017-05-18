package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_6 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_6")
@NamedQuery(name = "IMGroupMessage6.findAll", query = "SELECT i FROM IMGroupMessage6 i")
public class IMGroupMessage6 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
