package com.ocarmon.splider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ocarmon.entity.User;
import com.ocarmon.util.Common;
import com.ocarmon.util.SpringContextUtil;

/**
 * @author 李浩铭
 *
 * @time 2017年10月20日 下午10:36:36
 */
public class CacheThreadPool {
	public static void main(String[] args) {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		Common.QUEUETOKENUSER.offer("huan-du-wo-shu");
		Common.QUEUETOKENUSER.offer("ji-xuan-yi-9");
		Common.QUEUETOKENUSER.offer("deng-pu-85");
		for (int k = 0;k < 3; k++) {
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					while(true){
						Map<String, Object> spliderMap= new Splider().getFollowUserList(Common.QUEUETOKENUSER.poll());
						if(spliderMap==null){
							continue;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						List<Object> userTokenList=(List<Object>) spliderMap.get("followUser");
						
						for(int i=0;i<userTokenList.size();i++){
							Common.QUEUETOKENUSER.offer(userTokenList.get(i)+"");
						}
						JSONArray followUserArray=(JSONArray) spliderMap.get("followUserArray");
						for(int i=0;i<followUserArray.size();i++){
							JSONObject flUser=followUserArray.getJSONObject(i);
							User user=JSONObject.parseObject(flUser.toString(), User.class);
							Common.DATAUSER.offer(user);
						}
					}
				}
			});
		}
	}

}
