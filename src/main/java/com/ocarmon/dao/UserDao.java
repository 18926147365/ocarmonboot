package com.ocarmon.dao;

import java.util.List;

import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ocarmon.entity.User;

/** 
* @author 李浩铭 
* @since 2017年9月4日 上午10:47:01
*/
@Repository  
@Table(name="user")  
@Qualifier("userDao")
public interface UserDao extends CrudRepository<User, Integer>{
	
	
	User findByUrlToken(String urlToken);
	
	
}

