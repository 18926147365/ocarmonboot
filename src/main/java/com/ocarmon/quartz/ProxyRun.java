package com.ocarmon.quartz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocarmon.entity.Proxy;
import com.ocarmon.util.Common;
import com.ocarmon.util.HttpClientUtil;
import com.ocarmon.util.HttpCommonClient;

/**
 * @author 李浩铭
 * @since 2017年4月12日 下午4:58:53
 */
public class ProxyRun {

	public static void init() {
		getProxy();
//		Proxy proxy = Common.QUEUEPROXY.poll();
//		HttpClientUtil.init(proxy.getHost(), proxy.getPort(), proxy.getSpeedTime());
//		 run();
	}

	public static void run() {
		Runnable runnable = new Runnable() {
			public void run() {
				getProxyByXicidaili(1);
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 30, 30, TimeUnit.SECONDS);
	}
	public static void getProxy(){
		getProxyBy66Ip();
//		getProxyBy66IpPlace();
//		getProxyByXicidaili(1);
	}

	
	public static void getProxyBy66Ip(){
		String url="http://www.66ip.cn/mo.php?sxb=&tqsl=100&port=&export=&ktip=&sxa=&submit=%CC%E1++%C8%A1&textarea=";
		String content=null;
		try {
			content =new HttpClientUtil().getWebPage(url);
			Document doc = Jsoup.parse(content);
			String body=doc.getElementsByTag("body").toString();
			BufferedReader reader = new BufferedReader(new StringReader(body));
			String line=null;
			List<Proxy> hostList=new ArrayList<Proxy>();
			while((line=reader.readLine())!=null){
				line=line.trim();
				if(line.indexOf(":")!=-1 && line.indexOf("br")!=-1){
					line=line.replace("<br />", "").trim();
					int i=line.indexOf(":");
					String host=line.substring(0, i);
					Integer port= Integer.valueOf(line.substring(i+1,line.length()));
					Proxy proxy=new Proxy(host, port);
					hostList.add(proxy);
				}
				if(line.indexOf("<script type=\"text/javascript\">")!=-1){
					break;
				}
			}
			checkConnection(hostList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void getProxyBy66IpPlace(){
		String url="http://www.66ip.cn/mo.php?sxb=%B9%E3%B6%AB&tqsl=100&port=&export=&ktip=&sxa=%B5%E7%D0%C5&submit=%CC%E1++%C8%A1&textarea=";
		String content=null;
		try {
			content =new HttpClientUtil().getWebPage(url);
			Document doc = Jsoup.parse(content);
			String body=doc.getElementsByTag("body").toString();
			BufferedReader reader = new BufferedReader(new StringReader(body));
			String line=null;
			List<Proxy> hostList=new ArrayList<Proxy>();
			while((line=reader.readLine())!=null){
				line=line.trim();
				if(line.indexOf(":")!=-1 && line.indexOf("br")!=-1){
					line=line.replace("<br />", "").trim();
					int i=line.indexOf(":");
					String host=line.substring(0, i);
					Integer port= Integer.valueOf(line.substring(i+1,line.length()));
					Proxy proxy=new Proxy(host, port);
					hostList.add(proxy);
				}
				if(line.indexOf("<script type=\"text/javascript\">")!=-1){
					break;
				}
			}
			checkConnection(hostList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void getProxyByXicidaili(Integer s) {
		String url = "http://www.xicidaili.com/nn/" + s;
		String content = null;
		List<Proxy> hostList = new ArrayList<Proxy>();
		try {
			content =new HttpClientUtil().getWebPage(url);
		} catch (IOException e1) {
		}
		Document doc = Jsoup.parse(content);
		Element eid = doc.getElementById("ip_list");
		Elements e = eid.getElementsByTag("tr");
		for (int i = 1; i < e.size(); i++) {
			Elements data = e.get(i).getElementsByTag("td");
			String host = data.get(1).html();
			Integer port = Integer.valueOf(data.get(2).html());
			String location = data.get(3).getElementsByTag("a").html();
			Double speedTime = Double.valueOf(data.get(6).getElementsByTag("div").attr("title").replace("秒", ""));
			Double connectTime = Double.valueOf(data.get(7).getElementsByTag("div").attr("title").replace("秒", ""));
			String survivalTime = data.get(8).html();
			Integer time = 0;
			if (survivalTime.indexOf("小时") != -1) {
				time = Integer.valueOf(survivalTime.replace("小时", "")) * 60;
			}
			if (survivalTime.indexOf("天") != -1) {
				time = Integer.valueOf(survivalTime.replace("天", "")) * 60 * 24;
			}
			if (survivalTime.indexOf("分钟") != -1) {
				time = Integer.valueOf(survivalTime.replace("分钟", ""));
			}

			Proxy proxy = new Proxy(host, port, location, speedTime, connectTime, time);
			hostList.add(proxy);
		}
		// 批量验证连接速度
		checkConnection(hostList);

	}

	private static void checkConnection(List<Proxy> list) {
		if(list.size()==0){
			return;
		}
		System.out.println("开始批量验证代理连接.....");
		String url = "http://www.xdaili.cn/ipagent//checkIp/ipList?";
		StringBuffer params = new StringBuffer();
		for (Proxy proxy : list) {
			params.append("ip_ports[]=" + proxy.getHost() + ":" + proxy.getPort() + "&");
		}
		String contents = HttpCommonClient.httpClientUtil(url + params.toString(), "");
		JSONObject result = JSONObject.parseObject(contents);
		JSONArray array = JSONArray.parseArray(result.getString("RESULT"));
		int i = 0;
		for (Object obj : array) {
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(obj));
			if (data.getString("time") != null) {
				Integer time = Integer.valueOf(data.getString("time").replace("ms", ""));
				if (time < 1000) {
					i++;
					Proxy proxy = new Proxy();
					proxy.setHost(data.getString("ip"));
					proxy.setPort(Integer.valueOf(data.getString("port")));
					proxy.setLocation(data.getString("position"));
					proxy.setSpeedTime((double) time);
					Common.QUEUEPROXY.offer(proxy);
				}

			}
		}
		System.out.println("共验证代理:" + list.size() + " 可用代理:" + i);
	}

}
