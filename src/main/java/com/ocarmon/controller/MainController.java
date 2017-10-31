package com.ocarmon.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocarmon.dao.UserDao;
import com.ocarmon.entity.Proxy;
import com.ocarmon.entity.User;
import com.ocarmon.quartz.ProxyRun;
import com.ocarmon.splider.Splider;
import com.ocarmon.thread.SpliderThread;
import com.ocarmon.thread.UserTokenThread;
import com.ocarmon.util.AliyunOSSClient;
import com.ocarmon.util.Common;

/**
 * @author 李浩铭
 * @since 2017年9月4日 上午10:35:53
 */
@EnableAutoConfiguration
@RestController()
@RequestMapping("/zhihu")
public class MainController extends HttpServlet{

	@Autowired
	private UserDao userDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@RequestMapping("/test")
	public void Test() {
		System.out.println(userDao.findByUrlToken("shen-xian-19-90"));
	}
	@Override
	public void init() throws ServletException {
		runs();
		
	}
	@RequestMapping("/uploadImage")
	public void uploadImage(){
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
		int i=149619;
		while(true){
			System.out.println(sdf.format(new Date()));
			
			List<Map<String, Object>> list=jdbcTemplate.queryForList("select id,avatar_url from user limit "+i+",500");
			for (Map<String, Object> map : list) {
				String id=(String)map.get("id");
				AliyunOSSClient.uploadImage((String)map.get("avatar_url"), id);
				System.out.println("保存图片("+(i++)+"):"+id);
			}
			
		}
		
	}
	
	@RequestMapping("/runs")
	public String runs(){
		System.out.println("查询数据....");
	    List<Map<String, Object>> tokenList=jdbcTemplate.queryForList("SELECT url_Token FROM `user` ORDER BY create_time  LIMIT "+0+",20");
		for (Map<String, Object> map : tokenList) {
			String url_token=map.get("url_token")+"";
			Common.QUEUETOKENUSER.offer(url_token);
		}
		System.out.println("查询数据结束:"+Common.QUEUETOKENUSER.size());
		//抓取用户线程池
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int k = 0;k < 4; k++) {
			final int index = k;  
			cachedThreadPool.execute(new Runnable() {
			
				public void run() {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
					while(true){
						Map<String, Object> spliderMap= new Splider().getFollowUserList(Common.QUEUETOKENUSER.poll());
						if(spliderMap==null){
							Common.QUEUETOKENUSER.poll();
							System.out.println("map为空错误");
							Common.isNotSuccssCount++;
							if(Common.isNotSuccssCount>10){
								try {
									System.out.println(sdf.format(new Date()) +" 线程"+(index+1)+"暂停");
									Thread.sleep(1000*10*60);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							continue;
						}
						Common.isNotSuccssCount=0;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						List<Object> userTokenList=(List<Object>) spliderMap.get("followUser");
						
						for(int i=0;i<userTokenList.size();i++){
							String token=userTokenList.get(i)+"";
							
							if(token.equals("false") || token.equals("null") || Common.QUEUETOKENUSER.contains(token)){
								continue;
							}
							
							Common.QUEUETOKENUSER.offer(userTokenList.get(i)+"");
						}
						Common.SUCCESSCOUNT++;
						if(Common.SUCCESSCOUNT>300){
							try {
								System.out.println(sdf.format(new Date()) +" 线程"+(index+1)+"暂停,防止封IP");
								Thread.sleep(1000*30*60);
								Common.SUCCESSCOUNT=0;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						JSONArray followUserArray=(JSONArray) spliderMap.get("followUserArray");
						int successUser=0;
						for(int i=0;i<followUserArray.size();i++){
							JSONObject flUser=followUserArray.getJSONObject(i);
							User user=JSONObject.parseObject(flUser.toString(), User.class);
//							System.out.println(sdf.format(new Date()) +" 线程"+(index+1)+"抓取用户:"+ user.getName());
							if(user==null || StringUtils.isEmpty(user.getId()) || user.getName().equals("[已重置]") || user.getName()==null){
								System.out.println("用户错误:不存在的用户");
								Common.QUEUETOKENUSER.poll();
								continue;
							}
							if(userDao.findByUrlToken(user.getUrlToken())==null){
								successUser++;
								User myUser=userDao.save(user);
//								AliyunOSSClient.uploadImage(user.getAvatarUrl(), myUser.getId());
								System.out.println(sdf.format(new Date()) +" 线程"+(index+1)+"保存数据("+successUser+"):"+ myUser.getName());
							}
						}
						System.out.println("数据库总条数:"+userDao.count()+" 列队总数:"+Common.QUEUETOKENUSER.size() +"列队长度:"+Common.SUCCESSCOUNT+" 抓取的用户总数:"+followUserArray.size());
					}
				}
				
			});
		}
	
		
		return "success";
	}
	private static final String insertsUserSql(){
		StringBuffer sbBuffer=new StringBuffer("insert into user values");
			
		for (int i=0;i<10;i++) {
			User user=Common.DATAUSER.poll();
			sbBuffer.append("('"+user.getId()+"',");
			sbBuffer.append("'"+user.getName()+"',");
			sbBuffer.append("'"+user.getHeadline()+"',");
			sbBuffer.append(""+user.getGender()+",");
			sbBuffer.append("'"+user.getAvatarUrlTemplate()+"',");
			sbBuffer.append("'"+user.getType()+"',");
			sbBuffer.append("'"+user.getUrl()+"',");
			sbBuffer.append(""+user.getAnswerCount()+",");
			sbBuffer.append(""+user.getArticlesCount()+",");
			sbBuffer.append(""+user.getFollowerCount()+",");
			sbBuffer.append("'"+user.getUrlToken()+"',");
			sbBuffer.append("'"+user.getAvatarUrl()+"',sysdate()),");
		}
		return sbBuffer.substring(0, sbBuffer.toString().length()-1);
	}
	@RequestMapping("/run")
	public String run() {
		// 防止初始队列线程中没有token 初始新增几个token
		Queue<String> iniQue = Common.QUEUETOKEN;
		int rad=(int) (Math.random()*1000);
		Map<String, Object> user= jdbcTemplate.queryForMap("SELECT url_Token FROM `user` ORDER BY create_time DESC LIMIT "+rad+",1");
		String urlToken = (String) user.get("url_Token");
		if (urlToken == null || urlToken.equals("null")) {
			urlToken = "ma-liu";
		}
		// urlToken=user.get(0).getUrlToken();
		System.out.println("新增队列中.....");
		// ProxyRun.init();// 是否使用代理
		 new Thread(new UserTokenThread(urlToken, Common.QUEUEPROXY.poll())).start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// // 用户队列线程
		int threadNum = Common.QUEUEPROXY.size();
		if (threadNum >= 2) {
			threadNum = 2;
		}
		threadNum = 8;
		for (int i = 0; i < threadNum; i++) {
			Proxy proxy = Common.QUEUEPROXY.poll();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SpliderThread spliderThread=new SpliderThread(i+1, proxy);
			 new Thread(spliderThread).start();
		}
		return "success   running";
	}

}
