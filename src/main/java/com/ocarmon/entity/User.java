package com.ocarmon.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 知乎用户资料
 */
@Table(name = "user")
@Entity
public class User {
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;
	@Column(name = "name")
	private String name;
	@Column(name = "headline")
	private String headline;

	@Column(name = "type")
	private String type;
	@Column(name = "url")
	private String url;
	@Column(name = "answer_Count")
	private Integer answerCount;
	@Column(name = "articles_Count")
	private Integer articlesCount;
	@Column(name = "avatar_Url")
	private String avatarUrl;
	@Column(name = "gender")
	private Integer gender;
	@Column(name = "avatar_Url_Template")
	private String avatarUrlTemplate;


	@Column(name = "followerCount")
	private Integer followerCount;
	@Column(name = "urlToken")
	private String urlToken;

	@Column(name = "create_time")
	private Date createTime;
	
	@OneToMany
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private List<Badge> badgeList = new ArrayList<Badge>();

	@OneToMany
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private List<Locations> locationsList = new ArrayList<Locations>();
	
	
	
	
	@OneToMany
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private List<Educations> educationsList = new ArrayList<Educations>();
	
	
	
	@OneToMany
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private List<Business> myBusiness;

	@Transient
	private Map<String, Object> business;
	@Transient
	private List<Map<String, Object>> educations;
	@Transient
	private List<Map<String, Object>> locations;
	@Transient
	private List<Map<String, Object>> badge;
	

	public List<Badge> getBadgeList() {
		return badgeList;
	}

	public void setBadgeList(List<Badge> badgeList) {
		this.badgeList = badgeList;
	}

	public List<Map<String, Object>> getBadge() {
		return badge;
	}

	public void setBadge(List<Map<String, Object>> badge) {
		List<Badge> result=new ArrayList<Badge>();
		for (Map<String, Object> map : badge) {
			Badge badge2=new Badge();
			try {
				BeanUtils.populate(badge2, map);
				result.add(badge2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setBadgeList(result);
		
		
		this.badge = badge;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<Locations> getLocationsList() {
		return locationsList;
	}

	public void setLocationsList(List<Locations> locationsList) {
		this.locationsList = locationsList;
	}

	public List<Map<String, Object>> getLocations() {
		return locations;
	}

	public void setLocations(List<Map<String, Object>> locations) {
		List<Locations> result=new ArrayList<Locations>();
		for (Map<String, Object> map : locations) {
			Locations location=new Locations();
			try {
				BeanUtils.populate(location, map);
				result.add(location);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setLocationsList(result);
		
		
		this.locations = locations;
	}

	public List<Educations> getEducationsList() {
		return educationsList;
	}

	public void setEducationsList(List<Educations> educationsList) {
		this.educationsList = educationsList;
	}

	public List<Map<String, Object>> getEducations() {
		return educations;
	}

	public void setEducations(List<Map<String, Object>> educations) {
		List<Educations> list=new ArrayList<Educations>();
		for (Map<String, Object> map : educations) {
			School school=new School();
			Major major=new Major();
			try {
				Educations education=new Educations();
				if((Map<String, Object>)map.get("school")!=null){
					BeanUtils.populate(school, (Map<String, Object>)map.get("school"));
				}
				if((Map<String, Object>)map.get("major")!=null){
					BeanUtils.populate(major, (Map<String, Object>)map.get("major"));
				}
				ArrayList<School> schoolTemp=new ArrayList<School>();
				ArrayList<Major> majorTemp=new ArrayList<Major>();
				schoolTemp.add(school);
				majorTemp.add(major);
				education.setSchool(schoolTemp);
				education.setMajor(majorTemp);
				list.add(education);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		setEducationsList(list);
		this.educations = educations;
	}


	public List<Business> getMyBusiness() {
		return myBusiness;
	}

	public void setMyBusiness(List<Business> myBusiness) {
		this.myBusiness = myBusiness;
	}



	public void setBusiness(Map<String, Object> business) {
		try {
			Business temp = new Business();
			BeanUtils.populate(temp, business);
			List<Business> list=new ArrayList<Business>();
			list.add(temp);
			setMyBusiness(list);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		this.business = business;
	}


	

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	public Integer getArticlesCount() {
		return articlesCount;
	}

	public void setArticlesCount(Integer articlesCount) {
		this.articlesCount = articlesCount;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		if(avatarUrl.indexOf("_is.jpg")!=-1){
			avatarUrl=avatarUrl.replace("_is.jpg", "_xl.jpg");
		}
		this.avatarUrl = avatarUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatarUrlTemplate() {
		return avatarUrlTemplate;
	}

	public void setAvatarUrlTemplate(String avatarUrlTemplate) {
		this.avatarUrlTemplate = avatarUrlTemplate;
	}


	public Integer getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Integer followerCount) {
		this.followerCount = followerCount;
	}

	public String getUrlToken() {
		return urlToken;
	}

	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}


	

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", headline=" + headline + ", type=" + type + ", url=" + url
				+ ", answerCount=" + answerCount + ", articlesCount=" + articlesCount + ", avatarUrl=" + avatarUrl
				+ ", gender=" + gender + ", avatarUrlTemplate=" + avatarUrlTemplate + ", followerCount=" + followerCount
				+ ", urlToken=" + urlToken + ", badgeList=" + badgeList + ", locationsList=" + locationsList
				+ ", educationsList=" + educationsList + ", myBusiness=" + myBusiness + ", business=" + business
				+ ", educations=" + educations + ", locations=" + locations + ", badge=" + badge + "]";
	}

	public User() {
	}

}
