package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_0 database table.
 * 
 */
@Entity
@Table(name = "im_message")
@NamedQuery(name = "IMMessage.findAll", query = "SELECT i FROM IMMessage i")
public class IMMessage extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
