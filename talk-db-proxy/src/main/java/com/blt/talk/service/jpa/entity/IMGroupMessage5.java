package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_5 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_5")
@NamedQuery(name = "IMGroupMessage5.findAll", query = "SELECT i FROM IMGroupMessage5 i")
public class IMGroupMessage5 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
