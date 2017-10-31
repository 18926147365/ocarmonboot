package com.ocarmon.entity;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/** 
* @author 李浩铭 
* @since 2017年7月6日 下午4:28:26
*/
public class Proxy {

	private String host;
	
	private Integer port;
	
	private String location;
	
	private Double speedTime;
	
	private Double connectTime;
	
	private Integer survivalTime;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getSpeedTime() {
		return speedTime;
	}

	public void setSpeedTime(Double speedTime) {
		this.speedTime = speedTime;
	}

	public Double getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(Double connectTime) {
		this.connectTime = connectTime;
	}

	public Proxy(String host, Integer port, String location, Double speedTime, Double connectTime,
			Integer survivalTime) {
		super();
		this.host = host;
		this.port = port;
		this.location = location;
		this.speedTime = speedTime;
		this.connectTime = connectTime;
		this.survivalTime = survivalTime;
	}


	public Proxy(){}
	public Proxy(String host, Integer port){
		super();
		this.host = host;
		this.port = port;
	}


	
	
}
