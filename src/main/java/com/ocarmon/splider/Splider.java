package com.ocarmon.splider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.ocarmon.entity.Proxy;
import com.ocarmon.entity.User;
import com.ocarmon.util.Common;
import com.ocarmon.util.HttpClientUtil;

/**
 * @author 李浩铭
 * @since 2017年6月30日 下午5:13:08 抓取关键信息
 */
public class Splider {

	private Proxy proxy;
	private HttpClientUtil httpClientUtil = new HttpClientUtil();

	public Splider() {
	}

	public Splider(Proxy proxy) {
		this.proxy = proxy;
	}

	
	// User user=new User();
	//
	// Session session=HibernateUtil.getSession();
	// try {
	// Transaction transaction=session.beginTransaction();
	// BeanUtils.populate(user, linkMap);
	// session.save(user);
	// transaction.commit();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally {
	// HibernateUtil.closeSession();
	// System.exit(0);
	// }
	//

	public static void main(String[] args) {
		new Splider().getFollowUserList("ma-liu");
	}
	
	public Map<String, Object> getFollowUserList(String userToken) {
		String content = null;
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			content = httpClientUtil.getWebPage("https://www.zhihu.com/people/" + userToken + "/following");
			Document doc = Jsoup.parse(content);
			String jsonurl = doc.select("[data-state]").first().attr("data-state");
			Object oji=JsonPath.read(jsonurl, "$.entities.users.*");
			
			JSONArray followUserArray=  JSONArray.parseArray(oji+"");
			List<Object> followUser = JsonPath.read(jsonurl, "$.people.followingByUser." + userToken + ".ids");
			Map<String, Object> user = JsonPath.read(jsonurl, "$.entities.users." + userToken);
			User myUser = new User();
			BeanUtils.populate(myUser, user);
			result.put("followUser", followUser);
			result.put("user", myUser);
			result.put("followUserArray", followUserArray);
			
			Common.isNotSuccssCount = 0;
		} catch (Exception e) {
			return null;
		}

		return result;

	}
	
}
