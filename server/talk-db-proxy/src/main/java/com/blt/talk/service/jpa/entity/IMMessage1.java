package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_message_1 database table.
 * 
 */
@Entity
@Table(name = "im_message_1")
@NamedQuery(name = "IMMessage1.findAll", query = "SELECT i FROM IMMessage1 i")
public class IMMessage1 extends IMMessageEntity {
    private static final long serialVersionUID = 1L;

}
