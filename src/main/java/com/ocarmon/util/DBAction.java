package com.ocarmon.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/** 
* @author 李浩铭 
* @since 2017年3月3日 上午10:02:36
* 连接数据库
*/
public class DBAction {
	public static Connection getConnection(){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/zhizhu?Unicode=true", "root", "123456");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeAll(Connection conn,Statement stat,ResultSet rs){
		try {
			if(rs!=null){
				rs.close();
			}
			if(stat!=null){
				stat.close();
			}
			if(conn!=null){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
