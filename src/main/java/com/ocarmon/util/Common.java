package com.ocarmon.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ocarmon.entity.Proxy;
import com.ocarmon.entity.User;

/**
 * @author 李浩铭
 * @since 2017年7月6日 上午9:33:36
 */
public class Common {
	// 存储用户TOKEN的队列
	public static Queue<String> QUEUETOKEN = new LinkedList<String>();
	public static Queue<User> DATAUSER=new LinkedList<User>();
	public static Integer DATACOUNT=0;
	//存储用户TOKEN的队列
	public static Queue<String> QUEUETOKENUSER=new LinkedList<String>();
	public static Integer soryInt=0;
	public  static Integer isNotSuccssCount=0;
	//代理IP
	public static Queue<Proxy> QUEUEPROXY=new LinkedList<Proxy>();
	public static int noSuccess=0;
	public static Integer SUCCESSCOUNT=0;
	
}
