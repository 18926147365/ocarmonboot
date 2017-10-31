package com.ocarmon.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * @author 李浩铭
 * @since 2017年6月19日 下午3:17:56 将实体类属性转化为常用的sql语句
 */
public class BeanSqlUtil {
	/**
	 * 根据实体类属性转化为插入sql语句
	 * 
	 * @param 实体类
	 * @throws 表名
	 */
	public static String insertSql(Object bean, String tableName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = transBean2Map(bean);
		return insertSql(map, tableName);
	}
	

	


	/**
	 * 根据实体类属性转化为插入sql语句
	 * 
	 * @param map
	 * @throws 表名
	 */
	public static String insertSql(Map<String, Object> map, String tableName) {
		Set<String> set = map.keySet();
		StringBuffer sBuffer = new StringBuffer("INSERT INTO " + tableName + "(");
		StringBuffer valBuffer = new StringBuffer(" VALUES(");
		for (String key : set) {
			Object val = map.get(key);
			sBuffer.append(key + ",");
			if (val instanceof Integer || val instanceof Long || val instanceof Double || val instanceof Float
					|| val instanceof Short) {
				valBuffer.append(val + ",");
			} else {
				if (val == null) {
					// TODO 还有一些数据类型没有考虑到
					valBuffer.append(val + ",");
				} else {
					valBuffer.append("'" + val + "',");
				}
			}
		}
		sBuffer = sBuffer.delete(sBuffer.length() - 1, sBuffer.length());
		valBuffer = valBuffer.delete(valBuffer.length() - 1, valBuffer.length());
		sBuffer.append(")");
		valBuffer.append(")");
		return sBuffer.toString() + valBuffer.toString();
	}
	
	/**
	 * 修改的sql语句为了防止未加修改条件而造成修改全部数据的情况，sql语句后加上了where 1=
	 * @param map
	 * @param 表名
	 */
	public static String updateSql(Map<String, Object> map, String tableName) {
		Set<String> set = map.keySet();
		StringBuffer sBuffer = new StringBuffer("UPDATE  " + tableName + " SET ");
		for (String key : set) {
			Object val = map.get(key);

			if (val instanceof Integer || val instanceof Long || val instanceof Double || val instanceof Float
					|| val instanceof Short) {
				sBuffer.append(key + "=" + val + ",");
			} else {
				if (val == null) {
					// TODO 还有一些数据类型没有考虑到
					sBuffer.append(key + "=" + val + ",");
				} else {
					sBuffer.append(key + "='" + val + "',");
				}
			}
		}
		sBuffer = sBuffer.delete(sBuffer.length() - 1, sBuffer.length());
		sBuffer.append(" where 1=");
		return sBuffer.toString();
	}
	/**
	 * 修改的sql语句，记得加where条件，不然全部修改
	 * @param 实体类
	 * @param 表名
	 */
	public static String updateSql(Object bean, String tableName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = transBean2Map(bean);
		return updateSql(map, tableName);
		
	}

	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	private static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}


}
