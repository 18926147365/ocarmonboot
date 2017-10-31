package com.ocarmon.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author 李浩铭
 * @since 2017年7月4日 上午11:39:43
 */
@Entity
@Table(name = "business")
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "native")
	@Column(name = "uid", nullable = false, length = 11)
	private Integer uid;
	@Column(name = "name")
	private String name;
	@Column(name = "avatar_url")
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

	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

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



	/**
	 * @return the avatar_Url
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatar_Url the avatar_Url to set
	 */
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


	public Business() {
	}

	@Override
	public String toString() {
		return "Business [uid=" + uid + ", name=" + name + ", avatarUrl=" + avatarUrl + ", introduction=" + introduction
				+ ", url=" + url + ", type=" + type + ", excerpt=" + excerpt + ", userId=" + userId + "]";
	}

	


}
