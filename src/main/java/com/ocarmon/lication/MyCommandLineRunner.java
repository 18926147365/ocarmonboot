/**
 * 
 */
package com.ocarmon.lication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.HttpClientUtils;
import com.ocarmon.util.HttpClientUtil;


/** 
* @author 李浩铭 
* @since 2017年10月11日 上午9:25:22
*/
@Component
public class MyCommandLineRunner implements CommandLineRunner{

	@Value("${spring.url}")
	private String url;
	
	@Override
	public void run(String... arg0) throws Exception {
		
		HttpClientUtil httpClientUtil=new HttpClientUtil();
		
//	httpClientUtil.getWebPage(url+"zhihu/runs.do");
		
	}

}
