package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_7 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_7")
@NamedQuery(name = "IMGroupMessage7.findAll", query = "SELECT i FROM IMGroupMessage7 i")
public class IMGroupMessage7 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
