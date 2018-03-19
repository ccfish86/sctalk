package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_0 database table.
 * 
 */
@Entity
@Table(name = "im_message_0")
@NamedQuery(name = "IMMessage0.findAll", query = "SELECT i FROM IMMessage0 i")
public class IMMessage0 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
