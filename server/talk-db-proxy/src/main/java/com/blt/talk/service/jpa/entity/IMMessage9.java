package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_9 database table.
 * 
 */
@Entity
@Table(name = "im_message_9")
@NamedQuery(name = "IMMessage9.findAll", query = "SELECT i FROM IMMessage9 i")
public class IMMessage9 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
