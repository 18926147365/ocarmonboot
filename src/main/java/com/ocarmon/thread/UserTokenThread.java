package com.ocarmon.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocarmon.entity.Proxy;
import com.ocarmon.entity.User;
import com.ocarmon.splider.Splider;
import com.ocarmon.util.Common;
import com.ocarmon.util.SpringContextUtil;


/**
 * @author 李浩铭
 * @since 2017年7月6日 上午9:36:17
 */
public class UserTokenThread implements Runnable {

	private String token;
	private Proxy proxy;
	public UserTokenThread(){
		proxy=new Proxy();
	}
	public UserTokenThread(String token,Proxy proxy) {
		this.token = token;
		this.proxy=proxy;
	}

	@Override
	public void run() {
		Common.QUEUETOKENUSER.offer(token);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (Common.QUEUETOKENUSER.size() != 0 ) {
			if(Common.noSuccess<9){
				findUserToken(Common.QUEUETOKENUSER.poll());
				System.out.println("队列长度:"+Common.QUEUETOKENUSER.size());
			}
				
		
//			
	}

	}

	private void findUserToken(String token) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			map = new Splider(proxy).getFollowUserList(token);
			if(map==null){
				Common.QUEUETOKENUSER.offer(token);
				return ;
			}
			JSONArray followUserArray=(JSONArray) map.get("followUserArray");
			for(int i=0;i<followUserArray.size();i++){
				JSONObject flUser=followUserArray.getJSONObject(i);
				User user=JSONObject.parseObject(flUser.toString(), User.class);
				if (!SpringContextUtil.isExistsUser(user.getUrlToken())) {
					System.out.println("抓取用户:"+user.getName());
					SpringContextUtil.insetInfo(user);
				}
				
			}
			List<Object> followUser = (List<Object>) map.get("followUser");
			if (followUser != null) {
				for (Object use : followUser) {
					if (use instanceof String) {
						// 进队
						Common.QUEUETOKENUSER.offer(use+"");
						Common.QUEUETOKEN.offer(use + "");
					}
					if (use instanceof Boolean) {
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("队列错误:"+e.getMessage());
			Common.QUEUETOKENUSER.offer(token);
			proxy=Common.QUEUEPROXY.poll();
		}
	
	}

}
