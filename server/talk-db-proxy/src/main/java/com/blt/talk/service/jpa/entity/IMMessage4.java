package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_4 database table.
 * 
 */
@Entity
@Table(name = "im_message_4")
@NamedQuery(name = "IMMessage4.findAll", query = "SELECT i FROM IMMessage4 i")
public class IMMessage4 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
