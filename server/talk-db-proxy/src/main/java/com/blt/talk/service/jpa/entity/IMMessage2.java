package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_2 database table.
 * 
 */
@Entity
@Table(name = "im_message_2")
@NamedQuery(name = "IMMessage2.findAll", query = "SELECT i FROM IMMessage2 i")
public class IMMessage2 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
