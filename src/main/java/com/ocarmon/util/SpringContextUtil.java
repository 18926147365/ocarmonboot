/**
 * 
 */
package com.ocarmon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ocarmon.dao.UserDao;
import com.ocarmon.entity.User;

/** 
* @author 李浩铭 
* @since 2017年9月20日 下午4:10:35
*/
@Component
public class SpringContextUtil implements ApplicationContextAware {

         private static ApplicationContext applicationContext; // Spring应用上下文环境

      

         public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
               SpringContextUtil.applicationContext = applicationContext;
         }

 

         public static ApplicationContext getApplicationContext() {
                return applicationContext;
         }


          @SuppressWarnings("unchecked")
          public static <T> T getBean(String name) throws BeansException {
                     return (T) applicationContext.getBean(name);
           }
          
      	public static Boolean isExistsUser(String token){
    		UserDao userDao=getBean("userDao");
    		User user= userDao.findByUrlToken(token);
    		if(user!=null) {
    			return true;
    		}
    		return false;
    	}
      	public static void insetInfo(User user){
      		UserDao userDao=getBean("userDao");
      		User sUser=userDao.save(user);
      	
      	}

}