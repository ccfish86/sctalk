package com.blt.talk.service.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the im_group_message_1 database table.
 * 
 */
@Entity
@Table(name = "im_group_message_1")
@NamedQuery(name = "IMGroupMessage1.findAll", query = "SELECT i FROM IMGroupMessage1 i")
public class IMGroupMessage1 extends IMGroupMessageEntity {
    private static final long serialVersionUID = 1L;

}
