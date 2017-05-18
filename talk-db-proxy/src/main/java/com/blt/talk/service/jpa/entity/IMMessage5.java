package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_5 database table.
 * 
 */
@Entity
@Table(name = "im_message_5")
@NamedQuery(name = "IMMessage5.findAll", query = "SELECT i FROM IMMessage5 i")
public class IMMessage5 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
