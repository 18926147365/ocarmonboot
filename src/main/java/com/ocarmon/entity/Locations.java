package com.ocarmon.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
* @author 李浩铭 
* @since 2017年7月4日 上午11:40:50
*/
@Entity
@Table(name="locations")
public class Locations {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "native")
	@Column(name = "uid", nullable = false, length = 11)
	private Integer uid;
	@Column(name = "name")
	private String name;
	@Column(name = "avatar_Url")
	private String avatarUrl;
	@Column(name = "introduction")
	private String introduction;
	@Column(name = "url")
	private String url;
	@Column(name = "type")
	private String type;
	@Column(name = "excerpt")
	private String excerpt;
	@Column(name="user_id")
	private String userId;
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id",insertable=false,updatable=false)
	private User users;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
