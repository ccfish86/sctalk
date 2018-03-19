package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_7 database table.
 * 
 */
@Entity
@Table(name = "im_message_7")
@NamedQuery(name = "IMMessage7.findAll", query = "SELECT i FROM IMMessage7 i")
public class IMMessage7 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
