package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_3 database table.
 * 
 */
@Entity
@Table(name = "im_message_3")
@NamedQuery(name = "IMMessage3.findAll", query = "SELECT i FROM IMMessage3 i")
public class IMMessage3 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
