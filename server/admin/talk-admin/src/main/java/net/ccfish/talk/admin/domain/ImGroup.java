package net.ccfish.talk.admin.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the im_group database table.
 * 
 */
@Entity
@Table(name="im_group")
@NamedQuery(name="ImGroup.findAll", query="SELECT i FROM ImGroup i")
public class ImGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(nullable=false, length=256)
	private String avatar;

	@Column(nullable=false)
	private int created;

	@Column(nullable=false)
	private Long creator;

	@Column(name="last_chated", nullable=false)
	private int lastChated;

	@Column(nullable=false, length=256)
	private String name;

	@Column(nullable=false)
	private byte status;

	@Column(nullable=false)
	private byte type;

	@Column(nullable=false)
	private int updated;

	@Column(name="user_cnt", nullable=false)
	private int userCnt;

	@Column(nullable=false)
	private int version;

	public ImGroup() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getCreated() {
		return this.created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public Long getCreator() {
		return this.creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public int getLastChated() {
		return this.lastChated;
	}

	public void setLastChated(int lastChated) {
		this.lastChated = lastChated;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getUpdated() {
		return this.updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getUserCnt() {
		return this.userCnt;
	}

	public void setUserCnt(int userCnt) {
		this.userCnt = userCnt;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}