package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_6 database table.
 * 
 */
@Entity
@Table(name = "im_message_6")
@NamedQuery(name = "IMMessage6.findAll", query = "SELECT i FROM IMMessage6 i")
public class IMMessage6 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
