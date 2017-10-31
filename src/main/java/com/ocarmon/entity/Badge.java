package com.ocarmon.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author 李浩铭
 * @since 2017年7月3日 上午11:41:31
 */
@Entity
@Table(name = "badge")
public class Badge {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "native")
	@Column(name = "uid", nullable = false, length = 11)
	private Integer uid;
	@Column(name = "type")
	private String type;
	@Column(name = "description")
	private String description;

	@Column(name="user_id")
	private String userId;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id",insertable=false,updatable=false)
	private User users;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Badge [uid=" + uid + ", type=" + type + ", description=" + description + ", userId=" + userId
				+ ", users=" + users + "]";
	}



}
