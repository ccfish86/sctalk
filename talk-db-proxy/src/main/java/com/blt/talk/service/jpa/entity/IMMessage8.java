package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_8 database table.
 * 
 */
@Entity
@Table(name = "im_message_8")
@NamedQuery(name = "IMMessage8.findAll", query = "SELECT i FROM IMMessage8 i")
public class IMMessage8 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
